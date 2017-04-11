package com.codeest.geeknews.ui.internews.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codeest.geeknews.R;
import com.codeest.geeknews.base.BaseFragment;
import com.codeest.geeknews.model.bean.WXItemBean;
import com.codeest.geeknews.presenter.InterNewsPresenter;
import com.codeest.geeknews.presenter.contract.InterNewsContract;
import com.codeest.geeknews.ui.internews.adapter.InterNewsAdapter;
import com.codeest.geeknews.util.SnackbarUtil;
import com.codeest.geeknews.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by WillLester on 2017/4/11.
 */

public class InterNewsMainFragment extends BaseFragment<InterNewsPresenter> implements InterNewsContract.View {
    @BindView(R.id.rv_content)
    RecyclerView rvInterNewsList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    InterNewsAdapter mAdapter;
    List<WXItemBean> mList;

    boolean isLoadingMore = false;


    @Override
    public void showError(String msg) {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        SnackbarUtil.showShort(rvInterNewsList,msg);
    }

    @Override
    public void showContent(List<WXItemBean> list) {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<WXItemBean> list) {
        ivProgress.stop();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        isLoadingMore = false;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new InterNewsAdapter(mContext, mList);
        rvInterNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        rvInterNewsList.setAdapter(mAdapter);
        rvInterNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) rvInterNewsList.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = rvInterNewsList.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {  //还剩2个Item时加载更多
                    if(!isLoadingMore){
                        isLoadingMore = true;
                        mPresenter.getMoreInterNewsData();
                    }
                }
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getInterNewsData();
            }
        });
        ivProgress.start();
        mPresenter.getInterNewsData();
    }
}
