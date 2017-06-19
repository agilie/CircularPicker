package com.agilie.agtimepickerexample

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.agilie.agtimepicker.presenter.BaseBehavior
import com.agilie.agtimepicker.ui.PickerPagerAdapter
import com.agilie.agtimepicker.ui.view.PickerPagerTransformer
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.mobileeastergift.ViewsAdapter
import com.agilie.volumecontrol.pointInCircle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewsAdapter.AddNewViewsListener {

    //                if (!
//                pointInActionArea()
//                        ) {
//                    touchState = BaseBehavior.TouchState.SWIPE
//                } else {
//                    touchState = BaseBehavior.TouchState.ROTATE
//                }

    val SWIPE_RADIUS_FACTOR = 0.6f

    //lateinit var adapter: ViewsAdapter
    var userList: List<TimePickerView>? = null

    private var touchState = BaseBehavior.TouchState.ROTATE

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

        pickerPagerAdapter.views.forEach { it.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!pointInActionArea(PointF(event.x, event.y), v as TimePickerView)) {
                        touchState = BaseBehavior.TouchState.SWIPE
                        view_pager.swipeEnable = true
                    } else {
                        touchState = BaseBehavior.TouchState.ROTATE
                        view_pager.swipeEnable = false

                    }
                }

            }

            true
        } }


    }

    fun pointInActionArea(pointF: PointF, view: TimePickerView) = pointInCircle(pointF, view.center, view.radius) &&
            !pointInCircle(pointF, view.center, (view.radius * SWIPE_RADIUS_FACTOR))

    override fun addNewView() {

    }

    private fun getViewsList(): List<TimePickerView> {
        return listOf(TimePickerView(this),
                TimePickerView(this))
    }

}
