package com.tovalue.me.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.tovalue.me.R


class CircularDotProgress : View {
	private var mSize // view size (both width and height), since this is a square view.
			= 0
	private var mDotCount = 0
	private var mDotRadius = 0
	private var mDotColor = 0
	private var mSpinDotRadius = 0
	private var mSpinDotColor = 0
	private var mSpinTailCount = 0
	private var mSpinInterval = 0
	private var mSpinProgress = 0
	
	/**
	 * @return true if spinning
	 */
	var isSpinning = false
		private set
	private var mProgress = 0
	private var mAnimProgress = 0
	private var mAnimDuration = DEFAULT_PROGRESS_ANIM_DURATION
	private var mAnimInterval = 0
	private var mIsAnimProgress = false
	private val mDotPaint = Paint()
	private val mDotSpinPaint = Paint()
	private val mDotTrackPaint = Paint()
	private var mDotCenterList: ArrayList<PointF>? = null
	private var mSpinTailRadiusList: ArrayList<Int>? = null
	
	constructor(context: Context) : super(context) {
		initView(context, null)
	}
	
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
		initView(context, attrs)
	}
	
	/**
	 * start spin
	 */
	fun startSpin() {
		if (!isSpinning) {
			mIsAnimProgress = false
			isSpinning = true
			postInvalidate()
		}
	}
	
	/**
	 * stop spin
	 */
	fun stopSpin() {
		isSpinning = false
		mSpinProgress = 0
	}
	
	/**
	 * set progress.
	 * use [.setProgress] to show progress without animation
	 * @param progress range [0-360]
	 */
	fun setProgressWithAnim(progress: Int) {
		mProgress = convertProgress(progress)
		isSpinning = false
		mIsAnimProgress = true
		mAnimProgress = 0
		mAnimInterval = if (mProgress == 0) 0 else mAnimDuration / mProgress
		postInvalidate()
	}
	/**
	 * @return current progress, range [0-360]
	 */
	/**
	 * set progress.
	 * use [.setProgressWithAnim] to show progress with animation
	 * @param progress range [0-360]
	 */
	var progress: Int
		get() = mProgress * 360 / mDotCount
		set(progress) {
			val p = convertProgress(progress)
			if (mProgress != p) {
				mProgress = p
				postInvalidate()
			}
		}
	
	// =============================================
	// private and override methods
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		
		// make view square
		val width = measuredWidth
		val height = measuredHeight
		val widthWithoutPadding = width - paddingLeft - paddingRight
		val heightWithoutPadding = height - paddingTop - paddingBottom
		mSize = if (widthWithoutPadding > heightWithoutPadding) {
			heightWithoutPadding
		} else {
			widthWithoutPadding
		}
		setMeasuredDimension(
			mSize + paddingLeft + paddingRight,
			mSize + paddingTop + paddingBottom
		)
	}
	
	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		if (w == 0 || h == 0) {
			return
		}
		if (oldw == w && oldh == h) {
			return
		}
		resetDotCenterList()
	}
	
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		for (i in 0 until mDotCenterList!!.size) {
			val p = mDotCenterList!![i]
			var radius: Int
			var paint: Paint
			if (isSpinning) {
				radius = getSpinRadius(i)
				paint = if (radius != mDotRadius) mDotSpinPaint else mDotPaint
			} else if (mIsAnimProgress) {
				radius = mDotRadius
				paint = if (i >= mAnimProgress) mDotTrackPaint else mDotPaint
			} else {
				radius = mDotRadius
				paint = if (i >= mProgress) mDotTrackPaint else mDotPaint
			}
			canvas.drawCircle(p.x, p.y, radius.toFloat(), paint)
		}
		if (isSpinning) {
			scheduleDrawSpin()
		} else if (mIsAnimProgress) {
			scheduleDrawProgress()
		}
	}
	
	private fun initView(context: Context, attrs: AttributeSet?) {
		val spinSpeed: Int
		if (attrs != null) {
			val a = context.obtainStyledAttributes(attrs, R.styleable.DotCircle)
			mDotCount = a.getInteger(R.styleable.DotCircle_dotCount, DEFAULT_DOT_COUNT)
			mDotColor = a.getColor(R.styleable.DotCircle_dotColor, DEFAULT_DOT_COLOR)
			mDotRadius = a.getDimensionPixelOffset(R.styleable.DotCircle_dotRadius, 0)
			mSpinDotRadius =
				a.getDimensionPixelOffset(R.styleable.DotCircle_dotSpinRadius, mDotRadius * 2)
			mSpinDotColor = a.getColor(R.styleable.DotCircle_dotSpinColor, mDotColor)
			mSpinTailCount = a.getInteger(R.styleable.DotCircle_dotSpinTailCount, mDotCount / 6)
			mAnimDuration = a.getInteger(
				R.styleable.DotCircle_dotProgressAnimDuration,
				DEFAULT_PROGRESS_ANIM_DURATION
			)
			val progress = a.getInteger(R.styleable.DotCircle_dotProgress, 0)
			mProgress = convertProgress(progress)
			spinSpeed = a.getInt(R.styleable.DotCircle_dotSpinSpeed, DEFAULT_SPIN_SPEED)
			a.recycle()
		} else {
			mDotCount = DEFAULT_DOT_COUNT
			mDotColor = DEFAULT_DOT_COLOR
			mSpinDotColor = mDotColor
			mSpinTailCount = mDotCount / 6
			spinSpeed = DEFAULT_SPIN_SPEED
		}
		mSpinInterval = 360 * 1000 / (spinSpeed * mDotCount)
		if (mSpinTailCount >= mDotCount) {
			mSpinTailCount = mDotCount - 1
		}
		if (mSpinTailCount < 0) {
			mSpinTailCount = 0
		}
		mSpinTailRadiusList = ArrayList(mSpinTailCount)
		initSpinRadius()
		resetPaint()
	}
	
	private fun resetPaint() {
		mDotPaint.color = mDotColor
		mDotPaint.isAntiAlias = true
		mDotTrackPaint.color = mDotColor
		mDotTrackPaint.alpha = 64
		mDotTrackPaint.isAntiAlias = true
		mDotSpinPaint.color = mSpinDotColor
		mDotSpinPaint.isAntiAlias = true
	}
	
	private fun resetDotCenterList() {
		if (mDotCenterList == null) {
			mDotCenterList = ArrayList(mDotCount)
		} else {
			mDotCenterList!!.clear()
		}
		val radius = mSize / 2f
		val centerX = paddingLeft + radius
		val centerY = paddingTop + radius
		if (mDotRadius == 0) {
			val perimeter = (2 * Math.PI * radius).toInt()
			mDotRadius = perimeter / mDotCount / 5
			initSpinRadius()
		}
		val r = radius - Math.max(mDotRadius, mSpinDotRadius)
		val angle = 360.0 / mDotCount * (Math.PI / 180)
		for (i in 0 until mDotCount) {
			val x = centerX + (r * Math.sin(i * angle)).toFloat()
			val y = centerY - (r * Math.cos(i * angle)).toFloat()
			mDotCenterList!!.add(PointF(x, y))
		}
	}
	
	private fun scheduleDrawSpin() {
		mSpinProgress += 1
		if (mSpinProgress >= mDotCount) {
			mSpinProgress = 0
		}
		postInvalidateDelayed(mSpinInterval.toLong())
	}
	
	private fun scheduleDrawProgress() {
		mAnimProgress += 1
		if (mAnimProgress > mProgress) {
			mAnimProgress = mProgress
			mIsAnimProgress = false
		} else {
			postInvalidateDelayed(mAnimInterval.toLong())
		}
	}
	
	private fun initSpinRadius() {
		if (mDotRadius == 0) {
			return
		}
		if (mSpinDotRadius == 0) {
			mSpinDotRadius = mDotRadius * 2
		}
		val delta = (mSpinDotRadius - mDotRadius) / (mSpinTailCount + 1)
		for (i in 1..mSpinTailCount) {
			val radius = mSpinDotRadius - i * delta
			mSpinTailRadiusList!!.add(radius)
		}
	}
	
	private fun getSpinRadius(index: Int): Int {
		if (index == mSpinProgress) {
			return mSpinDotRadius
		}
		var m = mSpinProgress - index
		if (m < 0) {
			m = m + mDotCount
		}
		return if (m > mSpinTailCount) {
			mDotRadius
		} else mSpinTailRadiusList!![m - 1]
	}
	
	/**
	 * convert degree progress to dot count
	 * @param degreeProgress range [0-360].
	 * less than 0 will be treat as 0;
	 * greater than 360 will be treat as 360.
	 * @return dot count progress
	 */
	private fun convertProgress(degreeProgress: Int): Int {
		var degreeProgress = degreeProgress
		if (degreeProgress > 360) {
			degreeProgress = 360
		}
		if (degreeProgress < 0) {
			degreeProgress = 0
		}
		return mDotCount * degreeProgress / 360
	}
	
	companion object {
		private const val DEFAULT_DOT_COUNT = 24
		private val DEFAULT_DOT_COLOR: Int = Color.WHITE
		private const val DEFAULT_SPIN_SPEED = 240 // in degree angle
		private const val DEFAULT_PROGRESS_ANIM_DURATION = 600 // in millis
	}
}