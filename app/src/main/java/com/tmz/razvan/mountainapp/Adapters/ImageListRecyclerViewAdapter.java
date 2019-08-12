package com.tmz.razvan.mountainapp.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmz.razvan.mountainapp.R;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

public class ImageListRecyclerViewAdapter extends RecyclerView.Adapter<ImageListRecyclerViewAdapter.ViewHolder>
{
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private boolean mContentresolver;
    // data is passed into the constructor
    public ImageListRecyclerViewAdapter(Context context, boolean contentResolver, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        mContentresolver = contentResolver;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_note_image_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filePath = mData.get(position);

        Bitmap bitmap = null;
        try {
            if(filePath.contains("content"))
            {
                Uri parsedUri = Uri.parse(filePath);
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), parsedUri);
                holder.mImageView.setImageBitmap(bitmap);
            }
            else
            {
                Picasso.get().load( Uri.parse(filePath)).placeholder(R.drawable.ic_load_image).into(holder.mImageView);
            }

        } catch (IOException e) {
            Picasso.get().load( Uri.parse(filePath)).into(holder.mImageView);
            e.printStackTrace();
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_note_grid_row_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    Uri getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
