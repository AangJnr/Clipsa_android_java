package com.clipsa.utilities.recyclerUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;

/**
 * Created by ordgen on 4/7/18.
 */

public abstract class BaseRecyclerViewAdapter<T>
    extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected LayoutInflater mInflater;
    protected List<T> mDataList;
    protected  IImageLoader imageLoader;
    protected ItemClickListener mItemClickListener;
    Context mContext;

     BaseRecyclerViewAdapter(@NonNull Context context,
                             ItemClickListener itemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
        mDataList = new ArrayList<>();
         imageLoader = new PicassoLoader();
    }

    public void add(List<T> itemList) {
        mDataList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void set(List<T> dataList) {
        List<T> clone = new ArrayList<>(dataList);
        mDataList.clear();
        mDataList.addAll(clone);
        notifyDataSetChanged();

    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onLongItemClick(View view, int position);
    }
}
