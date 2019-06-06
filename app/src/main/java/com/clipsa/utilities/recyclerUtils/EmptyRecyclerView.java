package com.clipsa.utilities.recyclerUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cowtribe.cowtribeapps.utilities.AppLogger;

/**
 * Created by ordgen on 9/7/17.
 */

public class EmptyRecyclerView extends RecyclerView {

    @Nullable
    View emptyView;

    private callback mCallback;

    public EmptyRecyclerView(Context context) { super(context); }

    public EmptyRecyclerView(Context context, AttributeSet attrs) { super(context, attrs); }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        if (emptyView != null) {
            if (getAdapter() != null && getAdapter().getItemCount() > 1) {
                AppLogger.e("EmptyRecyclerView", getAdapter().getItemCount() + "");

                emptyView.setVisibility(GONE);
            } else emptyView.setVisibility(VISIBLE);
        }
    }

    @NonNull
    final AdapterDataObserver observer = new AdapterDataObserver() {

        @Override public void onChanged() {
            super.onChanged();
            checkIfEmpty();
            if (mCallback != null) {
                mCallback.onDataChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
            if (mCallback != null) {
                mCallback.onDataChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
            if (mCallback != null) {
                mCallback.onDataChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            checkIfEmpty();
            if (mCallback != null) {
                mCallback.onDataChanged();
            }
        }

    };

    @Override public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    public interface callback {
        void onDataChanged();
    }

    public void setCallback(callback mCallback) {
        this.mCallback = mCallback;
    }
}
