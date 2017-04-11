package com.codeest.geeknews.model.http.api;

import com.codeest.geeknews.model.bean.WXItemBean;
import com.codeest.geeknews.model.http.response.WXHttpResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by WillLester on 2017/4/10.
 */

public interface InterNewsApis {
    String HOST = "http://api.tianapi.com/";

    /**
     * 国际新闻列表
     */
    @GET("world")
    Observable<WXHttpResponse<List<WXItemBean>>> getInterNewsSearch(@Query("key") String key, @Query("num") int num, @Query("page") int page);
    /**
     * 国际新闻列表搜索
     */
    @GET("world")
    Observable<WXHttpResponse<List<WXItemBean>>> getInterNewsSearch(@Query("key") String key, @Query("num") int num, @Query("page") int page, @Query("word") String word);
}
