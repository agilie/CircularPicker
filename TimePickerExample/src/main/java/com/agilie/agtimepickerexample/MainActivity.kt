package com.agilie.agtimepickerexample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.agilie.agtimepicker.ui.PickerPagerAdapter
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

        val pickerPagerAdapter = PickerPagerAdapter().apply {

            addView(TimePickerView(this@MainActivity).apply {
                setBackgroundColor(Color.parseColor("#00000000"))
            })

            addView(TimePickerView(this@MainActivity).apply {
                setBackgroundColor(Color.parseColor("#00000000"))
            })
        }

        pickerPagerAdapter.notifyDataSetChanged()
        view_pager.adapter = pickerPagerAdapter


    }

}
