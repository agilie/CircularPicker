package com.agilie.agtimepickerexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.agilie.agtimepicker.presenter.BaseBehavior


class MainActivity : AppCompatActivity() {

    val SWIPE_RADIUS_FACTOR = 0.6f

    private var touchState = BaseBehavior.TouchState.ROTATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*      view_pager.apply {

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

        pickerPagerAdapter.views.forEach {
            (it as TimePickerView).apply {
                touchListener = object : TimePickerView.TouchListener {

                    override fun onViewTouched(pointF: PointF, event: MotionEvent?) {
                        val pickerPoint = pointInCircle(pointF, center, radius) &&
                                !pointInCircle(pointF, center, (radius * SWIPE_RADIUS_FACTOR))

                        picker = pickerPoint
                        view_pager?.swipeEnable = !pickerPoint
                        view_pager?.onInterceptTouchEvent(event)
                    }
                }
            }
        }
    }

    fun pointInActionArea(pointF: PointF, view: TimePickerView) = pointInCircle(pointF, view.center, view.radius) &&
            !pointInCircle(pointF, view.center, (view.radius * SWIPE_RADIUS_FACTOR))

    }*/
    }
}
