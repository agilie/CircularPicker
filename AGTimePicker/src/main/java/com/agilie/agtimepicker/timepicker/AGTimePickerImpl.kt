package com.agilie.agtimepicker.timepicker

import android.graphics.Canvas
import android.graphics.PointF
import android.view.MotionEvent
import com.agilie.agtimepicker.animation.PickerPath
import com.agilie.volumecontrol.calculateAngleWithTwoVectors

class AGTimePickerImpl(val pickerPath: PickerPath) : AGTimePicker {

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        pickerPath.center.apply {
            x = width / 2f
            y = height / 2f
        }

        val radius = Math.min(width, height) / 6f
        pickerPath.radius = radius
    }


    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(PointF(event.x, event.y))
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                //nothing here
            }
        }
    }

    private fun onActionDown(pointF: PointF) {
        val angle = calculateAngleWithTwoVectors(pointF, pickerPath.center)
        pickerPath.onActionDown(angle)
    }
}