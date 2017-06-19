package com.agilie.agtimepicker.presenter

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.view.TimePickerView


class BehaviorWrapper(var timePickerView: TimePickerView, var behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) {

    fun generateBehavior() = PickerBehavior(timePickerView, PickerPath(setPickerPaint(), setTrianglePaint()), behaviorConstructor)

    private fun setTrianglePaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        pathEffect = CornerPathEffect(10f)
        strokeWidth = 2f
    }

    private fun setPickerPaint() = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 4f
    }
}