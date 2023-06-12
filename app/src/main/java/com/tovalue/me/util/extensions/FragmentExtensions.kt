package com.tovalue.me.util.extensions

import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Fragment.useAdjustNothing() = setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

fun Fragment.useAdjustPan() = setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

fun Fragment.useAdjustResize() = setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

private fun Fragment.setSoftInputMode(flags: Int) {
	(activity ?: return).window.setSoftInputMode(flags)
}

fun Fragment.clearBackStack() {
	if (activity != null) {
			activity!!.supportFragmentManager.popBackStack(
				null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE
			)
	}
}

fun Fragment.updateStatusBarBackgroundColor(color: String) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		try {
			requireActivity().window.statusBarColor = Color.parseColor(color)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}