package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.presenter.HourPickerBehavior
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.animation.TrianglePath

class TimePickerView : View, View.OnTouchListener, TimePickerContract.View { // TODO DELETE

    var behavior: TimePickerContract.Behavior? = null

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
        behavior?.onDraw(canvas)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        behavior?.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return behavior!!.onTouchEvent(event)
    }

    private fun init() {
        behavior = HourPickerBehavior(
                PickerPath(setPickerPaint()),
                TrianglePath(setTrianglePaint()))

        setOnTouchListener(this)
        // Load attributes
    }

//    fun setGradientColors(vararg color: Int) {
//        behavior?.hoursColors = color
//    }


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