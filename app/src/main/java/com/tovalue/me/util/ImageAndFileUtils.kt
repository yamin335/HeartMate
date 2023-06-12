package com.tovalue.me.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageAndFileUtils {
    @Throws(Exception::class)
    fun saveBitmapFileIntoExternalStorageWithTitle(
        applicationContext: Context,
        bitmap: Bitmap,
        title: String
    ): File {

        val newImageFile = makeEmptyFileIntoExternalStorageWithTitle(applicationContext, title)
        val fileOutputStream = FileOutputStream(newImageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        return newImageFile
    }

    private fun makeEmptyFileIntoExternalStorageWithTitle(applicationContext: Context, title: String): File {
        //If your app is used on a device that runs Android 4.3 (API level 18) or lower,
        // then the array contains just one element,
        // which represents the primary external storage volume
        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
        val primaryExternalStorage = externalStorageVolumes[0]
        //path = "$primaryExternalStorage/$realDocId"

        //val root: String = Environment.getExternalStorageDirectory().getAbsolutePath()
        return File(primaryExternalStorage.absolutePath, title)
    }

//    fun fileFromBitmap(bitmap: Bitmap?, context: Context): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
//        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        String mediaPath = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), result, newFile.getName(), null)
//        //create a file to write bitmap customerMenu
////        val f = File(context.cacheDir, "JPEG_$timeStamp.jpg")
////        f.createNewFile()
//
//        val f = File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        )
//
//        //Convert bitmap to byte array
//        val os = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
//        val bitmapdata = os.toByteArray()
//
//        //write the bytes in file
//        val fos = FileOutputStream(f)
//        fos.write(bitmapdata)
//        fos.flush()
//        fos.close()
//
//        return f
//    }

    fun uriFromBitmap(bitmap: Bitmap, context: Context, name: String) =
        Uri.parse(saveBitmapFileIntoExternalStorageWithTitle(context, bitmap, name).absolutePath)
}