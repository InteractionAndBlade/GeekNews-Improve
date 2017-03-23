package com.codeest.geeknews.ui.zhihu.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.codeest.geeknews.R;
import com.codeest.geeknews.base.SimpleActivity;
import com.codeest.geeknews.component.RxBus;
import com.codeest.geeknews.util.DateUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by codeest on 16/8/13.
 */

public class CalendarActivity extends SimpleActivity {
    @BindView(R.id.view_calender)
    MaterialCalendarView mCalender;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    CalendarDay mDate;

    @Override
    protected int getLayout() {
        return R.layout.activity_calender;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "选择日期-查看旧日报");
        mCalender.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2015, 5, 20))
                .setMaximumDate(CalendarDay.from(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
//        mDate = CalendarDay.from(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay());
        mCalender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mDate = date;
                RxBus.getDefault().post(mDate);
                finish();
            }
        });
//        mCalender.setTileSize(88);
    }

    /*@OnClick(R.id.tv_calender_enter)
    void chooseDate() {
        RxBus.getDefault().post(mDate);
        finish();
    }*/
}
