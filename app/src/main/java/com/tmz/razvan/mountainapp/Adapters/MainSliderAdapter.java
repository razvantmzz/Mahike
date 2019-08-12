package com.tmz.razvan.mountainapp.Adapters;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    private List<String> source;

    public MainSliderAdapter(List<String> source) {
        this.source = source;
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        viewHolder.bindImageSlide(source.get(position));
    }
}