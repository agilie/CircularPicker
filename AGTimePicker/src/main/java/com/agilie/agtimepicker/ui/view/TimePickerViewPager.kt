package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class TimePickerViewPager : ViewPager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var swipeEnable = false

    override fun onTouchEvent(ev: MotionEvent?) =
            if (swipeEnable) super.onTouchEvent(ev)
            else false

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
            if (swipeEnable) super.onInterceptTouchEvent(ev)
            else false
}