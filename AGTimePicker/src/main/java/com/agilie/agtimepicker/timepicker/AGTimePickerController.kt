package com.agilie.agtimepicker.timepicker

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.agtimepicker.animation.HoursPickerPath
import com.agilie.agtimepicker.animation.MinutesPickerPath
import com.agilie.agtimepicker.animation.TrianglePath
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import com.agilie.volumecontrol.pointInCircle
import java.lang.Math.max
import java.lang.Math.min

class AGTimePickerController(val hoursPickerPath: HoursPickerPath,
                             val minutesPickerPath: MinutesPickerPath,
                             val trianglePath: TrianglePath,
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

        trianglePath.onDraw(canvas)
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
        trianglePath.center = center

        hoursPickerPath.radius = radius
        minutesPickerPath.radius = radius
        trianglePath.radius = radius
        previousTouchPoint = center

        hoursPickerPath.createPickerPath()
        minutesPickerPath.createPickerPath()
        trianglePath.createTrianglePath()
    }

    private fun onActionDown(pointF: PointF) {
        val pointInCircle = pointInCircle(pointF, hoursPickerPath.center, hoursPickerPath.radius)
        if (!swipePoint(pointF)) {
            touchState = TouchState.SWIPE
        } else {
            touchState = TouchState.ROTATE
        }
        hoursPickerPath.lockMove = !pointInCircle
        trianglePath.lockMove = !pointInCircle
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

        if (!isSwipe()) {
            val angle = calculateAngleWithTwoVectors(pointF, hoursPickerPath.center)
            val distance = distance(pointF, hoursPickerPath.center) - hoursPickerPath.radius
            //TODO clean up code
            val pullUp = min(MAX_PULL_UP, max(distance, 0f))
            hoursPickerPath.onActionMove(angle, pullUp)

            if (pullUp != 0f) trianglePath.onActionMove(angle, pullUp)
        } else {
            previousTouchPoint = pointF
            updatePickerPosition(moveDistance, vector)
        }


    }

    private fun updatePickerPosition(moveDistance: Float, vector: Float) {
        if (vector < 0 && viewState == ClockState.HOURS) {
            hoursPickerPath.center.x -= moveDistance
            minutesPickerPath.center.x -= moveDistance
        } else {
        }
        if (vector > 0 && viewState == ClockState.MINUTES) {
            hoursPickerPath.center.x += moveDistance
            minutesPickerPath.center.x += moveDistance
        } else {
           // previousTouchPoint = minutesPickerPath.center
        }

        hoursPickerPath.onUpdatePickerPath()
        minutesPickerPath.onUpdatePickerPath()
    }

    private fun onActionUp() {
        hoursPickerPath.lockMove = true
        trianglePath.lockMove = true

        hoursPickerPath.onActionUp()
        trianglePath.onActionUp()
    }

    enum class ClockState { HOURS, MINUTES }
    enum class TouchState { SWIPE, ROTATE }

    fun setDiffX(diffX: Float) {
        //this.
    }
}