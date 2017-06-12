package com.agilie.agtimepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.animation.PickerPath
import com.agilie.agtimepicker.timepicker.AGTimePickerImpl

/**
 * TODO: document your custom view class.
 */
class TimePickerView : View, View.OnTouchListener {

    var timePickerImpl: AGTimePickerImpl? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        timePickerImpl?.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        timePickerImpl?.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        timePickerImpl?.onTouchEvent(event)
        return true
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        timePickerImpl = AGTimePickerImpl(
                PickerPath(setPickerPaint()))
        // Load attributes
    }

    private fun setPickerPaint() = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 2f
    }
}
