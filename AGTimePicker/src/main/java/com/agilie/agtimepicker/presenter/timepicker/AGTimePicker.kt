package com.agilie.agtimepicker.presenter.timepicker

import android.graphics.Canvas
import android.view.MotionEvent

interface AGTimePicker {
    fun onDraw(canvas: Canvas)
    fun onSizeChanged(width: Int, height: Int)
    fun onTouchEvent(event: MotionEvent) : Boolean
}