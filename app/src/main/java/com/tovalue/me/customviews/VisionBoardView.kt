package com.tovalue.me.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath
import com.tovalue.me.R


class VisionBoardView : View {
	
	private var color = Color.YELLOW
	private var size = 200f
	private var insideColor = Color.WHITE
	private var borderWidth = 2f
	private val mouthPath = Path()
	private var background = Color.TRANSPARENT
	
	constructor(context: Context) : super(context) {
		initView(context, null)
	}
	
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
		initView(context, attrs)
	}
	
	private fun initView(context: Context, attrs: AttributeSet?) {
		if (attrs != null) {
			val a = context.obtainStyledAttributes(attrs, R.styleable.VisionBoardShape)
			insideColor = a.getColor(R.styleable.VisionBoardShape_bottomViewCurveColor, Color.WHITE)
			background =
				a.getColor(R.styleable.VisionBoardShape_upperViewCurveColor, Color.TRANSPARENT)
			a.recycle()
		}
	}
	
	
	private fun drawCurvedShape(canvas: Canvas) {
		
		val paint = Paint()
		paint.color = color
		paint.style = Paint.Style.FILL
		setBackgroundColor(background)
		
		// 1
		mouthPath.moveTo(0f, size)
		// 2
		mouthPath.quadTo(size * 0f, size * 0.5f, size, size * 0.5f)
		mouthPath.lineTo(canvas.width - size, size * 0.5f)
		mouthPath.quadTo(canvas.width - 0.1f, size * 0.45f, canvas.width * 1f, 0f)
		mouthPath.lineTo(canvas.width * 1f, canvas.height * 1f)
		mouthPath.lineTo(0f, canvas.height * 1f)
		mouthPath.lineTo(0f, size)
		
		// 4
		paint.color = insideColor
		paint.strokeWidth = borderWidth
		paint.style = Paint.Style.FILL
		
		
		// 5
		canvas.drawPath(mouthPath, paint)
	}
	
	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		if (canvas != null)
			drawCurvedShape(canvas)
	}
	
}

class VisionBoardCard : CornerTreatment() {
	
	override fun getCornerPath(
		shapePath: ShapePath,
		angle: Float,
		interpolation: Float,
		radius: Float
	) {
		shapePath.reset(0f, -radius * interpolation, SHADOW_ANGLE, SHADOW_ANGLE - angle)
		shapePath.addArc(
			0f, -2 * radius * interpolation, 2 * radius * interpolation,
			0f,
			START_ANGLE,
			-angle
		)
	}
	
	companion object {
		const val START_ANGLE = 180f
		const val SHADOW_ANGLE = 270f
	}
}