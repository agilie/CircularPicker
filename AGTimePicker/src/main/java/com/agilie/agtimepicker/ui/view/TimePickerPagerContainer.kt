package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

/**Action Flow
 * 1) Set correct position for view pager
 * 2) Set correct
 * */

class TimePickerPagerContainer : RelativeLayout, ViewPager.OnPageChangeListener {


    private var pager: TimePickerViewPager? = null
    private var needsRedraw = false
    //Set adapter
    //var adapter: PickerPagerAdapter? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }


    override fun onPageScrollStateChanged(state: Int) {
        needsRedraw = state != ViewPager.SCROLL_STATE_IDLE

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
         if (needsRedraw) invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onPageSelected(position: Int) {
    }


    override fun onFinishInflate() {
        try {
            pager = getChildAt(0) as TimePickerViewPager
            pager?.setOnPageChangeListener(this)
        } catch (e: Exception) {
            throw IllegalStateException("The root child of PagerContainer must be a ViewPager")
        }
    }

    private val center = PointF()
    private val touch = PointF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.x = w / 2f
        center.y = h / 2f
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //We capture any touches not already handled by the ViewPager
        // to implement scrolling from a touch outside the pager bounds.
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touch.x = ev.x
                touch.y = ev.y
                ev.offsetLocation((center.x - touch.x), (center.y - touch.y))
            }
            else -> ev.offsetLocation((center.x - touch.x), (center.y - touch.y))
        }

        return getChildAt(0).dispatchTouchEvent(ev)
    }

    private fun init(attrs: AttributeSet?) {
        clipChildren = false
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

}