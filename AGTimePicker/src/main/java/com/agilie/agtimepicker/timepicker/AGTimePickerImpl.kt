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

class AGTimePickerImpl(val pickerPath: PickerPath,
                       val trianglePath: TrianglePath,
                       var colors: IntArray = intArrayOf(
                               Color.parseColor("#0080ff"),
                               Color.parseColor("#53FFFF"))) : AGTimePicker {

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
        trianglePath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {

        val center = PointF(width / 2f, height / 2f)
        val radius = Math.min(width, height) / 5f
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

    private fun drawShapes(center: PointF, radius: Float) {
        pickerPath.center = center
        trianglePath.center = center

        pickerPath.radius = radius
        trianglePath.radius = radius

        pickerPath.createPickerPath()
        trianglePath.createTrianglePath()
    }

    private fun onActionDown(pointF: PointF) {
        val pointInCircle = pointInCircle(pointF, pickerPath.center, pickerPath.radius)
        pickerPath.lockMove = !pointInCircle
    }

    private fun onActionMove(pointF: PointF) {
        val angle = calculateAngleWithTwoVectors(pointF, pickerPath.center)
        val distance = distance(pointF, pickerPath.center) - pickerPath.radius
        //TODO clean up code
        val pullUp = min(5 * 15f, max(distance, 0f))
        Log.d("AGTimePickerImpl", "touchP = $pointF pullUp = $pullUp")
        pickerPath.onActionMove(angle, pullUp)
    }

    private fun onActionUp() {
        pickerPath.lockMove = true
        pickerPath?.onActionUp()
    }
}