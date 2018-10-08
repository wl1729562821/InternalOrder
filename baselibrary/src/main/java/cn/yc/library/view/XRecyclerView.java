package cn.yc.library.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class XRecyclerView extends RecyclerView {

    private static final int TYPE_FOOTER = 10001;
    private LinearLayoutManager mLayoutManager=null;
    private WrapAdapter mWrapAdapter=null;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private boolean mFooterView=false;
    private boolean mFooterRefresh=false;

    private LoadingListener mLoadingListener;
    private SwipeRefreshLayout mRefresh=null;

    public void setRefreshLayout(SwipeRefreshLayout refreshLayout){
        mRefresh=refreshLayout;
    }

    public XRecyclerView(Context context) {
        super(context);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(getLayoutManager() instanceof LinearLayoutManager){
            mLayoutManager=(LinearLayoutManager) getLayoutManager();
        }
        boolean isrefresh=false;
        if(mRefresh!=null){
            isrefresh=mRefresh.isRefreshing();
        }
        Log.e("Recycler","onScrollStateChanged"+getAdapter().getItemCount()+";%10="+getAdapter().getItemCount()%10+";refresh="+isrefresh);
        if(!isrefresh&&getAdapter().getItemCount()>0 && getAdapter().getItemCount()%10==0 &&
                state == RecyclerView.SCROLL_STATE_IDLE&&mLayoutManager!=null && mWrapAdapter!=null){
            int endCompletelyPosition=mLayoutManager.findLastCompletelyVisibleItemPosition();
            if (endCompletelyPosition == mWrapAdapter.getItemCount()-1 && !mFooterRefresh && mLoadingListener!=null){
                if(mRefresh!=null){
                    mRefresh.setEnabled(false);
                }
                mFooterView=true;
                mFooterRefresh=true;
                //mWrapAdapter.refreshFooter();
                mLoadingListener.onLoadMore();
            }
        }
    }

    public boolean isLoadMore(){
        return mFooterRefresh;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if(layout instanceof LinearLayoutManager){
            mLayoutManager=(LinearLayoutManager)layout;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter=new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
                if (mWrapAdapter.getItemCount() == 0) {
                    XRecyclerView.this.setVisibility(View.GONE);
                } else {
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        public RecyclerView.Adapter getOriginalAdapter(){
            return this.adapter;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.e("adapter","onCreateViewHolder type="+viewType);
            /*if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_footer,parent,false));
            }*/
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (adapter != null) {
                adapter.onBindViewHolder(holder,position);
            }
        }

        // some times we need to override this
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,List<Object> payloads) {
            Log.e("Adapter","onBind "+mFooterView);
            if (adapter != null) {
                adapter.onBindViewHolder(holder,position,payloads);
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return adapter.getItemCount();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
           return adapter.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            if (adapter!=null) {
                return adapter.getItemId(position);
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void finishLoadMore(){
        if(mRefresh!=null){
            mRefresh.setEnabled(true);
        }
        mFooterRefresh=false;
        mFooterView=false;
        if(mWrapAdapter!=null){
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {
        void onLoadMore();
    }
}
