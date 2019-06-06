package com.clipsa.utilities.recyclerUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cowtribe.cowtribeapps.R;
import com.cowtribe.cowtribeapps.utilities.AppConstants;
import com.cowtribe.cowtribeapps.utilities.AppLogger;


/**
 * Any class that extend this class
 * should implement getCustomItemViewType
 * should call super.onBindViewHolder(holder, position); inside onBindViewHolder
 */


public abstract class LoadMoreRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T>  {
    private static final int TYPE_PROGRESS = 0xFFFF;
    private RetryLoadMoreListener mRetryLoadMoreListener;
    private boolean mOnLoadMoreFailed;
    private boolean mIsReachEnd;


    protected LoadMoreRecyclerViewAdapter(@NonNull Context context,
                                          ItemClickListener itemClickListener,
                                          @NonNull RetryLoadMoreListener retryLoadMoreListener) {
        super(context, itemClickListener);
        mRetryLoadMoreListener = retryLoadMoreListener;

    }

    protected LoadMoreRecyclerViewAdapter(@NonNull Context context,
                                          ItemClickListener itemClickListener) {
        super(context, itemClickListener);
        mRetryLoadMoreListener = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                View view = mInflater.inflate(R.layout.item_recyclerview_bottom, parent, false);
                return new BottomViewHolder(view, mRetryLoadMoreListener);
        }
        throw new RuntimeException("LoadMoreRecyclerViewAdapter: ViewHolder = null");
    }


    @Override
    public int getItemViewType(int position) {
        if (position == bottomItemPosition()) {
            return TYPE_PROGRESS;
        }
        return getCustomItemViewType(position);
    }

    private int bottomItemPosition() {
        return getItemCount() - 1;
    }

    protected abstract int getCustomItemViewType(int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BottomViewHolder) {


            ((BottomViewHolder) holder).mTvNoMoreItem.setVisibility(
                    mIsReachEnd ? View.VISIBLE : View.GONE);

            ((BottomViewHolder) holder).mProgressBar.setVisibility(mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.GONE : View.GONE);
            ((BottomViewHolder) holder).layoutRetry.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.VISIBLE : View.GONE);

            AppLogger.e("LoadMoreRecycler", "RETRY LOAD MORE IS " + mRetryLoadMoreListener);

        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1; // +1 for progress
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView mTvNoMoreItem;
        private Button mBtnRetry;
        private View layoutRetry;
        private RetryLoadMoreListener mRetryLoadMoreListener;
        private RelativeLayout mLoadMoreLayout;

        BottomViewHolder(View itemView, RetryLoadMoreListener retryLoadMoreListener) {
            super(itemView);
            mRetryLoadMoreListener = retryLoadMoreListener;
            mLoadMoreLayout = itemView.findViewById(R.id.load_more_layout);
            mProgressBar = itemView.findViewById(R.id.progress);


            layoutRetry = itemView.findViewById(R.id.layout_retry);
            mBtnRetry = itemView.findViewById(R.id.button_retry);
            mTvNoMoreItem = itemView.findViewById(R.id.text_no_more_item);

            layoutRetry.setVisibility(View.GONE); // gone layout retry as default
            mTvNoMoreItem.setVisibility(View.GONE); // gone text view no more item as default

            mBtnRetry.setOnClickListener(v -> mRetryLoadMoreListener.onRetryLoadMore());
        }
    }

    /**
     * It help visible progress when load more
     */
    public void startLoadMore() {
        mOnLoadMoreFailed = false;
        notifyDataSetChanged();
    }

    /**
     * It help visible layout retry when load more failed
     */
    public void onLoadMoreFailed() {
        mOnLoadMoreFailed = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEnd() {
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }

    public interface RetryLoadMoreListener {
        void onRetryLoadMore();
    }




    public int getIcon(String name){

        switch (name){

            case AppConstants.SUB_CATEGORY_BIRD_FEED:

                return R.drawable.bird_feed;

            case AppConstants.SUB_CATEGORY_CATTLE_DEWORMERS:

                return R.drawable.beef;

            case AppConstants.SUB_CATEGORY_CATTLE_VACCINES:

                return R.drawable.ic_cattle_syringe;

            case AppConstants.SUB_CATEGORY_GOAST_AND_SHEEP_DEWORMERS:

                return R.drawable.goat;

            case AppConstants.SUB_CATEGORY_GOAST_AND_SHEEP_VACCINES:

                return R.drawable.ic_vaccine;


            case AppConstants.SUB_CATEGORY_POULTRY_DEWORMERS:

                return 0;

            case AppConstants.SUB_CATEGORY_POULTRY_VACCINES:

                return R.drawable.rooster;

            default: return 0;


        }







    }
}
