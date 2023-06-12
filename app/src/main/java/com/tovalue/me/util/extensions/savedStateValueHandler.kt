package com.tovalue.me.util.extensions

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> savedStateValueHandler(handle: SavedStateHandle, key: String): ReadWriteProperty<Any?, T?> {
	return (
			object : ReadWriteProperty<Any?, T?> {
				override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
					return handle[key]
				}
				
				override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
					handle[key] = value
				}
			})
}