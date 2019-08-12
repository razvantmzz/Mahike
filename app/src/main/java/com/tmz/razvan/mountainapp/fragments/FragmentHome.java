package com.tmz.razvan.mountainapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmz.razvan.mountainapp.Adapters.RecommendedListAdapter;
import com.tmz.razvan.mountainapp.Adapters.SearchAreasListAdapter;
import com.tmz.razvan.mountainapp.Helpers.GetUrlData;
import com.tmz.razvan.mountainapp.Helpers.TaskLoadedCallback;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class FragmentHome extends Fragment implements TaskLoadedCallback {

    private FirebaseFunctions mFunctions;
    private RecyclerView hikesRecylerView;
    private RecommendedListAdapter mAdapter;
    private List<ModelWrapper> mRecommendedData;
    private android.content.Context mContext;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_fragment_home, container, false);

        mFunctions = FirebaseFunctions.getInstance();
        setUpViews(fragmentView);
        setUpHikesList();
        SetTranslationStrings(fragmentView);
        getRecommendedHikeList();
        return fragmentView;
    }

    // region private methods

    private void setUpViews(View view)
    {
        hikesRecylerView = view.findViewById(R.id.rv_fragment_home_recommended_hikes);
    }

    private void getRecommendedHikeList()
    {
        String url = "https://us-central1-mountainapp-de974.cloudfunctions.net/getRecommendedHikes";
        HashMap<String, String> data = new HashMap<>();
        data.put("exp", "0");
        new GetUrlData(this.getContext()).execute(url, data).setOnTaskLoadedCallback(this);
    }

    private void setUpHikesList()
    {
//        hikesRecylerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        hikesRecylerView.setLayoutManager(layoutManager);
//        mAdapter = new SearchHikesListAdapter(mHikesList, this.getContext());
        mAdapter = new RecommendedListAdapter(mRecommendedData, this.getContext());
        hikesRecylerView.setAdapter(mAdapter);
    }

    private void SetTranslationStrings(View fragmentView) {
    }

    @Override
    public void onTaskDone(Object... values) {
        String serialisedList = (String) values[0];
        Gson g = new Gson();
        Type listType = new TypeToken<List<ModelWrapper>>(){}.getType();
        List<ModelWrapper> groups = g.fromJson(serialisedList,  listType);
        mRecommendedData = groups;
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.mDataset = mRecommendedData;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public class ModelWrapper
    {
        HikeModel hikeModel;
        AreaModel areaModel;

        public HikeModel getHikeModel() {
            return hikeModel;
        }

        public void setHikeModel(HikeModel hikeModel) {
            this.hikeModel = hikeModel;
        }

        public AreaModel getAreaModel() {
            return areaModel;
        }

        public void setAreaModel(AreaModel areaModel) {
            this.areaModel = areaModel;
        }
    }

    //endregion
}
