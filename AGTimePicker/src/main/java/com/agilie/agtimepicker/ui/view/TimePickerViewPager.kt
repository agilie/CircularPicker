package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.agilie.volumecontrol.pointInCircle


class TimePickerViewPager : ViewPager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var swipeEnable = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeEnable) return super.onTouchEvent(ev)
        else return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) : Boolean{
        if (swipeEnable) return super.onInterceptTouchEvent(ev)
        else return false
    }
}