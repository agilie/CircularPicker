package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.presenter.timepicker.AGTimePicker


interface TimePickerContract {

    interface Layout

    interface Behavior : AGTimePicker {

        fun calculateValue(lap: Int, angle: Int): Int
        fun countOfLaps(): Int
        fun countOfValues(): Int
        fun value(value: Int)

        interface BehaviorConstructor {
            fun countOfLaps(): Int
            fun countOfValues(): Int
            fun onValueCalculated(value: Int)
        }
    }

    interface View
}