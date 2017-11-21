package com.matheusflausino.closetapp.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.matheusflausino.closetapp.R;
import com.matheusflausino.closetapp.model.Clothes;

import java.util.ArrayList;

/**
 * Created by matheusflausino on 06/11/17.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Clothes> data;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Clothes> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.like = (ImageView) row.findViewById(R.id.likeView);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Clothes item = data.get(position);

        if(item.getFavorite() == 1){
            holder.like.setImageResource(R.drawable.ic_liked);
            holder.like.setTag(R.drawable.ic_liked);
        }else{
            holder.like.setImageResource(R.drawable.ic_like);
            holder.like.setTag(R.drawable.ic_like);
        }

        UtilFoto.setImage(item.getImage(), holder.image, 120,160);

//        final ViewHolder finalHolder = holder;
//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = (int) finalHolder.like.getTag();
//                if( id == R.drawable.ic_like){
//                    finalHolder.like.setTag(R.drawable.ic_liked);
//                    finalHolder.like.setImageResource(R.drawable.ic_liked);
//
//                }else{
//                    finalHolder.like.setTag(R.drawable.ic_like);
//                    finalHolder.like.setImageResource(R.drawable.ic_like);
//                }
//            }
//        });

        return row;
    }

    static class ViewHolder {
        ImageView like;
        ImageView image;
    }
}
