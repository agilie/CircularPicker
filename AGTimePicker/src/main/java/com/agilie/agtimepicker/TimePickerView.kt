package com.agilie.agtimepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.animation.HoursPickerPath
import com.agilie.agtimepicker.animation.MinutesPickerPath
import com.agilie.agtimepicker.animation.TrianglePath
import com.agilie.agtimepicker.timepicker.AGTimePickerController

class TimePickerView : View, View.OnTouchListener {
    var timePickerController: AGTimePickerController? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    val onSwipeTouchListener = OnSwipeTouchListener(context, timePickerController)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        timePickerController?.onDraw(canvas)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        timePickerController?.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        onSwipeTouchListener.onTouch(v, event)
        return timePickerController!!.onTouchEvent(event)
    }

    private fun init() {
        timePickerController = AGTimePickerController(
                HoursPickerPath(setPickerPaint()),
                MinutesPickerPath(setPickerPaint()),
                TrianglePath(setTrianglePaint()))

        setOnTouchListener(this)
        // Load attributes
    }

    fun setGradientColors(vararg color: Int) {
        timePickerController?.hoursColors = color
    }

    private fun setTrianglePaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        pathEffect = CornerPathEffect(10f)
        strokeWidth = 2f
    }

    private fun setPickerPaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 4f
    }
}