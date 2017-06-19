package com.agilie.agtimepickerexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.mobileeastergift.ViewsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewsAdapter.AddNewViewsListener {


    lateinit var adapter: ViewsAdapter
    var userList: List<TimePickerView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = getViewsList()
        adapter = ViewsAdapter(userList!!, this, this)
        recycler_view.adapter = adapter

    }

    override fun addNewUser() {

    }

    private fun getViewsList(): List<TimePickerView> {
        return listOf(TimePickerView(this),
                TimePickerView(this))
    }

}
