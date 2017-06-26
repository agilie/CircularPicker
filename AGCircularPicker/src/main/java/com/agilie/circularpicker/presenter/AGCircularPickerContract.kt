package com.agilie.circularpicker.presenter

import com.agilie.circularpicker.presenter.circularpicker.AGCircularPicker


interface AGCircularPickerContract {

    interface Layout

    interface Behavior : AGCircularPicker {

        fun calculateValue(angle: Int): Int
        fun value(value: Int)

        interface ValueChangeListener {
            fun onValueChanged(value: Int)
        }
    }

    interface View
}