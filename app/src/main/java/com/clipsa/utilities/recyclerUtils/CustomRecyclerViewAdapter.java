package com.clipsa.utilities.recyclerUtils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cowtribe.cowtribeapps.data.db.entity.Item;
import com.cowtribe.cowtribeapps.utilities.Callbacks;

import static com.cowtribe.cowtribeapps.ui.base.BaseActivity.ORDER;


/**
 * Any class that extend this class
 * should implement getCustomItemViewType
 * should call super.onBindViewHolder(holder, position); inside onBindViewHolder
 */


public abstract class CustomRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T>  {

    T mRecentlyDeletedItem;
    int mRecentlyDeletedItemPosition;

    public static Callbacks.ShowSnackBarListener showSnackBarListener;


    protected CustomRecyclerViewAdapter(@NonNull Context context,
                                        ItemClickListener itemClickListener) {
        super(context, itemClickListener);
     }



    @Override
    public int getItemViewType(int position) {
        return getCustomItemViewType(position);
    }



    protected abstract int getCustomItemViewType(int position);


    @Override
    public int getItemCount() {
        return mDataList.size(); // +1 for progress
    }




    public void deleteItem(int position) {

        ORDER.getItems().remove(position);


        mRecentlyDeletedItem = mDataList.get(position);
        mRecentlyDeletedItemPosition = position;
        mDataList.remove(position);
        notifyItemRemoved(position);

        if(showSnackBarListener != null)
            showSnackBarListener.showUndoSnackBar();




    }




    public void undoDelete() {
        mDataList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);

        ORDER.getItems().add((Item) mRecentlyDeletedItem);

    }


    public static void setShowUndoSnackBarListener(Callbacks.ShowSnackBarListener listener){
        showSnackBarListener = listener;
    }


    public static void clearPrintListener(){
        showSnackBarListener = null;

    }


}
