package com.tmz.razvan.mountainapp.baseClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmz.razvan.mountainapp.CustomUIViews.ProgressDialog;
import com.tmz.razvan.mountainapp.Interfaces.OnBusyChangedListener;

public class BaseFragment  extends Fragment {

    protected ProgressDialog mProgressView;

    private boolean IsBusy;
    protected OnBusyChangedListener mOnBusyChangedListener;
    ViewGroup mContainer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        mContainer = container;

        return view;
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
        if(mProgressView == null)
        {
            InitialiseProgressDialog(mContainer);
        }
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

    //endregion

    //region private methods

    private void InitialiseProgressDialog(ViewGroup container)
    {
        mProgressView = new ProgressDialog(this.getContext(), container);
    }

    //endregion
}
