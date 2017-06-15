package com.agilie.agtimepicker.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.agilie.agtimepicker.animation.PickerPath
import com.agilie.agtimepicker.animation.TrianglePath
import com.agilie.agtimepicker.timepicker.AGTimePickerController
import java.lang.Math.abs

class TimePickerView : View, View.OnTouchListener {
    var timePickerController: AGTimePickerController? = null



        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 200


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    var gestureDetector: GestureDetector? = null

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
        gestureDetector!!.onTouchEvent(event)
        return timePickerController!!.onTouchEvent(event)
    }

    private fun init() {
        timePickerController = AGTimePickerController(
                PickerPath(setPickerPaint()),
                PickerPath(setPickerPaint()),
                TrianglePath(setTrianglePaint()),
                TrianglePath(setTrianglePaint()))

        setOnTouchListener(this)
        gestureDetector = GestureDetector(context, GestureListener())
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

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                timePickerController?.setDiffX(diffX)
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            timePickerController?.onSwipeRight(diffX)
                        } else {
                            timePickerController?.onSwipeLeft(diffX)
                        }
                        result = true
                    }

                }

//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        timePickerController?.onSwipeBottom(diffY)
//                    } else {
//                        timePickerController?.onSwipeTop(diffY)
//                    }
//                    result = true
//                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }
}