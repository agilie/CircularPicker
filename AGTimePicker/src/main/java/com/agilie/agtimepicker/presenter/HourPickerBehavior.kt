package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.animation.TrianglePath
import com.agilie.agtimepicker.ui.view.TimePickerView


class HourPickerBehavior : BaseBehavior, TimePickerContract.Behavior {
    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath, hoursTrianglePath: TrianglePath) : super(timePickerView, hoursPickerPath, hoursTrianglePath)
    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath, hoursTrianglePath: TrianglePath, colors: IntArray) : super(timePickerView, hoursPickerPath, hoursTrianglePath, colors)

    override fun calculateValue(angle: Int) = 7f
}