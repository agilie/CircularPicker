package com.agilie.agtimepicker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.widget.FrameLayout
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.TimePickerPagerContainer
import com.agilie.agtimepicker.ui.view.TimePickerView

class TimePickerFrameLayout : FrameLayout, TimePickerContract.Behavior.ValueListener, TimePickerContract.Layout{
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

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        // setViewPagerPosition()
        setViewPagerContainerParams()
    }

    private fun setViewPagerPosition() {
        viewPager.let {
            this.top = (0.4 * h).toInt()
            this.left = (0.2 * w).toInt()
            this.right = ((0.8 * w).toInt())
            this.bottom = h
        }
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

        val pickerPagerAdapter = PickerPagerAdapter()
        pickerPagerAdapter.addView(TimePickerView(context))
        pickerPagerAdapter.addView(TimePickerView(context))

        pickerPagerAdapter.notifyDataSetChanged()

        viewPager?.adapter = pickerPagerAdapter


    }

    // Set TimeViewPagerContainer coordinates
    private fun setViewPagerContainerParams() {
        /*addOnLayoutChangeListener { v, left, top, right, bottom, _, _, _, _ ->
            run {
                timeViewPagerContainer.let {
                    this.left = left
                    this.right = right
                    this.bottom = bottom
                    this.top = (0.4 * h).toInt()
                }
               *//* viewPager.let {
                    this.top = (0.4 * h).toInt()
                    this.left = (0.2 * w).toInt()
                    this.right = ((0.8 * w).toInt())
                    this.bottom = h
                }*//*
            }
        }*/
        timeViewPagerContainer.let {
            this.left = left
            this.right = right
            this.bottom = bottom
            this.top = (0.4 * h).toInt()
        }


    }

}