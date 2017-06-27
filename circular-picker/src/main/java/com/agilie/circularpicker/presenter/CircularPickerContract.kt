package com.agilie.circularpicker.presenter

import com.agilie.circularpicker.presenter.picker.CircularPicker


interface CircularPickerContract {

    interface Behavior : CircularPicker {

        fun calculateValue(angle: Int): Int
        fun value(value: Int)

        interface ValueChangedListener {
            fun onValueChanged(value: Int)
        }

        interface ColorChangedListener {
            fun onColorChanged(r: Int, g: Int, b: Int)
        }
    }

    interface View
}