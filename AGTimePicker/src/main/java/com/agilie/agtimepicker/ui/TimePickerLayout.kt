package com.agilie.agtimepicker.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.PickerPagerTransformer
import com.agilie.agtimepicker.ui.view.TimePickerPagerContainer
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.agtimepicker.ui.view.TimePickerViewPager


class TimePickerLayout : RelativeLayout, TimePickerContract.Layout {

    var pickerPagerAdapter: PickerPagerAdapter? = null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                if (!
//                pointInActionArea()
//                        ) {
//                    touchState = BaseBehavior.TouchState.SWIPE
//                } else {
//                    touchState = BaseBehavior.TouchState.ROTATE
//                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    var timeViewPagerContainer: TimePickerPagerContainer? = null
    var onLayout = true
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
        if (onLayout) {
            setViewPagerParams()
            setViewPagerContainerParams()
            onLayout = false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        onLayout = true
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    private fun init(attrs: AttributeSet?) {
        timeViewPagerContainer = TimePickerPagerContainer(context)
        //add view pager
        addView(timeViewPagerContainer)
        addViewPager()
    }

    private var viewPager: TimePickerViewPager? = null

    //ViewPager params
    private fun addViewPager() {
        viewPager = TimePickerViewPager(context).apply {

            pageMargin = 15
            clipChildren = false
            setPageTransformer(false, PickerPagerTransformer(context))
        }

        timeViewPagerContainer?.addView(viewPager)

        //add view in adapter
        pickerPagerAdapter = PickerPagerAdapter().apply {

            addView(TimePickerView(context).apply {
                setBackgroundColor(Color.parseColor("#00000000"))
            })

            addView(TimePickerView(context).apply {
                setBackgroundColor(Color.parseColor("#00000000"))
            })
        }

        viewPager?.adapter = pickerPagerAdapter
        pickerPagerAdapter?.notifyDataSetChanged()

    }

    // Set TimeViewPagerContainer coordinates
    private fun setViewPagerContainerParams() {
        val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (h * 0.8).toInt())
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.addRule(RelativeLayout.ALIGN_PARENT_START)
        timeViewPagerContainer?.layoutParams = params

    }

    private fun setViewPagerParams() {
        val params = RelativeLayout.LayoutParams((w * 0.7).toInt(), LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        viewPager?.layoutParams = params

    }
}