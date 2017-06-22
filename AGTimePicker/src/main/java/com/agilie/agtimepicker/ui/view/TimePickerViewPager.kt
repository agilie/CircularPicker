package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import com.agilie.agtimepicker.ui.PickerPagerAdapter
import com.agilie.agtimepicker.ui.view.TimePickerView.Companion.MAX_PULL_UP
import com.agilie.agtimepicker.ui.view.TimePickerView.Companion.SWIPE_RADIUS_FACTOR
import com.agilie.volumecontrol.pointInCircle


class TimePickerViewPager : ViewPager {

    private var pagerAdapter = PickerPagerAdapter()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var swipeEnable = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeEnable) return super.onTouchEvent(ev)
        else return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeEnable) return super.onInterceptTouchEvent(ev)
        else return false
    }

    fun onAddView(view: TimePickerView) {
        addTouchListener(view)
        this@TimePickerViewPager.addView(view)
        pagerAdapter.addView(view)
        pagerAdapter.notifyDataSetChanged()
        adapter = pagerAdapter
    }

    fun getView(position: Int) = pagerAdapter.views[position] as TimePickerView

    private fun addTouchListener(view: TimePickerView) {
        view.apply {
            touchListener = object : TimePickerView.TouchListener {
                override fun onViewTouched(pointF: PointF, event: MotionEvent?) {
                    val pickerPoint = pointInCircle(pointF, center, radius + MAX_PULL_UP) &&
                            !pointInCircle(pointF, center, (radius * SWIPE_RADIUS_FACTOR))

                    picker = pickerPoint
                    this@TimePickerViewPager.swipeEnable = !pickerPoint
                    this@TimePickerViewPager.onInterceptTouchEvent(event)
                }
            }
            setBackgroundColor(Color.parseColor("#00000000"))
        }
    }
}