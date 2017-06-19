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

    var behavior: BaseBehavior? = null
    private var w = 0
    private var h = 0


    /*var picker: Boolean
        set(value) {
            behavior?.picker = value
        }
        get() = behavior?.picker ?: false*/

    val center: PointF
        get() = behavior!!.pointCenter
    val radius: Float
        get() = behavior!!.radius
    var touchPoint = PointF()

    //var touchListener: TouchListener? = null

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
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        behavior?.onSizeChanged(w, h)

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        /*when (event.action) {
            //MotionEvent.ACTION_DOWN -> touchListener?.onViewTouched(PointF(event.x, event.y), event)

        }*/
        behavior?.onTouchEvent(event)
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
        behavior = BehaviorWrapper(this, object :TimePickerContract.Behavior.BehaviorConstructor {
            override fun onValueCalculated(value: Int) {
            }

            override fun countOfLaps() = 0

            override fun countOfValues() = 0

        }).generateBehavior()
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