package com.agilie.agtimepicker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.TimePickerPagerContainer

class TimePickerFrameLayout : RelativeLayout, TimePickerContract.Behavior.ValueListener, TimePickerContract.Layout {
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

    private var viewPager: ViewPager? = null

    private fun addViewPager() {
        viewPager = ViewPager(context).apply {
            setBackgroundColor(Color.RED)
            pageMargin = 15
            clipChildren = false

        }
        timeViewPagerContainer?.addView(viewPager)

        val pickerPagerAdapter = PickerPagerAdapter(context)

       /* pickerPagerAdapter.addView(TimePickerView(context).apply {
            setBackgroundColor(Color.BLACK)
            layoutParams = ViewGroup.LayoutParams(300, 300)
        })
        pickerPagerAdapter.addView(TimePickerView(context).apply {
            setBackgroundColor(Color.BLACK)
            layoutParams = ViewGroup.LayoutParams(300, 300)
        })*/

        val view = TextView(context)
        view.text = "Item "
        view.gravity = Gravity.CENTER
        view.setBackgroundColor(Color.argb(255,  50,  10,  50))
        //pickerPagerAdapter.addView(view)

        pickerPagerAdapter.notifyDataSetChanged()

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