package com.tmz.razvan.mountainapp.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Adapters.HikeFeatureSpinnerAdapter;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Helpers.PermissionHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.AddBaseActivity;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.enums.HikeFeatureType;
import com.tmz.razvan.mountainapp.models.HikeFeature;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.models.UserNote;
import com.tmz.razvan.mountainapp.models.internal.FeatureRowItem;

import java.util.ArrayList;
import java.util.List;

public class HikeFeatureActivity extends AddBaseActivity {
    Spinner spinner;
    List<FeatureRowItem> rowItems;
    private HikeModel mCurrentHike;
    private HikeFeature mCurrentHikeFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_hike_feature);
        String serialisedHikeModel = getIntent().getStringExtra(NavigationContants.HIKE_MODEL_KEY);
        String serialisedHikeFeature = getIntent().getStringExtra(NavigationContants.BASE_FEATURE);
        mCurrentHike = new Gson().fromJson(serialisedHikeModel, HikeModel.class);
        mCurrentHikeFeature = new Gson().fromJson(serialisedHikeFeature, HikeFeature.class);
        if(mCurrentHikeFeature == null)
        {
            mCurrentHikeFeature = new HikeFeature();
            mCurrentHikeFeature.setHikeId(mCurrentHike.getId());
            mCurrentHikeFeature.setAuthorId(UserCore.Instance().User.getUserId());
        }
        mCurrentBaseFeature = mCurrentHikeFeature;
        if(mCurrentBaseFeature.getImageUrlList() != null)
        {
            mUriList.addAll(mCurrentBaseFeature.getImageUrlList());
        }
        super.onCreate(savedInstanceState);

        ref = storageReference.child("hikeFeatureData/").child(mCurrentHike.getId() + "/").child("userNoteData")
                    .child(mCurrentBaseFeature.getId());
        InitialiseItems();
        SetTranslationStrings();
        InitialiseSpinner();
        ApplyPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void SyncFeatureData() {

        FeatureRowItem seletedTpe = (FeatureRowItem) spinner.getSelectedItem();
        mCurrentHikeFeature.setType(seletedTpe.getType());
        mCurrentHikeFeature.setImageUrlList(mCurrentBaseFeature.getImageUrlList());
        mCurrentHike.addFeature(mCurrentHikeFeature);
        CoreData.getInstance().updateHike(mCurrentHike);
        FirebaseHelper.Instance().SyncHikesData(mCurrentHike);
    }

    //region private methods

    private void InitialiseSpinner()
    {
        if(shouldEdit == false)
        {
            spinner.setEnabled(false);
            spinner.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        rowItems = new ArrayList<>();
        rowItems.add(new FeatureRowItem(TranslationConstants.WATERSOURCE, R.drawable.ic_water_cource, HikeFeatureType.WATER_SOURCE));
        rowItems.add(new FeatureRowItem(TranslationConstants.SHELTER, R.drawable.ic_shelter, HikeFeatureType.SHELTER));
        rowItems.add(new FeatureRowItem(TranslationConstants.CABIN, R.drawable.ic_cabin, HikeFeatureType.CABIN));
        rowItems.add(new FeatureRowItem(TranslationConstants.RESCUE_POINT, R.drawable.ic_rescue_point, HikeFeatureType.RESCUE_POINT));

        HikeFeatureSpinnerAdapter adapter = new HikeFeatureSpinnerAdapter(HikeFeatureActivity.this,
                R.layout.row_feature_spinner, R.id.tv_row_feature_spinner_text, rowItems);
        spinner.setAdapter(adapter);
        spinner.setSelection(mCurrentHikeFeature.getType() -1);
    }

    private void InitialiseItems()
    {
        spinner = (Spinner)findViewById(R.id.sp_activity_hike_feature_feature);
    }

    private void ApplyPermissions()
    {
        if(!shouldEdit && !PermissionHelper.userCanEditFeature(mCurrentHikeFeature.getAuthorId()))
        {
            mEditSaveButton.setVisibility(View.GONE);

        }
    }

    private void SetTranslationStrings()
    {

    }

    //endregion
}
