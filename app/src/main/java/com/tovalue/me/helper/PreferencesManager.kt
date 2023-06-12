package com.tovalue.me.helper

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor() {
	fun setStringValue(key: String?, value: String?) {
		mPref!!.edit()
			.putString(key, value)
			.apply()
	}
	
	fun setIntValue(key: String?, value: Int) {
		mPref!!.edit()
			.putInt(key, value)
			.apply()
	}
	
	fun setLongValue(key: String?, value: Long) {
		mPref!!.edit()
			.putLong(key, value)
			.apply()
	}
	
	fun setBooleanValue(key: String?, value: Boolean) {
		mPref!!.edit()
			.putBoolean(key, value)
			.apply()
	}
	
	fun setStringSet(
		key: String?,
		value: Set<String?>?
	) {
		remove(key)
		mPref!!.edit()
			.putStringSet(key, value)
			.apply()
	}
	
	fun getLongValue(key: String?): Long {
		return mPref!!.getLong(key, 0)
	}
	
	fun getStringSet(key: String?): Set<String>? {
		return mPref!!.getStringSet(key, null)
	}
	
	fun getStringValue(key: String?): String? {
		return mPref!!.getString(key, "")
	}
	
	fun getIntValue(key: String?): Int {
		return mPref!!.getInt(key, 0)
	}
	
	fun getBooleanValue(key: String?): Boolean {
		return mPref!!.getBoolean(key, false)
	}
	
	fun remove(key: String?) {
		mPref!!.edit()
			.remove(key)
			.apply()
	}
	
	fun clear(): Boolean {
		return mPref!!.edit()
			.clear()
			.commit()
	}

	fun contain(key:String):Boolean{
	return mPref!!.contains(key)
	}
	
	companion object {
		const val PREFERENCES_FILE = "BingoPreferenceFile"
		private var sInstance: PreferencesManager? = null
		private var mPref: SharedPreferences? = null
		
		@Synchronized
		fun getInstance(context: Context): PreferencesManager? {
			if (sInstance == null) {
				mPref = context.getSharedPreferences(
					PREFERENCES_FILE,
					Context.MODE_PRIVATE
				)
				sInstance = PreferencesManager()
			}
			return sInstance
		}
	}
}