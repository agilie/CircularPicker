package com.agilie.agtimepicker.presenter

import android.graphics.*
import android.view.MotionEvent
import com.agilie.agtimepicker.presenter.timepicker.AGTimePicker
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.animation.TrianglePath
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import com.agilie.volumecontrol.pointInCircle


abstract class BaseBehavior(val hoursPickerPath: PickerPath,
                   val hoursTrianglePath: TrianglePath,
                   var colors: IntArray = intArrayOf(
                           Color.parseColor("#0080ff"),
                           Color.parseColor("#53FFFF"))) : TimePickerContract.Behavior{

    private val MAX_PULL_UP = 65f
    val SWIPE_RADIUS_FACTOR = 0.6f
    private var previousTouchPoint = PointF()
    var touchState = TouchState.ROTATE


    var angle = 0f
    var valueListener: TimePickerContract.Behavior.ValueListener? = null
    fun isSwipe() = touchState == TouchState.SWIPE

    override fun onDraw(canvas: Canvas) {
        hoursPickerPath.onDraw(canvas)
        hoursTrianglePath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        val center = PointF(width / 2f, height / 2f)
        val radius = Math.min(width, height) / 4f
        updatePaint(center, radius)
        drawShapes(center, radius)
    }

    fun updatePaint(center: PointF, radius: Float) {
        val startPoint = getPointOnBorderLineOfCircle(center, radius, 180)
        val endPoint = getPointOnBorderLineOfCircle(center, radius, 0)
        hoursPickerPath.paint.apply {
            shader = LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, colors,
                    null,
                    Shader.TileMode.CLAMP)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(PointF(event.x, event.y))
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(PointF(event.x, event.y))
            }
            MotionEvent.ACTION_UP -> {
                onActionUp()
            }
        }
        return true
    }

    private fun drawShapes(center: PointF, radius: Float) {
        hoursPickerPath.center = center
        hoursTrianglePath.center = center

        hoursPickerPath.radius = radius
        hoursTrianglePath.radius = radius

        previousTouchPoint = center

        hoursPickerPath.createPickerPath()
        hoursTrianglePath.createTrianglePath()
    }

    fun swipePoint(pointF: PointF) =
            pointInCircle(pointF, hoursPickerPath.center, hoursPickerPath.radius) &&
                    !pointInCircle(pointF, hoursPickerPath.center, (hoursPickerPath.radius * SWIPE_RADIUS_FACTOR))

    private fun onActionDown(pointF: PointF) {
        val pointInCircle = pointInCircle(pointF, hoursPickerPath.center, hoursPickerPath.radius)
        if (!swipePoint(pointF)) {
            touchState = TouchState.SWIPE
        } else {
            touchState = TouchState.ROTATE
        }
        previousTouchPoint = pointF
        hoursPickerPath.lockMove = !pointInCircle
        hoursTrianglePath.lockMove = !pointInCircle
    }


    private fun onActionMove(pointF: PointF) {
        previousTouchPoint = pointF

        if (!isSwipe()) {
            angle = calculateAngleWithTwoVectors(pointF, hoursPickerPath.center)
            val distance = distance(pointF, hoursPickerPath.center) - hoursPickerPath.radius
            //TODO clean up code
            val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))

            hoursPickerPath.onActionMove(angle, pullUp)

            if (pullUp != 0f) {
                hoursTrianglePath.onActionMove(angle, pullUp)
            }
            valueListener?.valueListener(calculateValue(angle = angle.toInt()))
        } else {
            //
        }
    }

    private fun onActionUp() {
        hoursPickerPath.lockMove = true
        hoursTrianglePath.lockMove = true
        hoursPickerPath.onActionUp()
        hoursTrianglePath.onActionUp()
    }

    enum class TouchState { SWIPE, ROTATE }
}
