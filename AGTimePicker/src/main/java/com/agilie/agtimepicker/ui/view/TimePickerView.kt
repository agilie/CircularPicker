package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.presenter.BaseBehavior
import com.agilie.agtimepicker.presenter.BehaviorWrapper
import com.agilie.agtimepicker.presenter.TimePickerContract

class TimePickerView : View, View.OnTouchListener, TimePickerContract.View {

    private var behavior: BaseBehavior? = null

    var picker: Boolean
        set(value) {
            behavior?.picker = value
        }
        get() = behavior?.picker ?: false

    val center: PointF
        get() = behavior!!.pointCenter
    val radius: Float
        get() = behavior!!.radius
    var touchPoint = PointF()

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
        //invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        behavior?.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
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


    fun setBehavior(behaviorWrapper: BehaviorWrapper) { behavior = behaviorWrapper.generateBehavior() }

    private fun init() {
//        behavior = PickerBehavior(this,
//                PickerPath(setPickerPaint()),
//                TrianglePath(setTrianglePaint()))

        setOnTouchListener(this)
        // Load attributes
    }

//    fun setGradientColors(vararg color: Int) {
//        behavior?.hoursColors = color
//    }


    interface TouchListener {
        fun onViewTouched(pointF: PointF, event: MotionEvent?)
    }
}