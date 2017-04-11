package com.codeest.geeknews.presenter;

import com.codeest.geeknews.app.Constants;
import com.codeest.geeknews.base.RxPresenter;
import com.codeest.geeknews.component.RxBus;
import com.codeest.geeknews.model.bean.WXItemBean;
import com.codeest.geeknews.model.event.SearchEvent;
import com.codeest.geeknews.model.http.RetrofitHelper;
import com.codeest.geeknews.model.http.response.WXHttpResponse;
import com.codeest.geeknews.presenter.contract.InterNewsContract;
import com.codeest.geeknews.util.RxUtil;
import com.codeest.geeknews.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by WillLester on 2017/4/10.
 */

public class InterNewsPresenter extends RxPresenter<InterNewsContract.View> implements InterNewsContract.Presenter {
    private static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;
    private String queryStr = null;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public InterNewsPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    @Override
    public void getInterNewsData() {
        queryStr = null;
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchInterNewsListInfo(NUM_OF_PAGE,currentPage)
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new CommonSubscriber<List<WXItemBean>>(mView) {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getMoreInterNewsData() {
        Observable<WXHttpResponse<List<WXItemBean>>> observable;
        if (queryStr != null) {
            observable = mRetrofitHelper.fetchInterNewsSearchListInfo(NUM_OF_PAGE,++currentPage,queryStr);
        } else {
            observable = mRetrofitHelper.fetchInterNewsListInfo(NUM_OF_PAGE,++currentPage);
        }
        Subscription rxSubscription = observable
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new CommonSubscriber<List<WXItemBean>>(mView, "没有更多了ヽ(≧Д≦)ノ") {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showMoreContent(wxItemBeen);
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void getSearchInterNewsData(String query) {
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchInterNewsSearchListInfo(NUM_OF_PAGE,currentPage,query)
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new CommonSubscriber<List<WXItemBean>>(mView) {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                });
        addSubscrebe(rxSubscription);
    }

    void registerEvent() {
        Subscription rxSubscription = RxBus.getDefault().toObservable(SearchEvent.class)
                .compose(RxUtil.<SearchEvent>rxSchedulerHelper())
                .filter(new Func1<SearchEvent, Boolean>() {
                    @Override
                    public Boolean call(SearchEvent searchEvent) {
                        return searchEvent.getType() == Constants.TYPE_INTER_NEWS;
                    }
                })
                .map(new Func1<SearchEvent, String>() {
                    @Override
                    public String call(SearchEvent searchEvent) {
                        return searchEvent.getQuery();
                    }
                })
                .subscribe(new CommonSubscriber<String>(mView, "搜索失败ヽ(≧Д≦)ノ") {
                    @Override
                    public void onNext(String s) {
                        queryStr = s;
                        getSearchInterNewsData(s);
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
