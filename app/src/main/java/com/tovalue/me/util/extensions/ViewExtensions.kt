package com.tovalue.me.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.tovalue.me.R
import java.lang.Math.hypot

/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.replaceFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    addToStack: Boolean = false
) {
    val ft = supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        .replace(containerViewId, fragment, tag)
    if (addToStack) { ft.addToBackStack(tag) }
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.addFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    addToStack: Boolean = false
) {
    val ft = supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        .add(containerViewId, fragment, tag)
    if (addToStack) { ft.addToBackStack(tag) }
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun <F : Fragment> F.replaceFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimatorRes enterAnimation: Int = R.animator.slide_in_left,
    @AnimatorRes exitAnimation: Int = R.animator.slide_out_left,
    @AnimatorRes popEnterAnimation: Int = R.animator.slide_out_right,
    @AnimatorRes popExitAnimation: Int = R.animator.slide_in_right,
    addToStack: Boolean = true
) {
    val ft = fragmentManager!!
        .beginTransaction()
        .setCustomAnimations(
            enterAnimation, exitAnimation,
            popEnterAnimation, popExitAnimation
        )
        .replace(containerViewId, fragment, tag)
    if (addToStack) {
        ft.addToBackStack(tag)
    }
    if (!fragmentManager!!.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun <F : Fragment> F.addFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimatorRes enterAnimation: Int = R.animator.slide_in_left,
    @AnimatorRes exitAnimation: Int = R.animator.slide_out_left,
    @AnimatorRes popEnterAnimation: Int = R.animator.slide_out_right,
    @AnimatorRes popExitAnimation: Int = R.animator.slide_in_right,
    addToStack: Boolean = true
) {
    val ft = fragmentManager!!
        .beginTransaction()
        .setCustomAnimations(
            enterAnimation, exitAnimation,
            popEnterAnimation, popExitAnimation
        )
        .add(containerViewId, fragment, tag)
    if (addToStack) {
        ft.addToBackStack(tag)
    }
    if (!fragmentManager!!.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun View.animateVisibility(visible: Int) {
    if (visible == View.VISIBLE) {
        visibility = View.VISIBLE
        alpha = 0f
        scaleX = 0f
        scaleY = 0f
    }
    val value = if (visible == View.VISIBLE) 1f else 0f
    animate()
        .alpha(value)
        .scaleX(value)
        .scaleY(value)
        .setDuration(300)
        .setInterpolator(OvershootInterpolator())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }
            
            override fun onAnimationEnd(animation: Animator) {
                if (visible == View.GONE)
                    visibility = View.GONE
                else
                    animate()
                        .setInterpolator(OvershootInterpolator())
                        .start()
            }
            
            override fun onAnimationCancel(animation: Animator) {
                if (visible == View.GONE) {
                    visibility = View.GONE
                }
            }
            
            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        .start()
}


fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this.context).load(imageUrl)
        .error(R.drawable.default_avatar)
        .into(this)
}

fun Activity.makeStatusBarTransparent() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        statusBarColor = Color.TRANSPARENT
    }
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}