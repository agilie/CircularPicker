package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.presenter.BaseBehavior
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.animation.PickerPath

class TimePickerView : View, View.OnTouchListener, TimePickerContract.View {

    companion object {
        var MAX_PULL_UP = 35f
        var SWIPE_RADIUS_FACTOR = 0.6f
    }

    var behavior: BaseBehavior? = null
    private var w = 0
    private var h = 0

    var picker: Boolean
        set(value) {
            behavior?.picker = value
        }
        get() = behavior?.picker ?: false

    val center: PointF
        get() = behavior!!.pointCenter
    val radius: Float
        get() = behavior!!.radius

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
        behavior?.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        behavior?.onSizeChanged(w, h)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchListener?.onViewTouched(PointF(event.x, event.y), event)

        }
        return behavior!!.onTouchEvent(event)
    }

    fun onInvalidate() {
        invalidate()
    }

    private fun init() {
        this.PickerBehavior().build()
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


    inner class PickerBehavior (countOfValues: Int = 24, countOfLaps: Int = 2)
        : BaseBehavior(this@TimePickerView, PickerPath(setPickerPaint(), setTrianglePaint()), countOfValues, countOfLaps) {
        private var valuesPerLap = 1
        private var anglesPerValue = 1
        private var valueChangeListener: TimePickerContract.Behavior.ValueChangeListener? = null

        fun setValueChangeListener(valueChangeListener: TimePickerContract.Behavior.ValueChangeListener) : PickerBehavior {
            this.valueChangeListener = valueChangeListener
            return this
        }

        fun setGradient(colors: IntArray, angle: Int = 0): PickerBehavior {
            this@PickerBehavior.colors = colors
            this@PickerBehavior.gradientAngle = angle
            return this
        }

        fun setMaxValue(countOfValues: Int): PickerBehavior {
            this@PickerBehavior.countOfValues = countOfValues
            return this
        }

        fun setMaxLap(maxLap: Int): PickerBehavior {
            this@PickerBehavior.maxLapCount = maxLap
            return this
        }

        fun build() : TimePickerView{
            valuesPerLap = countOfValues / maxLapCount
            anglesPerValue = 360 / valuesPerLap
            behavior = this@PickerBehavior
            return this@TimePickerView
        }

        var prevValue = 0
        override fun calculateValue(angle: Int): Int {
            val maxAngle = 360 * maxLapCount
            val closestAngle = (0..maxAngle step anglesPerValue).firstOrNull { it > angle } ?: 360 * lapCount - 1
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