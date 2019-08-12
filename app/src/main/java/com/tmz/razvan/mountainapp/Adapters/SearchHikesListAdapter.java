package com.tmz.razvan.mountainapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Helpers.GoogleMapsHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.activities.HikeActivity;
import com.tmz.razvan.mountainapp.models.HikeCoordinates;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class SearchHikesListAdapter extends RecyclerView.Adapter<SearchHikesListAdapter.MyViewHolder> {
    private List<HikeModel> mDataset;
    private  Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private int lastSelectedPosition = -1;
    private MyViewHolder lastHolder;
    public GoogleMap mMap;
    private Polyline selectedPolyline;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rootView;
        public RelativeLayout detailsContainer;
        public TextView detailsTextView;
        public TextView name;
//        public TextView difficulty;
        public TextView duration;
        public ImageView image;

        public MyViewHolder(RelativeLayout rootView) {
            super(rootView);
            this.rootView = rootView;

            detailsContainer = rootView.findViewById(R.id.rl_row_search_hikes_details_container);
            detailsTextView = rootView.findViewById(R.id.tv_row_search_hike_details);

            name = rootView.findViewById(R.id.tv_row_search_hike_name);
//            difficulty = rootView.findViewById(R.id.tv_row_search_difficulty);
            duration = rootView.findViewById(R.id.tv_row_search_duration);
            image = rootView.findViewById(R.id.iv_row_search);
        }
    }

    public SearchHikesListAdapter(List<HikeModel> myDataset, Context context, GoogleMap googleMap) {
        mDataset = myDataset;
        mContext = context;
        mMap = googleMap;
    }

    @Override
    public SearchHikesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
    int viewType) {
        // create a new view
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_hikes, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ShowDetailsOverlay(holder, lastSelectedPosition, false);

        holder.name.setText(mDataset.get(position).getName());
//        holder.difficulty.setText("Difficulty level: " + mDataset.get(position).getDifficulty());
        holder.duration.setText("Duration: " + mDataset.get(position).getDuration());

        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(3, 2),
                new ColorFilterTransformation(0x000)
        );

        Glide.with(mContext).load(mDataset.get(position).getImageUrl().get(0))
                .transform(multi).into(holder.image);
//        Picasso.get().load(mDataset.get(position).getImageUrl().get(0)).into(holder.image);

        holder.detailsTextView.setText(TranslationConstants.SEE_DETAILS);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if(lastSelectedPosition != position)
                   {
                       if(lastSelectedPosition != -1)
                       {
                           ShowDetailsOverlay(lastHolder, lastSelectedPosition, false);
                       }
                       ShowDetailsOverlay(holder, position, true);
                       lastSelectedPosition = position;
                       lastHolder  = holder;
                   }
                   setMapDecorations(mDataset.get(position));
            }
        });

        holder.detailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHikeDetails(mDataset.get(position));
            }
        });
    }

    private void ShowDetailsOverlay(MyViewHolder holder, int position, boolean visibile)
    {
        if(visibile)
        {
            holder.detailsTextView.setVisibility(View.VISIBLE);
            holder.detailsContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.detailsTextView.setVisibility(View.GONE);
            holder.detailsContainer.setVisibility(View.GONE);
        }
    }

    private void setMapDecorations(HikeModel hikeModel)
    {
        if(selectedPolyline != null)
        {
            selectedPolyline.remove();
        }

        HikeCoordinates hikeCoordinates = hikeModel.getCoordinates();
        LatLng startPoint = new LatLng(hikeCoordinates.getStartPoint().getLatitude(), hikeCoordinates.getStartPoint().getLongitude());
        LatLng endPoint = new LatLng(hikeCoordinates.getEndPoint().getLatitude(), hikeCoordinates.getEndPoint().getLongitude());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        PolylineOptions polylineWhite = GoogleMapsHelper.getPolylineOptions(hikeModel.getPolyline());
        polylineWhite.width(10).color(Color.BLACK).zIndex(2);

        selectedPolyline = mMap.addPolyline(polylineWhite);

        builder.include(startPoint);
        builder.include(endPoint);

        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 200);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap. moveCamera(cu);// animateCamera(cu);
            }
        });
    }

    private void navigateToHikeDetails(HikeModel hikeModel)
    {
        Intent intent = new Intent(mContext, HikeActivity.class);
        String serialisedHikeModel = new Gson().toJson(hikeModel);
        intent.putExtra(NavigationContants.HIKE_MODEL_KEY, serialisedHikeModel);
        mContext.startActivity(intent);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}