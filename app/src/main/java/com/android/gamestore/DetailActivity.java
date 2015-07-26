package com.android.gamestore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.gamestore.api.GamesDetailModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class DetailActivity extends AppCompatActivity implements
        ShareActionProvider.OnShareTargetSelectedListener {

    private GamesDetailModel gamesDetailModel;
    private TextView mGameName;
    private TextView mRating;
    private RatingBar mRatingBar;
    private WebView mChartView;
    private ImageView mGameImage;
    private final String PIE_CHART_URL = "file:///android_asset/PieChart.html";
    private Toolbar mToolBar;
    private List<GamesDetailModel.Demographic> demographicList;
    private JSONObject mJsonData;
    private JSONArray mJsonArray;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mGameName = (TextView) findViewById(R.id.title);
        mRating = (TextView) findViewById(R.id.rating);
        mGameImage = (ImageView) findViewById(R.id.gameImg);
        mChartView = (WebView)findViewById(R.id.pieChart);
        mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        gamesDetailModel = EventBus.getDefault().removeStickyEvent(GamesDetailModel.class);

        if(gamesDetailModel != null)
            setValues();

        setUpToolBar();
    }

    private void setUpToolBar()
    {
        mToolBar.setTitle(getString(R.string.game_detail));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setValues()
    {
        mGameName.setText(gamesDetailModel.getName());
        mRating.setText(gamesDetailModel.getRating());
        Picasso.with(this).load(gamesDetailModel.getImage()).into(mGameImage);
        mRatingBar.setRating(Float.parseFloat(gamesDetailModel.getRating()));
        demographicList = new ArrayList<>(gamesDetailModel.getDemographic());
        
        if(demographicList.size() != 0)
            createJson();

        loadChart();
    }

    private void loadChart()
    {
        WebSettings settings = mChartView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        mChartView.addJavascriptInterface(new JsonString(), "jsonString");
        mChartView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });
        mChartView.setWebViewClient(new ChartWebViewClient(DetailActivity.this));
        mChartView.loadUrl(PIE_CHART_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = (MenuItem) menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(getDefaultShareIntent(gamesDetailModel.getName(),gamesDetailModel.getUrl()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_app_store) {
            openPlayStore(gamesDetailModel.getUrl());
            return true;
        }
        if (id == R.id.action_send_sms) {
            sendPlayStoreLinkThroughSms(gamesDetailModel.getName(),gamesDetailModel.getUrl());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createJson() {
        try {
            mJsonArray = new JSONArray();
            for (int i = 0; i < demographicList.size(); i++) {
                JSONObject object = new JSONObject();
                object.put("country", demographicList.get(i).getCountry());
                object.put("percentage", demographicList.get(i).getPercentage());
                mJsonArray.put(object);
            }
            mJsonData = new JSONObject();
            mJsonData.put("data", mJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Returns a share intent */
    private Intent getDefaultShareIntent(String name,String link){
        String text = "Hey check out this cool game,"+name+" here is the play store link: \n"+link;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        return intent;
    }

    private void openPlayStore(String link)
    {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        }
    }

    private void sendPlayStoreLinkThroughSms(String name, String link)
    {
        String sms_text = "Hey check out this cool game,"+name+" here is the play store link: \n"+link;
        Uri uri = Uri.parse("smsto:" + "");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", sms_text);
        startActivity(intent);
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        return false;
    }

    private class JsonString {
        @JavascriptInterface
        public String getJsonString() {
            return mJsonData.toString();
        }
    }
}
