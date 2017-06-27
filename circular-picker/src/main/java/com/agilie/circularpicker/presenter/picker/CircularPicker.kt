package com.agilie.circularpicker.presenter.picker

import android.graphics.Canvas
import android.view.MotionEvent

interface CircularPicker {
    fun onDraw(canvas: Canvas)
    fun onSizeChanged(width: Int, height: Int)
    fun onTouchEvent(event: MotionEvent): Boolean
}