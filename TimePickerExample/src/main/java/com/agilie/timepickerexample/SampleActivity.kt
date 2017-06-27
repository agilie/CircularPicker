package com.agilie.timepickerexample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.agilie.circularpicker.presenter.CircularPickerContract
import com.agilie.circularpicker.ui.view.CircularPickerView
import com.agilie.circularpicker.ui.view.PickerPagerTransformer
import kotlinx.android.synthetic.main.activity_main.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.apply {
            clipChildren = false
            setPageTransformer(false, PickerPagerTransformer(context, 300))
        }

        view_pager.onAddView(CircularPickerView(this@SampleActivity).apply {
            colors = (intArrayOf(
                    Color.parseColor("#00EDE9"),
                    Color.parseColor("#0087D9"),
                    Color.parseColor("#8A1CC3")))
            gradientAngle = 220
            maxLapCount = 2
            currentValue = 13
            maxValue = 24
            centeredTextSize = 60f
            centeredText = "Hours"
            valueChangedListener = (object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {
                    when (value) {
                        0 -> hoursTextView.text = value.toString() + "0"
                        else -> hoursTextView.text = value.toString()
                    }
                }
            })
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    hoursTextView.setTextColor(Color.rgb(r, g, b))
                }
            })
        })

        view_pager.onAddView(CircularPickerView(this@SampleActivity).apply {
            colors = (intArrayOf(
                    Color.parseColor("#FF8D00"),
                    Color.parseColor("#FF0058"),
                    Color.parseColor("#920084")))
            gradientAngle = 150
            maxValue = 60
            currentValue = 24
            maxLapCount = 1
            centeredTextSize = 60f
            centeredText = "Minutes"
            valueChangedListener = object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {
                    when (value) {
                        0 -> minutesTextView.text = value.toString() + "0"
                        else -> minutesTextView.text = value.toString()
                    }
                }
            }
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    minutesTextView.setTextColor(Color.rgb(r, g, b))
                }
            })
        })
        val tf = Typeface.createFromAsset(this.assets, "OpenSans-ExtraBold.ttf")
        hoursTextView.apply {
            typeface = tf
            text = "13"
        }

        minutesTextView.apply {
            typeface = tf
            text = "24"

        }
        setupScale()
        addPageListener()
    }

    private fun addPageListener() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // empty
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val scaleFactor = 1 - positionOffset * 0.3f
                val scrollX = -positionOffset * (hoursTextView.width + colonTextView.width)
                if (positionOffset > 0) {
                    // Scale
                    scaleView(scaleFactor)
                    // Translation
                    translationView(scrollX)
                    // ColorZ
                    hoursTextView.setTextColor(blendColors(resources.getColor(R.color.colorFirstCounter), resources.getColor((R.color.colorCounterBehind)), positionOffset))

                    minutesTextView.apply {
                        scaleX = 0.7f + positionOffset * 0.3f
                        scaleY = 0.7f + positionOffset * 0.3f
                        setTextColor(blendColors(resources.getColor(R.color.colorCounterBehind), resources.getColor(R.color.colorSecondCounter), positionOffset))
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                // empty
            }

        })
    }

    private fun setupScale() {
        minutesTextView.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }

        colonTextView.apply {
            scaleX = 0.7f
            scaleY = 0.7f
        }
    }

    private fun translationView(scrollX: Float) {
        hoursTextView.translationX = scrollX
        colonTextView.translationX = scrollX
        minutesTextView.translationX = scrollX
    }

    private fun scaleView(scaleFactor: Float) {
        hoursTextView.apply {
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

