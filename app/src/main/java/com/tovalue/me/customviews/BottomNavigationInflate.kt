package com.tovalue.me.customviews

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import com.tovalue.me.R
import kotlin.properties.Delegates

class BottomNavigationInflate : BottomNavigationView {
    private val LOG_TAG = "BottomNavigationCircles"

    private var currentNavigationItemId = -1
    private var currentCircleId = -1
    private val menuViewGroupId = View.generateViewId()

    private lateinit var rootLayout: RelativeLayout
    private var disabledColor =
        ContextCompat.getColor(context, android.R.color.black)
    private var enabledColor = android.R.color.black
    private var textColor by Delegates.notNull<Int>()

    var backgroundShape = Shape.Circle
    private var customBackgroundDrawable = -1
    var circleColor = Color.GREEN
    var darkIcon = false
        set(value) {
            field = value
            updateEnabledColor()
        }

    enum class Shape(val value: Int) {
        Circle(0),
        RoundedRectangle(1);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value }
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr) {
            init(attrs)
        }

    private fun init(attrs: AttributeSet? = null) {
        getColors(attrs)
        getBackgroundDrawable(attrs)
        setupRootLayout()
        setupListener()
        setupClipping()
        selectFirstItem()
    }

    private fun getColors(attrs: AttributeSet?) {
        circleColor = getAttributeColorOrDefault(attrs)
        val textView = TextView(context)
        textColor = textView.currentTextColor
    }


    private fun getAttributeColorOrDefault(attrs: AttributeSet?): Int {
        var color: Int

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.BottomNavigationCircles, 0, 0
        ).apply {
            try {
                color = getInteger(
                    R.styleable.BottomNavigationCircles_circleColor,
                    ContextCompat.getColor(context, android.R.color.black)
                )
                darkIcon = getBoolean(
                    R.styleable.BottomNavigationCircles_darkIcon,
                    false
                )
            } finally {
                updateEnabledColor()
                recycle()
            }
        }

        return color
    }



    private fun getBackgroundDrawable(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.BottomNavigationCircles, 0, 0
        ).apply {
            try {
                backgroundShape = Shape.getByValue(
                    getInt(
                        R.styleable.BottomNavigationCircles_backgroundShape,
                        0
                    )
                )!!
                customBackgroundDrawable = getResourceId(
                    R.styleable.BottomNavigationCircles_customBackgroundShape,
                    -1
                )
            } finally {
                recycle()
            }
        }
    }

    private fun setupRootLayout() {
        val menuViewGroup = getChildAt(0) as BottomNavigationMenuView
        menuViewGroup.id = menuViewGroupId
        rootLayout = RelativeLayout(context)
        removeView(menuViewGroup)
        rootLayout.addView(menuViewGroup)
        addView(rootLayout)
    }

    private fun setupListener() {
        setOnItemSelectedListener {
            return@setOnItemSelectedListener animateBottomIcon(it.itemId)
        }
    }

    private fun setupClipping() {
        viewTreeObserver.addOnGlobalLayoutListener {
            clipChildren = false
            rootLayout.clipChildren = false
            findViewById<BottomNavigationMenuView>(menuViewGroupId).clipChildren = false

            disableClipOnParents(this)
        }
    }

    private fun disableClipOnParents(view: View) {
        if (view is ViewGroup) {
            view.clipChildren = false
        }

        if (view.parent is View) {
            disableClipOnParents(view.parent as View)
        }
    }

    private fun selectFirstItem() {
        if (
            rootLayout.childCount > 0 &&
            ((rootLayout.getChildAt(0)) as BottomNavigationMenuView).childCount > 0
        ) {
            val navigationItemView =
                (
                    (rootLayout.getChildAt(0) as BottomNavigationMenuView)
                        .getChildAt(0) as NavigationBarItemView
                )

            navigationItemView.viewTreeObserver.addOnGlobalLayoutListener {
                animateBottomIcon(selectedItemId)
            }
        }
    }

    private fun updateEnabledColor() {
//        enabledColor = if (darkIcon) Color.BLACK else Color.WHITE
        enabledColor =  Color.BLACK
    }

    private fun animateBottomIcon(itemId: Int): Boolean {
        if (itemId != currentNavigationItemId) {
            val itemView = getNavigationBarItemView(itemId)
            val icon = getAppCompatImageView(itemView)
            disableClipOnParents(icon)
            val subText = getSubTextView(itemView)
            val animatorSet = AnimatorSet()

            setSubTextStyle(subText)


            if (currentNavigationItemId != -1) {
                val currentItemView = getNavigationBarItemView(currentNavigationItemId)
                val currentView = getAppCompatImageView(currentItemView)


                currentView.drawable.setTint(Color.BLACK)
                val cv =  currentView.layoutParams
                cv.height =80
                cv.width = 80

                currentView.layoutParams = cv

                animatorSet.playTogether(
                    buildTranslateIconAnimator(currentView, -(height / 4).toFloat(), 0f),
                    buildTintAnimator(currentView, enabledColor, disabledColor)
                )

            }

            findViewById<BottomNavigationMenuView>(menuViewGroupId).bringToFront()

            animatorSet.playTogether(
                buildTranslateIconAnimator(icon, 0f, -(height / 4).toFloat()),

                buildTintAnimator(icon, disabledColor, enabledColor)
            )

            currentNavigationItemId = itemId
            animatorSet.start()
        }

        return true
    }

    private fun getNavigationBarItemView(itemId: Int): NavigationBarItemView {
        return findViewById(itemId)
    }

    private fun getAppCompatImageView(itemView: NavigationBarItemView): AppCompatImageView {
        return itemView.findViewById(
            com.google.android.material.R.id.navigation_bar_item_icon_view
        )
    }

    private fun getSubTextView(itemView: NavigationBarItemView): TextView {
        return itemView.findViewById(
            com.google.android.material.R.id.navigation_bar_item_large_label_view
        )
    }

    private fun setSubTextStyle(textView: TextView) {
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.setTextColor(textColor)
    }

    private fun buildTranslateIconAnimator(currentView: View, from: Float, to: Float):
            ValueAnimator? {

        if(currentView.height <100){

            val layoutParams: ViewGroup.LayoutParams = currentView.getLayoutParams()
            layoutParams.height = currentView.height+30
            layoutParams.width = currentView.width +30
            currentView.setLayoutParams(layoutParams)

        }else{

            val layoutParams: ViewGroup.LayoutParams = currentView.getLayoutParams()
            layoutParams.height = currentView.height-30
            layoutParams.width = currentView.width-30
            currentView.setLayoutParams(layoutParams)
        }


        val anim = ValueAnimator.ofInt(200, -100)


        anim.setDuration(500)

        return anim
        }



    private fun buildTintAnimator(currentView: AppCompatImageView, from: Int, to: Int):
        ValueAnimator {
            val animateTint = ValueAnimator.ofArgb(from, to)
            animateTint.duration = 500
            animateTint.addUpdateListener {

                currentView.drawable.setTint(it.animatedValue as Int)
            }

            return animateTint
        }



}
