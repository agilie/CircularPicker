package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView


class TimePickerPagerContainer : FrameLayout, ViewPager.OnPageChangeListener {


    private var mPager: TimePickerViewPager? = null
    var mNeedsRedraw = false

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        clipChildren = false
        mPager = TimePickerViewPager(context)
        mPager?.adapter = MyPagerAdapter()
        addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            var layPar = LayoutParams(layoutParams)
            mPager?.pageMargin = 15
            mPager?.layoutParams = LayoutParams(layPar)
        }

        addView(mPager)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onFinishInflate() {
        try {
            mPager = getChildAt(0) as TimePickerViewPager
            mPager!!.setOnPageChangeListener(this)
        } catch (e: Exception) {
            throw IllegalStateException("The root child of PagerContainer must be a ViewPager")
        }

    }

    fun getViewPager(): ViewPager {
        return mPager!!
    }

    private val mCenter = Point()
    private val mInitialTouch = Point()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mCenter.x = w / 2
        mCenter.y = h / 2
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialTouch.x = ev.x.toInt()
                mInitialTouch.y = ev.y.toInt()
                ev.offsetLocation(mCenter.x - mInitialTouch.x.toFloat(), mCenter.y - mInitialTouch.y.toFloat())
            }
            else -> ev.offsetLocation(mCenter.x - mInitialTouch.x.toFloat(), mCenter.y - mInitialTouch.y.toFloat())
        }

        return mPager!!.dispatchTouchEvent(ev)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mNeedsRedraw) invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {
        mNeedsRedraw = state != ViewPager.SCROLL_STATE_IDLE
    }


    private inner class MyPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = TextView(context)
            view.text = "Item " + position
            view.gravity = Gravity.CENTER
            view.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50))

            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }
}