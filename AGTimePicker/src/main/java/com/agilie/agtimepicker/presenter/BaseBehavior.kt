package com.agilie.agtimepicker.presenter

import android.graphics.*
import android.util.Log
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
                            var colors: IntArray = intArrayOf(
                                    Color.parseColor("#0080ff"),
                                    Color.parseColor("#53FFFF"))) : TimePickerContract.Behavior {

    private val MAX_PULL_UP = 45f
    var angle = 0f
    val SWIPE_RADIUS_FACTOR = 0.6f
    var picker = true

    val pointCenter: PointF
        get() = pickerPath.center
    val radius: Float
        get() = pickerPath.radius

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        val center = PointF(width / 2f, height / 2f)
        val radius = max(min(width, height), 0) / 3f
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
                if (picker)actionDownAngle = calculateAngleWithTwoVectors(PointF(event.x, event.y), pickerPath.center).toInt()
                onActionDown(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                angleDelta = 0
                if (picker) previousAngle = calculateAngleWithTwoVectors(PointF(event.x, event.y), pickerPath.center).toInt()
                onActionUp()
                view.onInvalidate()
            }
        }
        return true
    }

    private fun drawShapes(center: PointF, radius: Float) {
        pickerPath.center = center
        pickerPath.radius = radius
        pickerPath.createPickerPath()
    }


    private fun onActionDown(pointF: PointF) {
        val action = pointInActionArea(pointF)
        pickerPath.lockMove = !action
    }

    fun pointInActionArea(pointF: PointF) = pointInCircle(pointF, pickerPath.center, pickerPath.radius) &&
            !pointInCircle(pointF, pickerPath.center, (pickerPath.radius * SWIPE_RADIUS_FACTOR))

    private fun onActionMove(pointF: PointF) {
        val currentAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()

        if (previousAngle != currentAngle) {
            if (overlappedClockwise(direction, previousAngle, currentAngle)) {
                angleDelta += (360 - previousAngle + currentAngle)
            } else if (overlappedCclockwise(direction, previousAngle, currentAngle)) {
                angleDelta -= (360 - currentAngle + previousAngle)
            } else if (previousAngle < currentAngle) {
                direction = Direction.CLOCKWISE
                angleDelta += (currentAngle - previousAngle)
            } else {
                direction = Direction.CCLOCKWISE
                angleDelta += (previousAngle - currentAngle)
            }
        }
        Log.d("TestLogTest", "currentAngle $currentAngle previousAngle $previousAngle direction $direction actionDownAngle $actionDownAngle angleDelta $angleDelta")
        if (picker) {
            angle = calculateAngleWithTwoVectors(pointF, pickerPath.center)
            val distance = distance(pointF, pickerPath.center) - pickerPath.radius
            //TODO clean up code
            val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))
            pickerPath.onActionMove(angle, pullUp)

            newLap(Math.max(Math.min(actionDownAngle + angleDelta, 360), 0))
        }
        previousAngle = currentAngle
        value(calculateValue(currentLap, angle.toInt()))
    }

    var currentLap = 0
    var maxLaps = 1
    private var actionDownAngle: Int = 0
    private var angleDelta = 0

    private var previousAngle = 0
    private var direction: Direction = Direction.UNDEFINED

    enum class Direction {
        UNDEFINED, CLOCKWISE, CCLOCKWISE
    }
    private fun overlappedCclockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CCLOCKWISE && (currentAngle - previousAngle) > 45

    private fun overlappedClockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CLOCKWISE && (previousAngle - currentAngle) > 45

    private fun newLap(angle: Int) {
//        Log.d("TestLogTest", "_____________________________${angle}")
        if (angle == 360) {
            currentLap++
            actionDownAngle = 0
            angleDelta = 0
        }
        else if (angle == 0) {
            currentLap--
            actionDownAngle = 0
            angleDelta = 0
        }
        Log.d("TestLogTest", "_______________$currentLap   ${this.angle}")
    }

    private fun onActionUp() {
        pickerPath.lockMove = true
        pickerPath.onActionUp()
    }

    enum class TouchState { SWIPE, ROTATE }
}
