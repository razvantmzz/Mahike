package com.tmz.razvan.mountainapp.CustomUIViews;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomHeaderTV extends android.support.v7.widget.AppCompatTextView {

    public CustomHeaderTV(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    public CustomHeaderTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    public CustomHeaderTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    public void setTypeface(Typeface tf) {
//        super.setTypeface(tf);
        super.setTypeface(tf, Typeface.BOLD);
    }
}