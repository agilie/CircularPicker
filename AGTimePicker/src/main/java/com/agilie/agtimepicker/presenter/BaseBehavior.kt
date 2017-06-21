package com.agilie.agtimepicker.presenter

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import java.lang.Math.max
import java.lang.Math.min


abstract class BaseBehavior(val view: TimePickerView,
                            val pickerPath: PickerPath,
                            var countOfValues: Int,
                            var maxLapCount: Int,
                            var colors: IntArray = intArrayOf(
                                    Color.parseColor("#0080ff"),
                                    Color.parseColor("#53FFFF"))) : TimePickerContract.Behavior {




    private val MAX_PULL_UP = 45f
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
                //if (picker) actionDownAngle = calculateAngleWithTwoVectors(PointF(event.x, event.y), pickerPath.center).toInt()
                onActionDown(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(PointF(event.x, event.y))
                view.onInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                angleDelta = 0
                //if (picker) previousAngle = calculateAngleWithTwoVectors(PointF(event.x, event.y), pickerPath.center).toInt()
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
        //val action = pointInActionArea(pointF)
        actionDownAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()
        previousAngle = actionDownAngle
        direction = Direction.UNDEFINED
        angleDelta = 0


        Log.d("TAG", "actionDownAngle $actionDownAngle")
        pickerPath.lockMove = !picker
    }

    /*fun pointInActionArea(pointF: PointF) = pointInCircle(pointF, pickerPath.center, pickerPath.radius) &&
            !pointInCircle(pointF, pickerPath.center, (pickerPath.radius * SWIPE_RADIUS_FACTOR))*/

    private var lapCount = 1
    private fun onActionMove(pointF: PointF) {
        val currentAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()

        if (picker) {
            //val currentAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()

            val angleChanged = previousAngle != currentAngle

            if (angleChanged) {
                if (overlappedClockwise(direction, previousAngle, currentAngle)) {
                    angleDelta += (360 - previousAngle + currentAngle)
                } else if (overlappedCclockwise(direction, previousAngle, currentAngle)) {
                    angleDelta -= (360 - currentAngle + previousAngle)
                } else if (previousAngle < currentAngle) {
                    direction = Direction.CLOCKWISE
                    angleDelta += (currentAngle - previousAngle)
                } else {
                    direction = Direction.CCLOCKWISE
                    angleDelta -= (previousAngle - currentAngle)
                }
            }

            if (lapCount <= 0) {
                lapCount = 1
            }
            val angle = Math.max(Math.min(actionDownAngle + angleDelta, 360), 0)
            if (direction == Direction.CLOCKWISE && angle == 360) {
                if (angle == 360) {
                    lapCount++
                }
                if (angle == 360 && lapCount <= maxLapCount) {
                    actionDownAngle = 0
                    angleDelta = 0
                }
                if (angle == 360 && lapCount >= maxLapCount) {
                    lapCount = 1
                    actionDownAngle = 0
                    angleDelta = 0
                }
            }
            // when CCLOCKWISE
            if (direction == Direction.CCLOCKWISE && angle == 0) {
                if (lapCount == 1) {
                    lapCount = maxLapCount
                } else {
                    lapCount--
                }
                actionDownAngle = 0
                angleDelta = 360
            }


            previousAngle = currentAngle

            Log.d("TAG", "angle  $angle lapCount $lapCount  currentAngle $currentAngle actionDownAngle $actionDownAngle + angleDelta + $angleDelta")

            val distance = distance(pointF, pickerPath.center) - pickerPath.radius
            //TODO clean up code
            val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))
            pickerPath.onActionMove(currentAngle.toFloat(), pullUp)
            value(calculateValue(((360 * lapCount) - 360) + angle))
        }

    }

    var currentLap = 0
    private var actionDownAngle: Int = 0
    private var angleDelta = 0

    private var previousAngle = 0
    private var direction: Direction = Direction.UNDEFINED

    enum class Direction {
        UNDEFINED, CLOCKWISE, CCLOCKWISE
    }

    private fun overlappedCclockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CCLOCKWISE && (currentAngle - previousAngle) > 45

    private fun overlappedClockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CLOCKWISE && (previousAngle - currentAngle) > 45

    private fun onActionUp() {

        actionDownAngle = 0
        angleDelta = 0
        currentLap = 0
        lapCount = 0
        pickerPath.lockMove = true
        pickerPath.onActionUp()
    }

    enum class TouchState { SWIPE, ROTATE }
}
