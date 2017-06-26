package com.agilie.circularpicker.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.agilie.circularpicker.presenter.AGCircularPickerContract
import com.agilie.circularpicker.presenter.BaseBehavior
import com.agilie.circularpicker.ui.animation.CircularPickerPath
import com.agilie.volumecontrol.closestValue

class CircularPickerView : View, View.OnTouchListener, AGCircularPickerContract.View {
    var behavior: BaseBehavior = PickerBehavior()

    companion object {
        var MAX_PULL_UP = 35f
        var SWIPE_RADIUS_FACTOR = 0.6f
        var CIRCULAR_PICKER_RATIO = 2.5f
    }

    var centeredTextSize: Float
        get() = behavior.centeredTextSize
        set(value) {
            behavior.centeredTextSize = value
        }

    var centeredTextColor: Int
        get() = behavior.centeredTextColor
        set(value) {
            behavior.centeredTextColor = value
        }

    var centeredTypeFace: Typeface
        get() = behavior.centeredTypeface
        set(value) {
            behavior.centeredTypeface = value
        }

    var maxValue: Int
        get() = behavior.countOfValues
        set(value) {
            behavior.countOfValues = value
            behavior.build()
        }

    var maxLapCount: Int
        get() = behavior.maxLapCount
        set(value) {
            behavior.maxLapCount = value
            behavior.build()
        }

    var color: Int
        get() = behavior.colors[0]
        set(value) {
            behavior.colors = intArrayOf(value, value)
            behavior.updatePaint(center, radius)
        }

    var colors: IntArray
        get() = behavior.colors
        set(value) {
            behavior.colors = value
            behavior.updatePaint(center, radius)
        }

    var gradientAngle: Int
        get() = behavior.gradientAngle
        set(value) {
            behavior.gradientAngle = value
            behavior.updatePaint(center, radius)
        }

    var centeredText: String
        get() = behavior.centeredText
        set(value) {
            behavior.centeredText = value
        }


    var valueChangeListener: AGCircularPickerContract.Behavior.ValueChangeListener?
        get() = behavior.valueChangeListener
        set(value) {
            behavior.valueChangeListener = value
        }
    private var w = 0
    private var h = 0

    var picker: Boolean
        set(value) {
            behavior.picker = value
        }
        get() = behavior.picker

    val center: PointF
        get() = behavior.pointCenter
    val radius: Float
        get() = behavior.radius

    var touchListener: TouchListener? = null

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
        behavior.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        behavior.onSizeChanged(w, h)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchListener?.onViewTouched(PointF(event.x, event.y), event)

        }
        return behavior.onTouchEvent(event)
    }

    fun onInvalidate() {
        invalidate()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    interface TouchListener {
        fun onViewTouched(pointF: PointF, event: MotionEvent?)
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

    inner class PickerBehavior : BaseBehavior(this@CircularPickerView, CircularPickerPath(setPickerPaint(), setTrianglePaint())) {
        private var valuesPerLap = 1
        private var anglesPerValue = 1

        override fun build() {
            valuesPerLap = countOfValues / maxLapCount
            anglesPerValue = 360 / valuesPerLap
        }

        var prevValue = 0
        var angle = 0
        override fun calculateValue(angle: Int): Int {
            this.angle = angle

            val closestAngle = closestValue(angle, anglesPerValue)

            val value = (countOfValues * closestAngle) / (360 * maxLapCount) - 1
            return value
        }

        override fun value(value: Int) {
            if (prevValue == value) return
            if (value < 0) valueChangeListener?.onValueChanged(prevValue)
            else {
                prevValue = value
                valueChangeListener?.onValueChanged(prevValue)
            }
        }

    }
}