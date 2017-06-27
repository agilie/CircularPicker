package com.agilie.circularpicker.ui.view

import android.content.Context
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class CircularPickerPagerContainer : RelativeLayout, ViewPager.OnPageChangeListener {

    private var pager: CircularPickerViewPager? = null
    private var needsRedraw = false

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

    override fun onPageSelected(position: Int) {
    }

    override fun onFinishInflate() {
        pager = getChildAt(0) as CircularPickerViewPager
        pager?.setOnPageChangeListener(this)
    }

    private val center = PointF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.x = w / 2f
        center.y = h / 2f
    }

    private fun init(attrs: AttributeSet?) {
        clipChildren = false
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

}