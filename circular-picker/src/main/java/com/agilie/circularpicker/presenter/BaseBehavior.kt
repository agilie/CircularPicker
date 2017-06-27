package com.agilie.circularpicker.presenter

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import com.agilie.circularpicker.ui.animation.PickerPath
import com.agilie.circularpicker.ui.view.CircularPickerView
import com.agilie.volumecontrol.calculateAngleWithTwoVectors
import com.agilie.volumecontrol.distance
import com.agilie.volumecontrol.getPointOnBorderLineOfCircle
import java.lang.Math.*


abstract class BaseBehavior : CircularPickerContract.Behavior {

    val view: CircularPickerView
    val pickerPath: PickerPath
    var countOfValues = 24
    var currentValue = 0
    var maxLapCount = 2
    var colors: IntArray = intArrayOf(
            Color.parseColor("#0080ff"),
            Color.parseColor("#53FFFF"))

    constructor(view: CircularPickerView, pickerPath: PickerPath) {
        this.view = view
        this.pickerPath = pickerPath
    }

    private companion object {
        val MIN_ANGLE = 0
        val MAX_ANGLE = 360
    }

    var valueChangeListener: CircularPickerContract.Behavior.ValueChangeListener? = null

    var centeredText = ""
    var swipeRadiusFactor = 0.6f
    var viewSpace = 3f
    var maxPullUp = 35f

    var picker = true

    val pointCenter: PointF
        get() = pickerPath.center
    val radius: Float
        get() = pickerPath.radius

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
        pickerPath.onDraw(canvas)
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
        val radius = max(min(width, height), 0) / viewSpace
        updatePaint(center, radius)
        drawShapes(center, radius)
    }

    var gradientAngle = 0

    fun updatePaint(center: PointF, radius: Float) {
        val startPoint = getPointOnBorderLineOfCircle(center, radius, 180 + gradientAngle)
        val endPoint = getPointOnBorderLineOfCircle(center, radius, 0 + gradientAngle)
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
                previousPoint = PointF(event.x, event.y)
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
        pickerPath.center = center
        pickerPath.radius = radius
        pickerPath.createPickerPath()
        // rotate to current value
        val angle = calculateClosestAngle(currentValue)
        pickerPath.onActionDown(angle, maxPullUp)
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

    private fun changeColors(angle: Int, pullUp: Float) {

        val colorPoint = getPointOnBorderLineOfCircle(pointCenter, radius - pullUp, angle)

        view.buildDrawingCache()
        var pixel = view.getDrawingCache(true).getPixel(colorPoint.x.toInt(), colorPoint.y.toInt())
        view.colorChangeListener?.onColorChange(Color.red(pixel), Color.green(pixel), Color.blue(pixel))
    }

    var previousPoint = PointF()
    private fun onActionMove(pointF: PointF) {

        if (picker) {
            val currentAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()
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

                val distance = distance(pointF, pickerPath.center) - pickerPath.radius
                val pullUp = Math.min(maxPullUp, Math.max(distance, 0f))
                pickerPath.onActionMove(totalAngle, pullUp)
                value(calculateValue(totalAngle))

                changeColors(totalAngle, pullUp)
            }
        }
    }

    private var actionDownAngle = 0
    private var totalAngle = 0
    private var previousAngle = 0

    private fun onActionUp(pointF: PointF) {
        previousAngle = 0
        pickerPath.lockMove = true
    }

    private fun calculateAngleValue(pointF: PointF) {
        if (picker) {
            pickerPath.lockMove = !picker
            val distance = distance(pointF, pickerPath.center) - pickerPath.radius
            val pullUp = Math.min(maxPullUp, Math.max(distance, 0f))
            actionDownAngle = calculateAngleWithTwoVectors(pointF, pickerPath.center).toInt()

            if (totalAngle > MAX_ANGLE) {
                calculateAngleDiff(actionDownAngle)
            } else {
                totalAngle = actionDownAngle
            }

            previousAngle = actionDownAngle
            pickerPath.onActionMove(actionDownAngle, pullUp)
            value(calculateValue(totalAngle))

            changeColors(totalAngle, 0f)

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
