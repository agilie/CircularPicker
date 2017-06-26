package com.agilie.circularpicker.presenter.circularpicker

import android.graphics.Canvas
import android.view.MotionEvent

interface AGCircularPicker {
    fun onDraw(canvas: Canvas)
    fun onSizeChanged(width: Int, height: Int)
    fun onTouchEvent(event: MotionEvent): Boolean
}