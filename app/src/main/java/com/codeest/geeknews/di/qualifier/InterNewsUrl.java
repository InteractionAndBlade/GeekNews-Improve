package com.codeest.geeknews.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Qualifier;

/**
 * Created by WillLester on 2017/4/10.
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface InterNewsUrl {

}
