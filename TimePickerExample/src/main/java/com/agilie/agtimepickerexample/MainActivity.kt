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

        //first_value.setFactory { LayoutInflater.from(this).inflate(R.layout.first_counter, first_value, false) }
        //second_value.setFactory { LayoutInflater.from(this).inflate(R.layout.second_counter, second_value, false) }


        view_pager.apply {
            clipChildren = false
            setPageTransformer(false, PickerPagerTransformer(context, 400))
        }

        // Create picker views
        view_pager.onAddView(TimePickerView(this).PickerBehavior()
                .setGradient(intArrayOf(
                        Color.parseColor("#00EDE9"),
                        Color.parseColor("#0087D9"),
                        Color.parseColor("#8A1CC3")), 220)
                .setMaxLap(2)
                .setMaxValue(24)
                .setValueChangeListener(object : TimePickerContract.Behavior.ValueChangeListener {
                    override fun onValueChanged(value: Int) {
                        when (value) {
                            0 -> first_value.text = value.toString() + "0"
                            else -> first_value.text = value.toString()
                        }
                    }
                }).build())

        view_pager.onAddView(TimePickerView(this).PickerBehavior()
                .setGradient(intArrayOf(
                        Color.parseColor("#FF8D00"),
                        Color.parseColor("#FF0058"),
                        Color.parseColor("#920084")), 150)
                .setMaxValue(60)
                .setMaxLap(1)
                .setValueChangeListener(object : TimePickerContract.Behavior.ValueChangeListener {
                    override fun onValueChanged(value: Int) {
                        when (value) {
                            0 -> second_value.text = value.toString() + "0"
                            else -> second_value.text = value.toString()
                        }
                    }
                }).build())

        setupScale()

        addPageListener()
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
                    first_value.setTextColor(blendColors(Color.parseColor("#00DCE6"), Color.parseColor("#3A3861"), positionOffset))

                    second_value.apply {
                        scaleX = 0.7f + positionOffset * 0.3f
                        scaleY = 0.7f + positionOffset * 0.3f
                        setTextColor(blendColors(Color.parseColor("#3A3861"), Color.parseColor("#F5005C"), positionOffset))
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
