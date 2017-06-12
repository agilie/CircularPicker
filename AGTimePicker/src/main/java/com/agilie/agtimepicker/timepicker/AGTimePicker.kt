package com.agilie.agtimepicker.timepicker

import android.graphics.Canvas

interface AGTimePicker {
    fun onDraw(canvas: Canvas)
    fun onSizeChanged(width: Int, height: Int)
}