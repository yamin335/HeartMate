package com.tovalue.me.common.views

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.tovalue.me.R

class EditableKeyWacther internal constructor(
	private val currentView: EditText,
	private val previousView: EditText?
) : View.OnKeyListener {
	override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
		if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL &&
			currentView.id != R.id.first_digit_et && currentView.text.isEmpty()) {
			previousView!!.text = null
			previousView.requestFocus()
			return true
		}
		return false
	}
	
	
}