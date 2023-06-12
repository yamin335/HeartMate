package com.tovalue.me.common

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.provider.SimPhonebookContract
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

fun Activity.showToast(text: String) {
	Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(@StringRes resId: Int) {
	showToast(getString(resId))
}

fun Fragment.showToast(text: String) {
	Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
}

val EditText.trimmedText: String
	get() = text.trim().toString()

val View.activity: AppCompatActivity?
get() {
	var context = context
	while (context is ContextWrapper) {
		if (context is AppCompatActivity) {
			return context
		}
		context = context.baseContext
	}
	return null
}

fun Drawable.applyTint(@ColorInt tintColor: Int?): Drawable {
	if (tintColor == null) return this
	
	val tintedDrawable = DrawableCompat.wrap(this).mutate()
	DrawableCompat.setTint(tintedDrawable, tintColor)
	DrawableCompat.setTintMode(tintedDrawable, PorterDuff.Mode.SRC_IN)
	return tintedDrawable
}