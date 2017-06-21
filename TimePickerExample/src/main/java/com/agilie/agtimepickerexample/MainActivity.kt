package com.agilie.agtimepickerexample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.PickerPagerTransformer
import com.agilie.agtimepicker.ui.view.TimePickerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.apply {
            pageMargin = -50
            clipChildren = false
            setPageTransformer(false, PickerPagerTransformer(context))
        }

        view_pager.onAddView(TimePickerView(this).apply {
            colors = (intArrayOf(
                    Color.parseColor("#00EDE9"),
                    Color.parseColor("#0087D9"),
                    Color.parseColor("#8A1CC3")))
            gradientAngle = 220
            maxLapCount = 2
            maxValue = 24
            centeredTextSize = 60f
            centeredText = "Hours"
            valueChangeListener = (object : TimePickerContract.Behavior.ValueChangeListener {
                override fun onValueChanged(value: Int) {
                    Log.d("valTest", "Hour $value \n" +
                            "__________________________")
                }
            })
        })

        view_pager.onAddView(TimePickerView(this).apply {
            colors = (intArrayOf(
                    Color.parseColor("#FF8D00"),
                    Color.parseColor("#FF0058"),
                    Color.parseColor("#920084")))
            gradientAngle = 150
            maxValue = 60
            maxLapCount = 1
            centeredTextSize = 60f
            centeredText = "Minutes"
            valueChangeListener = object : TimePickerContract.Behavior.ValueChangeListener {
                override fun onValueChanged(value: Int) {
                    Log.d("valTest", "Minutes $value \n" +
                            "__________________________")
                }
            }
        })


    }

}
