package com.agilie.agtimepickerexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.agilie.agtimepicker.presenter.BehaviorWrapper
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.TimePickerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val timePickerView = findViewById(R.id.timePickerView) as TimePickerView
        timePickerView.setBehavior(BehaviorWrapper(timePickerView, object : TimePickerContract.Behavior.BehaviorConstructor {
            override fun countOfLaps() = 2

            override fun countOfValues() = 60

            override fun onValueCalculated(value: Int) {
                Log.d("valueTest", "$value")
            }
        }))
    }
}
