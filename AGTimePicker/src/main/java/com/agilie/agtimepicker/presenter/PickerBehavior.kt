package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.view.TimePickerView


class PickerBehavior : BaseBehavior, TimePickerContract.Behavior {

    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath,
                behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) : super(timePickerView, hoursPickerPath) {
        init(behaviorConstructor)
    }

    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath,
                colors: IntArray, behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) : super(timePickerView, hoursPickerPath, colors) {
        init(behaviorConstructor)
    }

    var behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor? = null
    fun init(behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) {
        this.behaviorConstructor = behaviorConstructor
    }

    override fun calculateValue(lap: Int, angle: Int): Int {
        val valuesPerLap = countOfValues() / countOfLaps()
        val anglesPerValue = 360 / valuesPerLap
        val closestAngle = (0..360 step anglesPerValue).firstOrNull { it > angle } ?: 0
        return (valuesPerLap * closestAngle) / 360
    }

    override fun countOfLaps() = behaviorConstructor?.countOfLaps() ?: 0

    override fun countOfValues() = behaviorConstructor?.countOfValues() ?: 0

    override fun value(value: Int) {
        behaviorConstructor?.onValueCalculated(value)
    }
}