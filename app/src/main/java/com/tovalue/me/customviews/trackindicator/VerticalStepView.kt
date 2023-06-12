package com.tovalue.me.customviews.trackindicator

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import kotlin.jvm.JvmOverloads

import androidx.core.content.ContextCompat
import com.tovalue.me.R


class VerticalStepView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), VerticalStepViewIndicator.OnDrawIndicatorListener {
    private var mTextContainer: RelativeLayout? = null
    private var mStepsViewIndicator: VerticalStepViewIndicator? = null
    private var mTexts: List<View>? = null
    private var mComplectingPosition = 0
    private var mUnComplectedTextColor =
        ContextCompat.getColor(getContext(), R.color.uncompleted_text_color) //定义默认未完成文字的颜色;
    private var mComplectedTextColor =
        ContextCompat.getColor(getContext(), android.R.color.black) //定义默认完成文字的颜色;
    private var mTextSize = 14 //default textSize
    private var mTextView: TextView? = null
    private fun init() {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.widget_vertical_stepsview, this)
        mStepsViewIndicator =
            rootView.findViewById<View>(R.id.steps_indicator) as VerticalStepViewIndicator
        mStepsViewIndicator!!.setOnDrawListener(this)
        mTextContainer = rootView.findViewById<View>(R.id.rl_text_container) as RelativeLayout
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 设置显示的文字
     *
     * @param texts
     * @return
     */
    fun setStepViewTexts(texts: List<View>?): VerticalStepView {
        mTexts = texts
        if (texts != null) {
            mStepsViewIndicator!!.setStepNum(mTexts!!.size)
        } else {
            mStepsViewIndicator!!.setStepNum(0)
        }
        return this
    }

    /**
     * 设置正在进行的position
     *
     * @param complectingPosition
     * @return
     */
    fun setStepsViewIndicatorComplectingPosition(complectingPosition: Int): VerticalStepView {
        mComplectingPosition = complectingPosition
        mStepsViewIndicator!!.setComplectingPosition(complectingPosition)
        return this
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    fun setStepViewUnComplectedTextColor(unComplectedTextColor: Int): VerticalStepView {
        mUnComplectedTextColor = unComplectedTextColor
        return this
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    fun setStepViewComplectedTextColor(complectedTextColor: Int): VerticalStepView {
        mComplectedTextColor = complectedTextColor
        return this
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    fun setStepsViewIndicatorUnCompletedLineColor(unCompletedLineColor: Int): VerticalStepView {
        mStepsViewIndicator!!.setUnCompletedLineColor(unCompletedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    fun setStepsViewIndicatorCompletedLineColor(completedLineColor: Int): VerticalStepView {
        mStepsViewIndicator!!.setCompletedLineColor(completedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    fun setStepsViewIndicatorDefaultIcon(defaultIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setDefaultIcon(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setCompleteIcon(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setAttentionIcon(attentionIcon)
        return this
    }

    /**
     * is reverse draw 是否倒序画
     *
     * @param isReverSe default is true
     * @return
     */
    fun reverseDraw(isReverSe: Boolean): VerticalStepView {
        mStepsViewIndicator!!.reverseDraw(isReverSe)
        return this
    }

    /**
     * set linePadding  proportion 设置线间距的比例系数
     *
     * @param linePaddingProportion
     * @return
     */
    fun setLinePaddingProportion(linePaddingProportion: Float): VerticalStepView {
        mStepsViewIndicator!!.setIndicatorLinePaddingProportion(linePaddingProportion)
        return this
    }

    /**
     * set textSize
     *
     * @param textSize
     * @return
     */
    fun setTextSize(textSize: Int): VerticalStepView {
        if (textSize > 0) {
            mTextSize = textSize
        }
        return this
    }

    override fun ondrawIndicator() {
        if (mTextContainer != null) {
            mTextContainer!!.removeAllViews() //clear ViewGroup
            val complectedXPosition = mStepsViewIndicator!!.circleCenterPointPositionList
            if (mTexts != null && complectedXPosition != null && complectedXPosition.size > 0) {
                for (i in mTexts!!.indices) {

                    var v = mTexts!![i]

                    v.y = complectedXPosition[i] - mStepsViewIndicator!!.circleRadius / 2
                    v.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

//                    mTextView = TextView(context)
//                    mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
////                    mTextView!!.text = mTexts!![i]
//                    mTextView!!.y =
//                    mTextView!!
//                    if (i <= mComplectingPosition) {
//                        mTextView!!.setTypeface(null, Typeface.BOLD)
//                        mTextView!!.setTextColor(mComplectedTextColor)
//                    } else {
//                        mTextView!!.setTextColor(mUnComplectedTextColor)
//                    }
                    mTextContainer!!.addView(v )
                }
            }
        }
    }

    init {
        init()
    }
}