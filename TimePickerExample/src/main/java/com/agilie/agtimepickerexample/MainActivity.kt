package com.agilie.agtimepickerexample

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.agilie.agtimepicker.presenter.TimePickerContract
import com.agilie.agtimepicker.ui.view.PickerPagerTransformer
import com.agilie.agtimepicker.ui.view.TimePickerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.apply {
            view_pager.apply {
                pageMargin = -50
                clipChildren = false
                setPageTransformer(false, PickerPagerTransformer(context, 400))
            }


            view_pager.onAddView(TimePickerView(this@MainActivity).apply {
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
                        when (value) {
                            0 -> first_value.text = value.toString() + "0"
                            else -> first_value.text = value.toString()
                        }
                    }
                })
            })

            view_pager.onAddView(TimePickerView(this@MainActivity).apply {
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
                        when (value) {
                            0 -> second_value.text = value.toString() + "0"
                            else -> second_value.text = value.toString()
                        }
                    }
                }
            })

            setupScale()

            addPageListener()
        }
    }

    private fun addPageListener() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val scaleFactor = 1 - positionOffset * 0.3f
                val scrollX = -positionOffset * (first_value.width + textView.width)
                if (positionOffset > 0) {
                    //Scale
                    scaleView(scaleFactor)
                    //Translation
                    translationView(scrollX)
                    //Color
                    first_value.setTextColor(blendColors(resources.getColor(R.color.colorFirstCounter), resources.getColor((R.color.colorCounterBehind)), positionOffset))

                    second_value.apply {
                        scaleX = 0.7f + positionOffset * 0.3f
                        scaleY = 0.7f + positionOffset * 0.3f
                        setTextColor(blendColors(resources.getColor(R.color.colorCounterBehind), resources.getColor(R.color.colorSecondCounter), positionOffset))
                    }
                }
            }

            override fun onPageSelected(position: Int) {
            }

        })
    }

    private fun setupScale() {
        second_value.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }

        textView.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }
    }

    private fun translationView(scrollX: Float) {
        first_value.translationX = scrollX
        textView.translationX = scrollX
        second_value.translationX = scrollX
    }

    private fun scaleView(scaleFactor: Float) {
        first_value.apply {
            scaleX = scaleFactor
            scaleY = scaleFactor
        }
    }

    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio

        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }
}

