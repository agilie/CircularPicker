package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.presenter.timepicker.AGTimePicker


interface TimePickerContract {

    interface Layout

    interface Behavior : AGTimePicker {

        fun calculateValue(angle: Int): Int
        fun value(value: Int)

        interface BehaviorConstructor {
            fun onValueCalculated(value: Int)
        }
    }

    interface View
}