package com.tmz.razvan.mountainapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.RelativeLayout;

import com.tmz.razvan.mountainapp.Adapters.SearchAreasListAdapter;
import com.tmz.razvan.mountainapp.Adapters.SearchHikesListAdapter;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Helpers.InternetHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.baseClasses.BaseFragment;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.List;

public class FragmentSearch extends BaseFragment {

//    private List<HikeModel> mHikesList;
    private List<AreaModel> areasList;
    private RecyclerView searchHikesRecyclerView;
    private LinearLayoutManager layoutManager;
    private SearchAreasListAdapter mAdapter;
    private TextView mSearchBar;
    private RelativeLayout mSearchContainer;
    private ImageView mSearchViewBack;

    public FragmentSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.fragment_fragment_search, container, false);


        areasList = CoreData.getInstance().getAreasList();
        if(areasList == null)
        {
            return null;
        }

        InitialiseViews(view);
        setUpHikesList();

        mSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContainer.setVisibility(View.VISIBLE);
            }
        });

        mSearchViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContainer.setVisibility(View.GONE);
            }
        });

        return view;

    }

    //region private methods

    private void InitialiseViews(View view)
    {
        searchHikesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_search_fragment_hikes_list);
        mSearchBar = (TextView) view.findViewById(R.id.tv_fragment_search_search_box);
        mSearchContainer = (RelativeLayout) view.findViewById(R.id.rl_fragment_search_search_view_container);
        mSearchViewBack = (ImageView)view.findViewById(R.id.iv_back_to_list_button);
    }

    private void setUpHikesList()
    {
        searchHikesRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        searchHikesRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchAreasListAdapter(areasList, this.getContext());
        searchHikesRecyclerView.setAdapter(mAdapter);
    }

    //endregion
}
