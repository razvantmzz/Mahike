package com.tmz.razvan.mountainapp.baseClasses;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmz.razvan.mountainapp.CustomUIViews.ProgressDialog;
import com.tmz.razvan.mountainapp.Interfaces.OnBusyChangedListener;
import com.tmz.razvan.mountainapp.R;

import java.util.ArrayList;
import java.util.List;

public class BaseAppCompat extends AppCompatActivity {

    private ProgressDialog mProgressView;
    private RelativeLayout mProgressViewContainer;
    private boolean IsBusy;
    protected OnBusyChangedListener mOnBusyChangedListener;

    public ImageView BackButton;
    public RelativeLayout Toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        initialiseProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseBackButton();
        Toolbar = findViewById(R.id.toolbar);
    }

    //region public methods

    public boolean IsBusy()
    {
        return IsBusy;
    }

    public void SetIsBusy(boolean busy)
    {
        SetIsBusy(busy, true);
    }

    public void SetIsBusy(boolean busy, boolean showProgressDialog)
    {
        IsBusy = busy;
        if(showProgressDialog)
        {
            mProgressView.showProgress(busy);
        }
        if (mOnBusyChangedListener != null)
        {
            mOnBusyChangedListener.onChanged(busy);
        }
    }

    public void SetToolbarTitle(String title)
    {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
    }

    public void addRightButton(Button button)
    {
        LinearLayout btnContainer = findViewById(R.id.ll_toolbar_btn_container);
        if(btnContainer == null)
        {
            return;
        }
//        ArrayList<View> viewList = new ArrayList<>();
//        viewList.add(button);
        btnContainer.addView(button);
    }

    //endregion

    //region private methods

    private  void initialiseBackButton()
    {
        BackButton = findViewById(R.id.toolbar_back);
        if(BackButton != null)
        {
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    private void initialiseProgressDialog()
    {
        RelativeLayout container = findViewById(R.id.view_container);
        mProgressView = new ProgressDialog(this, container);
    }

    //endregion
}
