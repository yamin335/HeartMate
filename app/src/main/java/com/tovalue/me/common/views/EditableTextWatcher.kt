package com.tovalue.me.common.views

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tovalue.me.R
import com.tovalue.me.util.Utils.hideKeyboard

class EditableTextWatcher internal constructor(
	private val currentView: View,
	private val nextView: View?
) : TextWatcher {
	override fun afterTextChanged(editable: Editable) {
		val text = editable.toString()
		when (currentView.id) {
			R.id.first_digit_et -> if (text.length == 1) nextView!!.requestFocus()
			R.id.second_digit_et -> if (text.length == 1) nextView!!.requestFocus()
			R.id.third_digit_et -> if (text.length == 1) nextView!!.requestFocus()
			R.id.fourth_digit_et -> if (text.length == 1) nextView!!.requestFocus()
			R.id.fifth_digit_et -> if (text.length == 1) nextView!!.requestFocus()
			R.id.sixth_digit_et -> if (text.length == 1) nextView?.let { hideKeyboard(it.context as Activity) }
		}
	}
	override fun beforeTextChanged(char: CharSequence, start: Int, end: Int, last: Int) {}
	override fun onTextChanged(char: CharSequence, start: Int, end: Int, last: Int) {
	}
	
}