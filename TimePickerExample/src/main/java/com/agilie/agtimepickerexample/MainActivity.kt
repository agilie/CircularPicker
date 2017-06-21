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

        view_pager.onAddView(TimePickerView(this).PickerBehavior()
                .setGradient(intArrayOf(
                        Color.parseColor("#00EDE9"),
                        Color.parseColor("#0087D9"),
                        Color.parseColor("#8A1CC3")), 45)
                .setMaxLap(2)
                .setMaxValue(24)
                .setValueChangeListener(object : TimePickerContract.Behavior.ValueChangeListener {
                    override fun onValueChanged(value: Int) {
                        Log.d("valTest", "Hour $value \n" +
                                "__________________________")
                    }
                }).build())

        view_pager.onAddView(TimePickerView(this).PickerBehavior()
                .setGradient(intArrayOf(
                        Color.parseColor("#FF8D00"),
                        Color.parseColor("#FF0058"),
                        Color.parseColor("#920084")), 45)
                .setMaxValue(60)
                .setMaxLap(1)
                .setValueChangeListener(object : TimePickerContract.Behavior.ValueChangeListener {
                    override fun onValueChanged(value: Int) {
                        Log.d("valTest", "Hour $value \n" +
                                "__________________________")
                    }
                }).build())


    }

}
