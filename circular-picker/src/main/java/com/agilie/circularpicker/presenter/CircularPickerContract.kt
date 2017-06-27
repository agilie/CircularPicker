package com.agilie.circularpicker.presenter

import com.agilie.circularpicker.presenter.picker.CircularPicker


interface CircularPickerContract {

    interface Behavior : CircularPicker {

        fun calculateValue(angle: Int): Int
        fun value(value: Int)

        interface ValueChangeListener {
            fun onValueChanged (value: Int)
        }
    }

    interface View
}