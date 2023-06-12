package com.tovalue.me.util

import android.content.Context

open class DialogUtils {
	
	companion object {
		private var progressLoader: ProgressDialog? = null
		fun showDialog(
			context: Context?,
			isCancelable: Boolean,
		) {
			hideDialog()
			if (context != null) {
				try {
					progressLoader = ProgressDialog(context)
					progressLoader?.let { progressLoader ->
						progressLoader.setCanceledOnTouchOutside(true)
						progressLoader.setCancelable(isCancelable)
						progressLoader.show()
					}
					
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
		
		fun hideDialog() {
			if (progressLoader != null && progressLoader?.isShowing!!) {
				progressLoader = try {
					progressLoader?.dismiss()
					null
				} catch (e: Exception) {
					null
				}
			}
		}
	}
	
}