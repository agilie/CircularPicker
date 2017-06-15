package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class TimePickerViewPager : ViewPager {

    var pagingEnable = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (pagingEnable) return super.onTouchEvent(ev)
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (pagingEnable) return super.onInterceptTouchEvent(ev)
        return false
    }
}