package com.agilie.agtimepickerexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        view_pager.onAddView(TimePickerView(this))
        view_pager.onAddView(TimePickerView(this))

    }

}
