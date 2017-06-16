package com.agilie.agtimepicker.presenter

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.animation.TrianglePath
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import com.agilie.volumecontrol.pointInCircle


abstract class BaseBehavior(val pickerPath: PickerPath,
                            val trianglePath: TrianglePath,
                            var colors: IntArray = intArrayOf(
                                    Color.parseColor("#0080ff"),
                                    Color.parseColor("#53FFFF"))) : TimePickerContract.Behavior {

    private val MAX_PULL_UP = 65f
    private var previousTouchPoint = PointF()
    var angle = 0
    var picker = false
    var valueListener: TimePickerContract.Behavior.ValueListener? = null

    var countOfLap = 2
    val pointCenter: PointF
        get() = pickerPath.center
    val radius: Float
        get() = pickerPath.radius

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
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
        pickerPath.paint.apply {
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
        pickerPath.center = center
        trianglePath.center = center

        pickerPath.radius = radius
        trianglePath.radius = radius

        previousTouchPoint = center

        pickerPath.createPickerPath()
        trianglePath.createTrianglePath()
    }

    private var actionDownAngle = 0

    private fun onActionDown(pointF: PointF) {
        actionDownAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()

        val pointInCircle = pointInCircle(pointF, pickerPath.center, pickerPath.radius)
        previousTouchPoint = pointF
        pickerPath.lockMove = !pointInCircle
        trianglePath.lockMove = !pointInCircle
    }

    private var previousAngle = 0
    private var angleDelta = 0

    private var direction: Direction = Direction.UNDEFINED

    enum class Direction {
        UNDEFINED, CLOCKWISE, CCLOCKWISE
    }

    private fun overlappedCclockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CCLOCKWISE && (currentAngle - previousAngle) > 45

    private fun overlappedClockwise(direction: Direction, previousAngle: Int, currentAngle: Int) = direction == Direction.CLOCKWISE && (previousAngle - currentAngle) > 45

    var prevToListenerAngle = -1
    private fun onActionMove(pointF: PointF) {
        previousTouchPoint = pointF
        angle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()
        if (picker && moveMore(angle)) {
            val distance = distance(pointF, pickerPath.center) - pickerPath.radius
            //TODO clean up code
            val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))

            pickerPath.onActionMove(angle.toFloat(), pullUp)

            if (pullUp != 0f) {
                trianglePath.onActionMove(angle.toFloat(), pullUp)
            }
            if (angle != prevToListenerAngle) {
                Log.d("rotateTest", "toListener!!!!!!!!!!!! $angle")
                prevToListenerAngle = angle
                valueListener?.valueListener(calculateValue(angle = angle))
            }
        }
    }

    private fun moveMovableCircle(angle: Int): Boolean {
        if (angle == 360 * countOfLap || angle == 0) {
            return false
        }
        return true
    }

    fun moveMore(angle: Int): Boolean {
        if (previousAngle != angle) {
            if (overlappedClockwise(direction, previousAngle, angle)) {
                angleDelta += (360 - previousAngle + angle)
            } else if (overlappedCclockwise(direction, previousAngle, angle)) {
                angleDelta -= (360 - angle + previousAngle)
            } else if (previousAngle < angle) {
                direction = Direction.CLOCKWISE
                angleDelta += (angle - previousAngle)
            } else {
                direction = Direction.CCLOCKWISE
                angleDelta -= (previousAngle - angle)
            }
        }
        previousAngle = angle
        Log.d("rotateTest", "dir == $direction delta == $angleDelta prev == $previousAngle angle == $angle actionDownAngle == $actionDownAngle actionDownAngle + angleDelta ${actionDownAngle + angleDelta}")

        this.angle = Math.max(Math.min(actionDownAngle + angleDelta, 360 * countOfLap), 0)

        return moveMovableCircle(angle)
    }

    private fun onActionUp() {
        angleDelta = 0
        direction = Direction.UNDEFINED
        actionDownAngle = 0
        pickerPath.lockMove = true
        trianglePath.lockMove = true
        pickerPath.onActionUp()
        trianglePath.onActionUp()
    }

    enum class TouchState { SWIPE, ROTATE }
}
