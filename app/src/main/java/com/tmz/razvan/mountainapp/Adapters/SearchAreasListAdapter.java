package com.tmz.razvan.mountainapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Helpers.OfflineHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.activities.AreaMapActivity;
import com.tmz.razvan.mountainapp.activities.HikeActivity;
import com.tmz.razvan.mountainapp.models.AreaModel;

import java.util.List;

public class SearchAreasListAdapter extends RecyclerView.Adapter<SearchAreasListAdapter.AreaViewHolder> {

    private List<AreaModel> mDataset;
    private  Context mContext;

    public SearchAreasListAdapter(List<AreaModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public SearchAreasListAdapter.AreaViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_area_search, parent, false);

        SearchAreasListAdapter.AreaViewHolder vh = new SearchAreasListAdapter.AreaViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SearchAreasListAdapter.AreaViewHolder holder, final int position) {

        final AreaModel area = mDataset.get(position);
        holder.nameTextView.setText(area.getName());
        holder.mountainNameTextView.setText(area.getMountainName());

        holder.detailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AreaMapActivity.class);
                intent.putExtra(NavigationContants.AREA_ID_KEY, area.getId());
                mContext.startActivity(intent);
            }
        });

        holder.saveOfflineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineHelper offlineHelper = new OfflineHelper();
                offlineHelper.SaveAreaOnDevice(mContext, area);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView nameTextView;
        public TextView mountainNameTextView;
        public LinearLayout detailsContainer;
        public Button saveOfflineButton;

        public AreaViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            nameTextView = rootView.findViewById(R.id.tv_row_area_search_name);
            mountainNameTextView = rootView.findViewById(R.id.tv_row_area_search_mountain_name);
            detailsContainer = rootView.findViewById(R.id.ll_container);
            saveOfflineButton = rootView.findViewById(R.id.btn_row_area_search_offline);
        }
    }
}