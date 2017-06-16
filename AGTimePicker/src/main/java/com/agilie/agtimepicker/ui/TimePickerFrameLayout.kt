package com.agilie.agtimepicker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.TimePickerPagerContainer
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.agtimepicker.ui.view.TimePickerViewPager
import com.agilie.volumecontrol.pointInCircle

class TimePickerFrameLayout : RelativeLayout, TimePickerContract.Behavior.ValueListener, TimePickerContract.Layout {

    val SWIPE_RADIUS_FACTOR = 0.6f

    var pickerPagerAdapter: PickerPagerAdapter? = null
//    var touchState = BaseBehavior.TouchState.ROTATE

//    val isSwipe: Boolean
//        get() = touchState == BaseBehavior.TouchState.SWIPE

//    fun swipePoint() {
//        val view = (pickerPagerAdapter?.getView((viewPager?.currentItem ?: 0)) as TimePickerView)
//        view.touchListener = object : TimePickerView.TouchListener {
//            override fun onViewTouched(pointF: PointF) {
//                val pickerPoint = pointInCircle(view.touchPoint, view.center, view.radius)
//                        &&
//                        !pointInCircle(view.touchPoint, view.center, (view.radius * SWIPE_RADIUS_FACTOR))
//
//                Log.d("swipeTesqwe", "swipePoint == $pickerPoint \n" +
//                        "view.radius == ${view.radius} \n" +
//                        "view.center.x == ${view.center.x} \n" +
//                        "view.center.y == ${view.center.y} \n" +
//                        "touchPoint.x == ${view.touchPoint.x} \n" +
//                        "touchPoint.y == ${view.touchPoint.y}")
//                view.picker = pickerPoint
//                viewPager?.swipeEnable = !pickerPoint
//            }
//        }
//    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                if (!
//                swipePoint()
//                        ) {
//                    touchState = BaseBehavior.TouchState.SWIPE
//                } else {
//                    touchState = BaseBehavior.TouchState.ROTATE
//                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun valueListener(value: Float) {
    }

    var timeViewPagerContainer: TimePickerPagerContainer? = null

    private var w = 0
    private var h = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        setViewPagerContainerParams()
        setViewPagerParams()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

    }


    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)

    }

    private fun init(attrs: AttributeSet?) {
        timeViewPagerContainer = TimePickerPagerContainer(context)
        timeViewPagerContainer?.setBackgroundColor(Color.BLUE)
        //add view pager
        addView(timeViewPagerContainer)
        addViewPager()

    }

    private var viewPager: TimePickerViewPager? = null

    private fun addViewPager() {
        viewPager = TimePickerViewPager(context).apply {
            setBackgroundColor(Color.RED)
            pageMargin = 15
            clipChildren = false

        }
        timeViewPagerContainer?.addView(viewPager)
        pickerPagerAdapter = PickerPagerAdapter()
        for (x in 0..2) {
            pickerPagerAdapter?.addView(TimePickerView(context).apply {
                setBackgroundColor(Color.BLACK)
                layoutParams = ViewGroup.LayoutParams(300, 300)
                touchListener = object : TimePickerView.TouchListener {
                    override fun onViewTouched(pointF: PointF) {
                        val pickerPoint = pointInCircle(pointF, center, radius) &&
                                !pointInCircle(pointF, center, (radius * SWIPE_RADIUS_FACTOR))
//                        Log.d("swipeTesqwe", "1pickerPoint == $pickerPoint ${radius * SWIPE_RADIUS_FACTOR}\n" +
//                                "outher ${pointInCircle(pointF, center, radius)} \n" +
//                                "inner ${pointInCircle(pointF, center, (radius * SWIPE_RADIUS_FACTOR))} \n " +
//                                "view.radius == ${radius} \n" +
//                                "view.center.x == ${center.x} \n" +
//                                "view.center.y == ${center.y} \n" +
//                                "touchPoint.x == ${pointF.x} \n" +
//                                "touchPoint.y == ${pointF.y}")
                        picker = pickerPoint
                        viewPager?.swipeEnable = !pickerPoint
                    }
                }
            })
        }
        pickerPagerAdapter?.notifyDataSetChanged()
        viewPager?.adapter = pickerPagerAdapter

    }

    // Set TimeViewPagerContainer coordinates
    private fun setViewPagerContainerParams() {
        val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (h * 0.8).toInt())
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.addRule(RelativeLayout.ALIGN_PARENT_START)
        timeViewPagerContainer?.layoutParams = params

    }

    private fun setViewPagerParams() {
        val params = ViewPager.LayoutParams()
        params.width = 300
        params.height = LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER_HORIZONTAL
        //  viewPager?.layoutParams = params

    }


}