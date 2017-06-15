package com.agilie.agtimepicker.timepicker

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.agtimepicker.animation.PickerPath
import com.agilie.agtimepicker.animation.TrianglePath
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import com.agilie.volumecontrol.pointInCircle
import java.lang.Math.max
import java.lang.Math.min

class AGTimePickerController(val hoursPickerPath: PickerPath,
                             val minutesPickerPath: PickerPath,
                             val hoursTrianglePath: TrianglePath,
                             val minutesTrianglePath: TrianglePath,
                             var hoursColors: IntArray = intArrayOf(
                                     Color.parseColor("#0080ff"),
                                     Color.parseColor("#53FFFF")),
                             var minutesColor: IntArray = intArrayOf(
                                     Color.parseColor("#FF8D00"),
                                     Color.parseColor("#FF0058"),
                                     Color.parseColor("#920084")
                             )) : AGTimePicker {

    private val MAX_PULL_UP = 65f
    val SWIPE_RADIUS_FACTOR = 0.6f
    var viewState = ClockState.HOURS
    var touchState = TouchState.ROTATE
    private var previousTouchPoint = PointF()

    override fun onDraw(canvas: Canvas) {
        hoursPickerPath.onDraw(canvas)
        minutesPickerPath.onDraw(canvas)

        hoursTrianglePath.onDraw(canvas)
        minutesTrianglePath.onDraw(canvas)
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
            shader = LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, hoursColors,
                    null,
                    Shader.TileMode.CLAMP)
        }
        minutesPickerPath.paint.apply {
            shader = LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, minutesColor,
                    null,
                    Shader.TileMode.CLAMP)
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
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

    fun isSwipe() = touchState == TouchState.SWIPE

    fun onSwipeRight(diff: Float) {
        if (isSwipe()) {

            Log.d("swipetest1", "right")
        }
    }

    fun onSwipeLeft(diff: Float) {
        if (isSwipe()) {
            Log.d("swipetest1", "left")
        }
    }

    fun onSwipeTop(diff: Float) {
    }

    fun onSwipeBottom(diff: Float) {
    }

    private fun drawShapes(center: PointF, radius: Float) {
        hoursPickerPath.center = center
        minutesPickerPath.center.apply {
            y = center.y
            x = center.x + 2.09f * radius + MAX_PULL_UP
        }
        hoursTrianglePath.center = center
        minutesPickerPath.center = minutesPickerPath.center

        hoursPickerPath.radius = radius
        minutesPickerPath.radius = radius
        hoursTrianglePath.radius = radius
        minutesTrianglePath.radius = radius
        previousTouchPoint = center

        hoursPickerPath.createPickerPath()
        minutesPickerPath.createPickerPath()
        hoursTrianglePath.createTrianglePath()
        minutesTrianglePath.createTrianglePath()
    }

    private fun onActionDown(pointF: PointF) {
        val pointInCircle = pointInCircle(pointF, hoursPickerPath.center, hoursPickerPath.radius)
        if (!swipePoint(pointF)) {
            touchState = TouchState.SWIPE
        } else {
            touchState = TouchState.ROTATE
        }
        previousTouchPoint = pointF
        hoursPickerPath.lockMove = !pointInCircle
        minutesPickerPath.lockMove = !pointInCircle
        hoursTrianglePath.lockMove = !pointInCircle
        minutesTrianglePath.lockMove = !pointInCircle

    }


    fun swipePoint(pointF: PointF) = when (viewState) {
        ClockState.HOURS -> {
            pointInCircle(pointF, hoursPickerPath.center, hoursPickerPath.radius) &&
                    !pointInCircle(pointF, hoursPickerPath.center, (hoursPickerPath.radius * SWIPE_RADIUS_FACTOR))
        }
        ClockState.MINUTES -> {
            pointInCircle(pointF, minutesPickerPath.center, minutesPickerPath.radius) &&
                    !pointInCircle(pointF, minutesPickerPath.center, (minutesPickerPath.radius * SWIPE_RADIUS_FACTOR))
        }
    }


    private fun onActionMove(pointF: PointF) {
        val moveDistance = distance(pointF, previousTouchPoint)
        val vector = pointF.x - previousTouchPoint.x
        previousTouchPoint = pointF

        if (!isSwipe()) {
            val angle = calculateAngleWithTwoVectors(pointF, hoursPickerPath.center)
            val distance = distance(pointF, hoursPickerPath.center) - hoursPickerPath.radius
            //TODO clean up code
            val pullUp = min(MAX_PULL_UP, max(distance, 0f))

            hoursPickerPath.onActionMove(angle, pullUp)
            minutesPickerPath.onActionMove(angle, pullUp)

            if (pullUp != 0f) {
                hoursTrianglePath.onActionMove(angle, pullUp)
                minutesTrianglePath.onActionMove(angle, pullUp)
            }

        } else {
            hoursPickerPath.center.x += vector
            minutesPickerPath.center.x += vector

            hoursTrianglePath.center = hoursPickerPath.center
            minutesTrianglePath.center = minutesPickerPath.center

            hoursPickerPath.onUpdatePickerPath()
            minutesPickerPath.onUpdatePickerPath()
            hoursTrianglePath.onUpdatePickerPath()
            minutesTrianglePath.onUpdatePickerPath()
        }
    }

    private fun onActionUp() {
        hoursPickerPath.lockMove = true
        minutesPickerPath.lockMove = true
        hoursTrianglePath.lockMove = true
        minutesTrianglePath.lockMove = true

        hoursPickerPath.onActionUp()
        minutesPickerPath.onActionUp()

        hoursTrianglePath.onActionUp()
        minutesTrianglePath.onActionUp()
    }

    enum class ClockState { HOURS, MINUTES }
    enum class TouchState { SWIPE, ROTATE }

    fun setDiffX(diffX: Float) {
        //this.
    }
}