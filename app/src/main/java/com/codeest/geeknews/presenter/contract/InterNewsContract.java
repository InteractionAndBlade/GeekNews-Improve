package com.codeest.geeknews.presenter.contract;

import com.codeest.geeknews.base.BasePresenter;
import com.codeest.geeknews.base.BaseView;
import com.codeest.geeknews.model.bean.WXItemBean;

import java.util.List;

/**
 * Created by WillLester on 2017/4/10.
 */

public interface InterNewsContract {
    interface View extends BaseView {
        void showContent(List<WXItemBean> mList);

        void showMoreContent(List<WXItemBean> mList);
    }

    interface Presenter extends BasePresenter<InterNewsContract.View> {

        void getInterNewsData();

        void getMoreInterNewsData();
    }
}
