package com.tmz.razvan.mountainapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmz.razvan.mountainapp.Helpers.BitmapHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.models.internal.FeatureRowItem;

import java.util.List;

public class HikeFeatureSpinnerAdapter extends ArrayAdapter<FeatureRowItem> {

    LayoutInflater flater;
    Context mContex;

    public HikeFeatureSpinnerAdapter(Activity context, int resouceId, int textviewId, List<FeatureRowItem> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
        mContex = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeatureRowItem rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.row_feature_spinner,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.tv_row_feature_spinner_text);
        txtTitle.setText(rowItem.getTitle());

        ImageView imageView = (ImageView) rowview.findViewById(R.id.iv_row_feature_spinner_image);
        imageView.setImageResource(rowItem.getImageId());
        return rowview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FeatureRowItem rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.row_feature_spinner,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.tv_row_feature_spinner_text);
        txtTitle.setText(rowItem.getTitle());

        ImageView imageView = (ImageView) rowview.findViewById(R.id.iv_row_feature_spinner_image);
        imageView.setImageResource(rowItem.getImageId());
        return rowview;
    }
}
