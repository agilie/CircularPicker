package com.agilie.circularpicker.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import com.agilie.circularpicker.ui.PickerPagerAdapter
import com.agilie.circularpicker.ui.view.CircularPickerView.Companion.MAX_PULL_UP
import com.agilie.circularpicker.ui.view.CircularPickerView.Companion.SWIPE_RADIUS_FACTOR
import com.agilie.volumecontrol.pointInCircle


class CircularPickerViewPager : ViewPager {

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

    fun onAddView(view: CircularPickerView) {
        addTouchListener(view)
        this@CircularPickerViewPager.addView(view)
        pagerAdapter.addView(view)
        pagerAdapter.notifyDataSetChanged()
        adapter = pagerAdapter
    }

    fun getView(position: Int) = pagerAdapter.views[position] as CircularPickerView

    private fun addTouchListener(view: CircularPickerView) {
        view.apply {
            touchListener = object : CircularPickerView.TouchListener {
                override fun onViewTouched(pointF: PointF, event: MotionEvent?) {
                    val pickerPoint = pointInCircle(pointF, center, radius + MAX_PULL_UP) &&
                            !pointInCircle(pointF, center, (radius * SWIPE_RADIUS_FACTOR))

                    picker = pickerPoint
                    this@CircularPickerViewPager.swipeEnable = !pickerPoint
                    this@CircularPickerViewPager.onInterceptTouchEvent(event)
                }
            }
            setBackgroundColor(Color.parseColor("#00000000"))
        }
    }
}