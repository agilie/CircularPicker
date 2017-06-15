package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.animation.TrianglePath


class HourPickerBehavior : BaseBehavior, TimePickerContract.Behavior {
    constructor(hoursPickerPath: PickerPath, hoursTrianglePath: TrianglePath) : super(hoursPickerPath, hoursTrianglePath)
    constructor(hoursPickerPath: PickerPath, hoursTrianglePath: TrianglePath, colors: IntArray) : super(hoursPickerPath, hoursTrianglePath, colors)

    override fun calculateValue(angle: Int) = 7f
}