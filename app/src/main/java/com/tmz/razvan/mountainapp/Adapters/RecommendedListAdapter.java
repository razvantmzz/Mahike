package com.tmz.razvan.mountainapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.tmz.razvan.mountainapp.Interfaces.IHikeData;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.activities.HikeActivity;
import com.tmz.razvan.mountainapp.fragments.FragmentHome;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeCoordinates;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class RecommendedListAdapter extends RecyclerView.Adapter {

    private static int HIKE_TYPE = 0;
    private static int AREA_TYPE = 1;

    public List<FragmentHome.ModelWrapper> mDataset;
    private  Context mContext;

    public static class AreaViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView titleTextView;

        public AreaViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;

            titleTextView = rootView.findViewById(R.id.tv_row_recommended_area_title);
        }
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public RelativeLayout detailsContainer;
        public TextView detailsTextView;
        public TextView name;
        //        public TextView difficulty;
        public TextView duration;
        public ImageView image;

        public HikeViewHolder(View rootView) {
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

    public RecommendedListAdapter(List<FragmentHome.ModelWrapper> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view;
        if(viewType == HIKE_TYPE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_search_hikes, parent, false);
            HikeViewHolder hv = new HikeViewHolder(view);
            return hv;
        }

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recommended_area, parent, false);
        AreaViewHolder av = new AreaViewHolder(view);

        return av;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof HikeViewHolder)
        {
            HikeViewHolder hv = (HikeViewHolder)viewHolder;
            hv.rootView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;

            HikeModel hike = mDataset.get(i).getHikeModel();
            hv.name.setText(hike.getName());
            hv.duration.setText(String.format("Duration: %s", hike.getDuration()));

            MultiTransformation multi = new MultiTransformation(
                    new BlurTransformation(3, 2),
                    new ColorFilterTransformation(0x000)
            );
            Glide.with(mContext).load(hike.getImageUrl().get(0))
                    .transform(multi).into(hv.image);
            return;
        }

        AreaViewHolder av = (AreaViewHolder)viewHolder;
        AreaModel area = mDataset.get(i).getAreaModel();

        av.titleTextView.setText(String.format("%s %s", TranslationConstants.EXPLORE, area.getMountainName()));

    }

    @Override
    public int getItemCount() {
        if(mDataset == null)
        {
            return 0;
        }
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        FragmentHome.ModelWrapper data = mDataset.get(position);
        if(data.getAreaModel() == null)
        {
            return HIKE_TYPE;
        }
        return AREA_TYPE;
    }
}