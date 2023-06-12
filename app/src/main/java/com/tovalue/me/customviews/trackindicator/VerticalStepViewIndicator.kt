package com.tovalue.me.customviews.trackindicator

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import kotlin.jvm.JvmOverloads
import androidx.core.content.ContextCompat
import com.tovalue.me.R
import java.util.ArrayList

class VerticalStepViewIndicator @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val TAG_NAME = this.javaClass.simpleName

    //定义默认的高度   definition default height
    private val defaultStepIndicatorNum =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, resources.displayMetrics)
            .toInt()
    private var mCompletedLineHeight //完成线的高度     definition completed line height
            = 0f

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    var circleRadius //圆的半径  definition circle radius
            = 0f
        private set
    private var mCompleteIcon //完成的默认图片    definition default completed icon
            : Drawable? = null
    private var mAttentionIcon //正在进行的默认图片     definition default underway icon
            : Drawable? = null
    private var mDefaultIcon //默认的背景图  definition default unCompleted icon
            : Drawable? = null
    private var mCenterX //该View的X轴的中间位置
            = 0f
    private var mLeftY = 0f
    private var mRightY = 0f
    private var mStepNum = 0 //当前有几部流程    there are currently few step
    private var mLinePadding //两条连线之间的间距  definition the spacing between the two circles
            = 0f
    private var mCircleCenterPointPositionList //定义所有圆的圆心点位置的集合 definition all of circles center point list
            : MutableList<Float>? = null
    private var mUnCompletedPaint //未完成Paint  definition mUnCompletedPaint
            : Paint? = null
    private var mCompletedPaint //完成paint      definition mCompletedPaint
            : Paint? = null
    private var mUnCompletedLineColor = ContextCompat.getColor(
        getContext(),
        R.color.uncompleted_color
    ) //定义默认未完成线的颜色  definition mUnCompletedLineColor
    private var mCompletedLineColor = Color.WHITE //定义默认完成线的颜色      definition mCompletedLineColor
    private var mEffects: PathEffect? = null
    private var mComplectingPosition //正在进行position   underway position
            = 0
    private var mPath: Path? = null
    private var mOnDrawListener: OnDrawIndicatorListener? = null
    private var mRect: Rect? = null
    private var mHeight //这个控件的动态高度    this view dynamic height
            = 0
    private var mIsReverseDraw //is reverse draw this view;
            = false

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    fun setOnDrawListener(onDrawListener: OnDrawIndicatorListener?) {
        mOnDrawListener = onDrawListener
    }

    /**
     * init
     */
    private fun init() {
        mPath = Path()
        mEffects = DashPathEffect(floatArrayOf(8f, 8f, 8f, 8f), 1f)
        mCircleCenterPointPositionList = ArrayList() //初始化
        mUnCompletedPaint = Paint()
        mCompletedPaint = Paint()
        mUnCompletedPaint!!.isAntiAlias = true
        mUnCompletedPaint!!.color = mUnCompletedLineColor
        mUnCompletedPaint!!.style = Paint.Style.STROKE
        mUnCompletedPaint!!.strokeWidth = 2f
        mCompletedPaint!!.isAntiAlias = true
        mCompletedPaint!!.color = mCompletedLineColor
        mCompletedPaint!!.style = Paint.Style.STROKE
        mCompletedPaint!!.strokeWidth = 2f
        mUnCompletedPaint!!.pathEffect = mEffects
        mCompletedPaint!!.style = Paint.Style.FILL

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum
        //圆的半径  set mCircleRadius
        circleRadius = 0.28f * defaultStepIndicatorNum
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defaultStepIndicatorNum
        mCompleteIcon = ContextCompat.getDrawable(context, R.drawable.complted) //已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(context, R.drawable.attention) //正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(context, R.drawable.default_icon) //未完成的icon
        mIsReverseDraw = true //default draw
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i(TAG_NAME, "onMeasure")
        var width = defaultStepIndicatorNum
        mHeight = 0
        if (mStepNum > 0) {
            //dynamic measure VerticalStepViewIndicator height
            mHeight =
                (paddingTop + paddingBottom + circleRadius * 2 * mStepNum + (mStepNum - 1) * mLinePadding).toInt()
        }
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec))
        }
        setMeasuredDimension(width, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i(TAG_NAME, "onSizeChanged")
        mCenterX = (width / 2).toFloat()
        mLeftY = mCenterX - mCompletedLineHeight / 2
        mRightY = mCenterX + mCompletedLineHeight / 2
        for (i in 0 until mStepNum) {
            //reverse draw VerticalStepViewIndicator
            if (mIsReverseDraw) {
                mCircleCenterPointPositionList!!.add(mHeight - (circleRadius + i * circleRadius * 2 + i * mLinePadding))
            } else {
                mCircleCenterPointPositionList!!.add(circleRadius + i * circleRadius * 2 + i * mLinePadding)
            }
        }
        /**
         * set listener
         */
        if (mOnDrawListener != null) {
            mOnDrawListener!!.ondrawIndicator()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i(TAG_NAME, "onDraw")
        if (mOnDrawListener != null) {
            mOnDrawListener!!.ondrawIndicator()
        }
        mUnCompletedPaint!!.color = mUnCompletedLineColor
        mCompletedPaint!!.color = mCompletedLineColor

        //-----------------------画线-------draw line-----------------------------------------------
        for (i in 0 until mCircleCenterPointPositionList!!.size - 1) {
            //前一个ComplectedXPosition
            val preComplectedXPosition = mCircleCenterPointPositionList!![i]
            //后一个ComplectedXPosition
            val afterComplectedXPosition = mCircleCenterPointPositionList!![i + 1]
            if (i < mComplectingPosition) //判断在完成之前的所有点
            {
                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                if (mIsReverseDraw) {
                    canvas.drawRect(
                        mLeftY,
                        afterComplectedXPosition + circleRadius - 10,
                        mRightY,
                        preComplectedXPosition - circleRadius + 10,
                        mCompletedPaint!!
                    )
                } else {
                    canvas.drawRect(
                        mLeftY,
                        preComplectedXPosition + circleRadius - 10,
                        mRightY,
                        afterComplectedXPosition - circleRadius + 10,
                        mCompletedPaint!!
                    )
                }
            } else {
                if (mIsReverseDraw) {
                    mPath!!.moveTo(mCenterX, afterComplectedXPosition + circleRadius)
                    mPath!!.lineTo(mCenterX, preComplectedXPosition - circleRadius)
                    canvas.drawPath(mPath!!, mUnCompletedPaint!!)
                } else {
                    mPath!!.moveTo(mCenterX, preComplectedXPosition + circleRadius)
                    mPath!!.lineTo(mCenterX, afterComplectedXPosition - circleRadius)
                    canvas.drawPath(mPath!!, mUnCompletedPaint!!)
                }
            }
        }
        //-----------------------画线-------draw line-----------------------------------------------

        //-----------------------画图标-----draw icon-----------------------------------------------
        for (i in mCircleCenterPointPositionList!!.indices) {
            val currentComplectedXPosition = mCircleCenterPointPositionList!![i]
            mRect = Rect(
                (mCenterX - circleRadius).toInt(),
                (currentComplectedXPosition - circleRadius).toInt(),
                (mCenterX + circleRadius).toInt(),
                (currentComplectedXPosition + circleRadius).toInt()
            )
            if (i < mComplectingPosition) {
                mCompleteIcon!!.bounds = mRect!!
                mCompleteIcon!!.draw(canvas)
            } else if (i == mComplectingPosition && mCircleCenterPointPositionList!!.size != 1) {
                mCompletedPaint!!.color = Color.WHITE
                canvas.drawCircle(
                    mCenterX,
                    currentComplectedXPosition,
                    circleRadius * 1.1f,
                    mCompletedPaint!!
                )
                mAttentionIcon!!.bounds = mRect!!
                mAttentionIcon!!.draw(canvas)
            } else {
                mDefaultIcon!!.bounds = mRect!!
                mDefaultIcon!!.draw(canvas)
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    val circleCenterPointPositionList: List<Float>?
        get() = mCircleCenterPointPositionList

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    fun setStepNum(stepNum: Int) {
        mStepNum = stepNum
        requestLayout()
    }

    /**
     *
     * 设置线间距的比例系数 set linePadding proportion
     * @param linePaddingProportion
     */
    fun setIndicatorLinePaddingProportion(linePaddingProportion: Float) {
        mLinePadding = linePaddingProportion * defaultStepIndicatorNum
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    fun setComplectingPosition(complectingPosition: Int) {
        mComplectingPosition = complectingPosition
        requestLayout()
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    fun setUnCompletedLineColor(unCompletedLineColor: Int) {
        mUnCompletedLineColor = unCompletedLineColor
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    fun setCompletedLineColor(completedLineColor: Int) {
        mCompletedLineColor = completedLineColor
    }

    /**
     * is reverse draw 是否倒序画
     */
    fun reverseDraw(isReverseDraw: Boolean) {
        mIsReverseDraw = isReverseDraw
        invalidate()
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    fun setDefaultIcon(defaultIcon: Drawable?) {
        mDefaultIcon = defaultIcon
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    fun setCompleteIcon(completeIcon: Drawable?) {
        mCompleteIcon = completeIcon
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setAttentionIcon(attentionIcon: Drawable?) {
        mAttentionIcon = attentionIcon
    }

    /**
     * 设置对view监听
     */
    interface OnDrawIndicatorListener {
        fun ondrawIndicator()
    }

    init {
        init()
    }
}