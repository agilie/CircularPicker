package com.agilie.agtimepicker.presenter

import android.util.Log
import com.agilie.agtimepicker.ui.animation.PickerPath
import com.agilie.agtimepicker.ui.view.TimePickerView


class PickerBehavior : BaseBehavior, TimePickerContract.Behavior {

//    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath,
//                behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) : super(timePickerView, hoursPickerPath) {
//        init(behaviorConstructor)
//    }
//
//    constructor(timePickerView: TimePickerView, hoursPickerPath: PickerPath,
//                colors: IntArray, behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor) : super(timePickerView, hoursPickerPath, colors) {
//        init(behaviorConstructor)
//    }


    var valuesPerLap = 1
    var anglesPerValue = 1
    var behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor? = null

    constructor(view: TimePickerView, pickerPath: PickerPath, countOfValues: Int, countOfLaps: Int, colors: IntArray, behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor?) :
            super(view, pickerPath, countOfValues, countOfLaps, colors) {
        init(behaviorConstructor)
    }

    constructor(view: TimePickerView, pickerPath: PickerPath, countOfValues: Int, countOfLaps: Int, behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor?) :
            super(view, pickerPath, countOfValues, countOfLaps) {
        init(behaviorConstructor)
    }

    fun init(behaviorConstructor: TimePickerContract.Behavior.BehaviorConstructor?) {
        this.behaviorConstructor = behaviorConstructor
        valuesPerLap = countOfValues / maxLapCount
        anglesPerValue = 360 / valuesPerLap
    }

    override fun calculateValue(angle: Int): Int {
        Log.d("valueTest", "angle $angle")
        val closestAngle = (((0..360 * maxLapCount)) step anglesPerValue).firstOrNull { it > angle } ?: 1
        Log.d("valueTest", " closestAngle $closestAngle")

        return (countOfValues * closestAngle) / (360 * maxLapCount)
    }

    override fun value(value: Int) {
        Log.d("valueTest", " value $value")
        behaviorConstructor?.onValueCalculated(value)
    }
}