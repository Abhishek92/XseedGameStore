package com.android.gamestore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gamestore.api.GameApiHitModel;
import com.android.gamestore.api.GameStoreApiClient;
import com.android.gamestore.api.GamesDetailModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    private GameListAdapter mAdapter;
    private TextView mApiCount;
    private TextView mGameCount;
    private List<GamesDetailModel> gameList;
    private int TOTAL_GAME_COUNT = 0;
    private ProgressBar mProgressBar;

    private Comparator<GamesDetailModel> ratingComparator = new Comparator<GamesDetailModel>() {
        @Override
        public int compare(GamesDetailModel lhs, GamesDetailModel rhs) {
            return Float.valueOf(rhs.getRating()).compareTo(Float.valueOf(lhs.getRating()));
        }
    };

    private Comparator<GamesDetailModel> priceComparator = new Comparator<GamesDetailModel>() {
        @Override
        public int compare(GamesDetailModel lhs, GamesDetailModel rhs) {
            return Float.valueOf(rhs.getPrice()).compareTo(Float.valueOf(lhs.getPrice()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mApiCount = (TextView) findViewById(R.id.apiCount);
        mGameCount = (TextView) findViewById(R.id.gameCount);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mToolbar.setTitle(getString(R.string.game));
        mToolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mToolbar);

        getGamesList();
        getNoOfApiHits();

        gameList = new ArrayList<>();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GamesDetailModel model = (GamesDetailModel) adapterView.getItemAtPosition(i);
                EventBus bus = EventBus.getDefault();
                bus.postSticky(model);
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                mAdapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                mAdapter.getFilter().filter(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_price) {
            sortList(priceComparator);
            return true;
        }

        if (id == R.id.action_sort_rating) {
            sortList(ratingComparator);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getGamesList()
    {
        if(Util.isNetworkConnected(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            GameStoreApiClient.getGameStoreApi().getGamesDetail(new Callback<List<GamesDetailModel>>() {
                @Override
                public void success(List<GamesDetailModel> gamesDetailModels, Response response) {
                    if (response.getStatus() == 200) {
                        mProgressBar.setVisibility(View.GONE);
                        gameList = new ArrayList<GamesDetailModel>(gamesDetailModels);
                        mAdapter = new GameListAdapter(MainActivity.this, gameList);
                        mListView.setAdapter(mAdapter);
                        TOTAL_GAME_COUNT = gamesDetailModels.size();
                        mGameCount.setText("Game Count: " + TOTAL_GAME_COUNT);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
            Toast.makeText(this,getString(R.string.check_internet),Toast.LENGTH_LONG).show();
    }

    private void getNoOfApiHits()
    {
        if(Util.isNetworkConnected(this)) {
            GameStoreApiClient.getGameStoreApi().getApiCounter(new Callback<GameApiHitModel>() {
                @Override
                public void success(GameApiHitModel gameApiHitModel, Response response) {
                    if(response.getStatus() == 200)
                        mApiCount.setText("Api Count: "+gameApiHitModel.getApiHits());
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    private void sortList(Comparator<GamesDetailModel> comparator)
    {
        Collections.sort(gameList, comparator);
        mAdapter = new GameListAdapter(this, gameList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
