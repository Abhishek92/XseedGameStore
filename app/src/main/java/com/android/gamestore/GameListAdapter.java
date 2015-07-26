package com.android.gamestore;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.gamestore.api.GamesDetailModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hp pc on 11-07-2015.
 */
public class GameListAdapter extends ArrayAdapter<GamesDetailModel> {

    private List<GamesDetailModel> gameList;
    private Context mContext;


    public GameListAdapter(Context context, List<GamesDetailModel> objects) {
        super(context, R.layout.game_list_item, objects);
        mContext = context;
        gameList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.game_list_item, parent, false);
            holder = new CustomViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.textView = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }
        final GamesDetailModel detailModel = getItem(position);
        holder.textView.setText(detailModel.getDescription());
        holder.price.setText("Price: "+detailModel.getPrice());
        Picasso.with(mContext).load(detailModel.getImage()).into(holder.imageView);
        return convertView;

    }

    class CustomViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected TextView price;
    }
}