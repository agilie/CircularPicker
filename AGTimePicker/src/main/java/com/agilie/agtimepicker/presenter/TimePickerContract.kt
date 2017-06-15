package com.agilie.agtimepicker.presenter

import com.agilie.agtimepicker.presenter.timepicker.AGTimePicker


interface TimePickerContract {

    interface Layout

    interface Behavior : AGTimePicker {

        fun calculateValue(angle: Int) : Float

        interface ValueListener {
            fun angleListener(angle: Int) {}
            fun valueListener(value: Float)
        }
    }

    interface View
}