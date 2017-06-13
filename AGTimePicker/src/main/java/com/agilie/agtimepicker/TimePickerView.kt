package com.agilie.agtimepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.animation.PickerPath
import com.agilie.agtimepicker.animation.TrianglePath
import com.agilie.agtimepicker.timepicker.AGTimePickerImpl

class TimePickerView : View, View.OnTouchListener {
    var timePickerImpl: AGTimePickerImpl? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        timePickerImpl?.onDraw(canvas)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        timePickerImpl?.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return timePickerImpl!!.onTouchEvent(event)
    }

    private fun init() {
        timePickerImpl = AGTimePickerImpl(
                PickerPath(setPickerPaint()),
                TrianglePath(setTrianglePaint()))

        setOnTouchListener(this)
        // Load attributes
    }

    fun setGradientColors(vararg color : Int) { timePickerImpl?.colors = color }

    private fun setTrianglePaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 2f
    }

    private fun setPickerPaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 4f
    }
}