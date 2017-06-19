package com.agilie.agtimepicker.presenter

import android.graphics.*
import android.view.MotionEvent
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import com.agilie.volumecontrol.pointInCircle
import java.lang.Math.max
import java.lang.Math.min


abstract class BaseBehavior(val view: TimePickerView,
                            val pickerPath: PickerPath,
                            //val trianglePath: TrianglePath,
                            var colors: IntArray = intArrayOf(
                                    Color.parseColor("#0080ff"),
                                    Color.parseColor("#53FFFF"))) : TimePickerContract.Behavior {

    private val MAX_PULL_UP = 45f
    private var previousTouchPoint = PointF()
    var angle = 0f
    var picker = false
    val SWIPE_RADIUS_FACTOR = 0.6f
    //var angle = 0f
    //var picker = true
    //var valueListener: TimePickerContract.Behavior.ValueListener? = null

    val pointCenter: PointF
        get() = pickerPath.center
    val radius: Float
        get() = pickerPath.radius

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
        //trianglePath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        val center = PointF(width / 2f, height / 2f)
        val radius = max(min(width, height),0) / 3f
        updatePaint(center, radius)
        drawShapes(center, radius)
    }

    fun updatePaint(center: PointF, radius: Float) {
        val startPoint = getPointOnBorderLineOfCircle(center, radius, 180)
        val endPoint = getPointOnBorderLineOfCircle(center, radius, 0)
        pickerPath.pickerPaint.apply {
            shader = LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, colors,
                    null,
                    Shader.TileMode.CLAMP)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                onActionUp()
                view.onInvalidate()
            }
        }
        return true
    }

    private fun drawShapes(center: PointF, radius: Float) {
        pickerPath.center = center
        //trianglePath.center = center

        pickerPath.radius = radius
        //trianglePath.radius = radius

        previousTouchPoint = center

        pickerPath.createPickerPath()
        //trianglePath.createTrianglePath()
    }


    private fun onActionDown(pointF: PointF) {
        //val pointInCircle = pointInCircle(pointF, pickerPath.center, pickerPath.radius)
        val action = pointInActionArea(pointF)
        previousTouchPoint = pointF
        pickerPath.lockMove = !action
        //trianglePath.lockMove = !action
    }

    var currentLap = 0
    fun pointInActionArea(pointF: PointF) = pointInCircle(pointF, pickerPath.center, pickerPath.radius) &&
            !pointInCircle(pointF, pickerPath.center, (pickerPath.radius * SWIPE_RADIUS_FACTOR))

    private fun onActionMove(pointF: PointF) {
        previousTouchPoint = pointF
        //if (picker) {
        val angle = calculateAngleWithTwoVectors(pointF, pickerPath.center)
        val distance = distance(pointF, pickerPath.center) - pickerPath.radius
        //TODO clean up code
        val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))

        pickerPath.onActionMove(angle, pullUp)

        //if (pullUp != 0f) {
       // trianglePath.onActionMove(angle, pullUp)
        //}
        //valueListener?.valueListener(calculateValue(angle = angle.toInt()))
        //}
        value(calculateValue(currentLap, angle.toInt()))
    }

    private fun onActionUp() {
        pickerPath.lockMove = true
        //trianglePath.lockMove = true
        pickerPath.onActionUp()
        //trianglePath.onActionUp()
    }

    enum class TouchState { SWIPE, ROTATE }
}
