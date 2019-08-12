package com.tmz.razvan.mountainapp.CustomUIViews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tmz.razvan.mountainapp.R;

public class ProgressDialog {

    private ProgressBar mProgressView;
    private View mProgressViewContainer;
    private Context mContext;

    public ProgressDialog(Context context, ViewGroup container) {
        mContext = context;

        View inflatedProgressView;
        LayoutInflater inflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedProgressView = inflater.inflate(R.layout.progress_dialog, container);

        mProgressView = inflatedProgressView.findViewById(R.id.login_progress);
        mProgressViewContainer = inflatedProgressView.findViewById(R.id.progress_view_container);
        //todo expose styles and fix colors

    }

    public void showProgress(final boolean show)
    {
        mProgressViewContainer.setBackgroundColor(Color.BLACK);
        mProgressViewContainer.setAlpha(0.8f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgressViewContainer.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
