package com.tovalue.me.helper

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


@SuppressLint("Recycle")
fun getFilePathFromUri(contentUri: Uri, context: Context): String? {
	val proj = arrayOf(MediaStore.Audio.Media.DATA)
	val cursor: Cursor? = context.contentResolver.query(
		contentUri,
		proj, null, null, null
	)
	if (cursor != null) {
		val index: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
		cursor.moveToFirst()
		return cursor.getString(index)
	}
	return contentUri.path
}