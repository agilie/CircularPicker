package com.agilie.agtimepicker

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

class PickerFrameLayout : FrameLayout, ViewPager.OnPageChangeListener {

    private var mPager: ViewPager? = null
    internal var mNeedsRedraw = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }


    override fun onPageScrollStateChanged(state: Int) {
        mNeedsRedraw = state != ViewPager.SCROLL_STATE_IDLE

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mNeedsRedraw) invalidate()
    }

    override fun onPageSelected(position: Int) {
    }


    override fun onFinishInflate() {
        try {
            mPager = getChildAt(0) as ViewPager
            mPager?.setOnPageChangeListener(this)
        } catch (e: Exception) {
            throw IllegalStateException("The root child of PagerContainer must be a ViewPager")
        }
    }

    private val mCenter = PointF()
    private val mInitialTouch = PointF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mCenter.x = w / 2f
        mCenter.y = h / 2f
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //We capture any touches not already handled by the ViewPager
        // to implement scrolling from a touch outside the pager bounds.
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialTouch.x = ev.x
                mInitialTouch.y = ev.y
                ev.offsetLocation((mCenter.x - mInitialTouch.x), (mCenter.y - mInitialTouch.y))
            }
            else -> ev.offsetLocation((mCenter.x - mInitialTouch.x), (mCenter.y - mInitialTouch.y))
        }

        return mPager!!.dispatchTouchEvent(ev)
    }

    private fun init(attrs: AttributeSet?) {
        val pager = ViewPager(context)

        this.addView(pager)

        val layoutParams = FrameLayout.LayoutParams(150,150)
        layoutParams.height = 100
        layoutParams.width = 150

        pager.layoutParams = layoutParams

        val adapter = MyPagerAdapter()
        pager.adapter = adapter
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.offscreenPageLimit = adapter.count
        //A little space between pages
        pager.pageMargin = 15

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.clipChildren = false


        clipChildren = false
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
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

        override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
            container.removeView(ob as View)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun isViewFromObject(view: View, ob: Any): Boolean {
            return view === ob
        }

     /*   override fun getPageWidth(position: Int): Float {
            return 0.5f
        }*/
    }


}