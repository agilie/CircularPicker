package com.agilie.agtimepicker.timepicker

import android.graphics.Canvas
import android.view.MotionEvent
import com.agilie.agtimepicker.animation.PickerPath

class AGTimePickerImpl(val pickerPath: PickerPath) : AGTimePicker {

    override fun onDraw(canvas: Canvas) {
        pickerPath.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        pickerPath.center.apply {
            x = width / 2f
            y = height / 2f
        }
    }


    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                //nothing here
            }
        }
    }
}