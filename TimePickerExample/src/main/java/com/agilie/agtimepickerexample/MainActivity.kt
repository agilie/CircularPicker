package com.agilie.agtimepickerexample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.agilie.agtimepicker.ui.PickerPagerAdapter
import com.agilie.agtimepicker.ui.view.PickerPagerTransformer
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.agtimepicker.ui.view.TimePickerViewPager
import com.agilie.mobileeastergift.ViewsAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewsAdapter.AddNewViewsListener {


    //lateinit var adapter: ViewsAdapter
    var userList: List<TimePickerView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //userList = getViewsList()
        //adapter = ViewsAdapter(userList!!, this, this)
        //  recycler_view.adapter = adapter

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
        view_pager.adapter.count
    }

    override fun addNewView() {

    }

    private fun getViewsList(): List<TimePickerView> {
        return listOf(TimePickerView(this),
                TimePickerView(this))
    }

}
