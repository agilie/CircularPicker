package com.agilie.circularpicker.presenter

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.circularpicker.ui.animation.CircularPickerPath
import com.agilie.circularpicker.ui.view.CircularPickerView
import com.agilie.circularpicker.ui.view.CircularPickerView.Companion.CIRCULAR_PICKER_RATIO
import com.agilie.circularpicker.ui.view.CircularPickerView.Companion.MAX_PULL_UP
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import java.lang.Math.*


abstract class BaseBehavior : AGCircularPickerContract.Behavior {

    val view: CircularPickerView
    val circularPickerPath: CircularPickerPath
    var countOfValues = 24
    var maxLapCount = 2
    var currentValue = 0
    var colors: IntArray = intArrayOf(
            Color.parseColor("#0080ff"),
            Color.parseColor("#53FFFF"))

    constructor(view: CircularPickerView, circularPickerPath: CircularPickerPath) {
        this.view = view
        this.circularPickerPath = circularPickerPath
    }

    private companion object {
        val MIN_ANGLE = 0
        val MAX_ANGLE = 360
    }

    var valueChangeListener: AGCircularPickerContract.Behavior.ValueChangeListener? = null

    var centeredText = ""

    var picker = true

    val pointCenter: PointF
        get() = circularPickerPath.center
    val radius: Float
        get() = circularPickerPath.radius

    var centeredTextSize = 50f
    var centeredStrokeWidth = 40f
    var centeredTypeface = Typeface.DEFAULT
    var centeredTextColor = Color.WHITE

    var textPaint = Paint().apply {
        color = centeredTextColor
        textSize = centeredTextSize
        typeface = centeredTypeface
        strokeWidth = centeredStrokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        circularPickerPath.onDraw(canvas)
        drawText(canvas)
    }

    fun drawText(canvas: Canvas) {
        textPaint.color = centeredTextColor
        textPaint.textSize = centeredTextSize
        textPaint.typeface = centeredTypeface
        textPaint.strokeWidth = centeredStrokeWidth
        canvas.drawText(centeredText, (pointCenter.x - (textPaint.measureText(centeredText) / 2)),
                (pointCenter.y - ((textPaint.descent() + textPaint.ascent())) / 2), textPaint)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        val center = PointF(width / 2f, height / 2f)
        val radius = max(min(width, height), 0) / CIRCULAR_PICKER_RATIO
        updatePaint(center, radius)
        drawShapes(center, radius)
    }

    var gradientAngle = 0

    fun updatePaint(center: PointF, radius: Float) {
        val startPoint = getPointOnBorderLineOfCircle(center, radius, 180 + gradientAngle)
        val endPoint = getPointOnBorderLineOfCircle(center, radius, 0 + gradientAngle)
        circularPickerPath.pickerPaint.apply {
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
                view.onInvalidate()
            }
            MotionEvent.ACTION_UP -> {

                onActionUp(PointF(event.x, event.y))
                view.onInvalidate()
            }
        }
        return true
    }

    private fun drawShapes(center: PointF, radius: Float) {
        circularPickerPath.center = center
        circularPickerPath.radius = radius
        circularPickerPath.createPickerPath()
        // rotate to current value
        val angle = calculateClosestAngle(currentValue)
        circularPickerPath.onActionDown(angle, MAX_PULL_UP)
        previousAngle = angle


    }

    private fun calculateClosestAngle(currentValue: Int): Int {
        if (currentValue >= countOfValues) {
            totalAngle = maxLapCount * MAX_ANGLE
            return MAX_ANGLE
        } else if (currentValue <= 0) {
            return 0
        } else {
            val valPerAngle = (maxLapCount * MAX_ANGLE) / countOfValues
            totalAngle = valPerAngle * currentValue
            return totalAngle
        }

    }

    private fun onActionDown(pointF: PointF) {
        calculateAngleValue(pointF)
    }

    private fun onActionMove(pointF: PointF) {

        if (picker) {
            val currentAngle = calculateAngleWithTwoVectors(pointF, circularPickerPath.center).toInt()
            val angleChanged = previousAngle != currentAngle

            if (angleChanged) {
                if (abs(currentAngle - previousAngle) < 180) {
                    totalAngle += (currentAngle - previousAngle)
                    if (totalAngle > MAX_ANGLE * maxLapCount) {
                        totalAngle = MIN_ANGLE
                    }
                    if (totalAngle < MIN_ANGLE) {
                        totalAngle = MAX_ANGLE * maxLapCount
                    }
                }

                Log.d("TAG", "totalAngle $totalAngle  ")
                previousAngle = currentAngle

                val distance = distance(pointF, circularPickerPath.center) - circularPickerPath.radius
                val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))
                circularPickerPath.onActionMove(totalAngle, pullUp)
                value(calculateValue(totalAngle))
            }
        }
    }

    private var actionDownAngle = 0
    private var totalAngle = 0
    private var previousAngle = 0

    private fun onActionUp(pointF: PointF) {
        previousAngle = 0
        circularPickerPath.lockMove = true
    }

    private fun calculateAngleValue(pointF: PointF) {
        if (picker) {
            circularPickerPath.lockMove = !picker
            val distance = distance(pointF, circularPickerPath.center) - circularPickerPath.radius
            val pullUp = Math.min(MAX_PULL_UP, Math.max(distance, 0f))
            actionDownAngle = calculateAngleWithTwoVectors(pointF, circularPickerPath.center).toInt()

            if (totalAngle > MAX_ANGLE) {
                calculateAngleDiff(actionDownAngle)
            } else {
                totalAngle = actionDownAngle
            }

            previousAngle = actionDownAngle
            circularPickerPath.onActionMove(actionDownAngle, pullUp)
            value(calculateValue(totalAngle))
            view.onInvalidate()
        }
    }

    private fun calculateAngleDiff(touchAngle: Int) {
        val angleInLap = (totalAngle / MAX_ANGLE) * MAX_ANGLE
        val diff = totalAngle - angleInLap
        if (touchAngle > diff) {
            totalAngle += abs(touchAngle - diff)
        } else {
            totalAngle -= abs(touchAngle - diff)
        }

    }

    abstract fun build()
}
