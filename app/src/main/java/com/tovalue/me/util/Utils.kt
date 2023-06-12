package com.tovalue.me.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Insets
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tovalue.me.BuildConfig
import com.tovalue.me.R
import com.tovalue.me.common.trimmedText
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object Utils {

    const val yearT_Month_day = "EE,MMM dd"
    const val month_day_year = "yyyy-mm-dd"
    const val hour_min_second = "HH:mm:ss"
    const val hourT_min_second = "hh:mm a"


    @JvmStatic
    fun hideTopBar(context: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window: Window = context.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            window.navigationBarColor =
                ContextCompat.getColor(context, R.color.main)
//			window.setBackgroundDrawableResource(R.drawable.app_bg)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            context.requestWindowFeature(Window.FEATURE_NO_TITLE)
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

/*	We can remove the logic and this dialog if we create separate DialogFragment*/

    @JvmStatic
    fun showCustomViewAlertDialog(
        context: Context,
        title: String,
        message: String?,
        viewLayout: Int,
        viewDataListener: AlertViewDataListener
    ) {
        try {
            val builder =
                AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(viewLayout, null)
            builder.setView(view)
            builder.setTitle(title)
            builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Confirm") { dialog, which ->
                }
                .setNegativeButton("Cancel", null)
            val alert = builder.create()
            alert.setCancelable(false)
            alert.show()

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val email = view.findViewById(R.id.email_et) as EditText
                if (isValidEmail(email.trimmedText)) {
                    viewDataListener.onDismiss(email.trimmedText)
                    alert.dismiss()
                } else {
                    Toast.makeText(context, "Email is not Valid", Toast.LENGTH_SHORT).show()
                }
            }

            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                alert.dismiss()
            }

            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.getColor(R.color.dark_sky))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.getColor(R.color.black))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun showGenericAlertDialog(
        context: Context,
        title: String,
        message: String?,
        alertActionListener: AlertActionListener
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Confirm") { dialog, id ->
                    alertActionListener.onAlertDismissWithOK()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    alertActionListener.onAlertDismissWithCancel()
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.setCancelable(false)
            alert.show()
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.getColor(R.color.dark_sky))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.getColor(R.color.black))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun extractErrorMsg(errorBody: ResponseBody): String {
        val str = "Something wrong"
        var obj: JSONObject? = null
        try {
            obj = JSONObject(errorBody.string())
            return obj.getString("error")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }


    @JvmStatic
    fun isValidEmail(email: String?): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun showSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isAcceptingText) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun bingoLogger(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.i(tag, text)
    }

    fun getYearDifference(year: Int): Int {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - year
    }

    @SuppressLint("SimpleDateFormat")
    fun getProperDateFormate(date: String): String? {
        val inputDateFormat = SimpleDateFormat("yyyy-mm-dd")
        val requiredDateFormat = SimpleDateFormat("MMM dd yyyy")
        val inputDateAfterParse = inputDateFormat.parse(date)
        return requiredDateFormat.format(inputDateAfterParse)
    }

    @JvmStatic
    fun convertDateTimeFormat(cal: Calendar, format: String): String {

        val myDate = Date(cal.timeInMillis)


        val formatter = SimpleDateFormat(format)
        return formatter.format(myDate)

    }


    interface AlertActionListener {
        fun onAlertDismissWithOK()
        fun onAlertDismissWithCancel()
    }

    interface AlertViewDataListener {
        fun onDismiss(text: String)
    }

    /* Get the Screen Width */
    @JvmStatic
    fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun sendMessage(context: Context, number: String, message: String) {
        val msgUri: Uri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, msgUri)
        intent.putExtra("sms_body", message)
        context.startActivity(intent)
    }

    @JvmStatic
    inline fun <reified T> convertJsonType(objSting: String): Gson {
        val empMapType =
            object : TypeToken<Map<String?, T?>?>() {}.type
//		val data: Map<String, T> = Gson().fromJson(objSting,empMapType)

        return Gson().fromJson(objSting, empMapType)
    }

    fun getRandomStr(length: Int = 64): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + getDateTime()
        val catch = (1..length)
            .map { allowedChars.random() }
            .joinToString("")
        return "$catch-${System.currentTimeMillis()}"
    }

    fun getDateTime(): String? {
        val simpleDateFormat = SimpleDateFormat("yyMMddhhmmss", Locale.ENGLISH)
        val date = Date()
        return simpleDateFormat.format(date)
    }

    private fun getRandomNumberString(): String {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        val number = rnd.nextInt(99999)
        // this will convert any number sequence into 6 character.
        return String.format("%05d", number)
    }

    fun getRandomString(length: Int = 64): String? {
        val randomNumber = Random().nextInt(64)+4
        return if (randomNumber > 0) {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            (1..randomNumber)
                .map { allowedChars.random() }
                .joinToString("")
        } else null
    }

    fun stringDateTimeToRequiredDateTime(dateTime: String, requiredDateFormat: String): String {
        return if (dateTime.isNotEmpty()) {
            var spf = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
            var newDate:Date ?=null
            try {
                newDate = spf.parse(dateTime)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat(requiredDateFormat)
            spf.format(newDate)
        } else ""
    }

    fun stringToDate(dateTime: String): Date? {
        return if (dateTime.isNotEmpty()) {
            val spf = SimpleDateFormat("yyyy-MM-dd")
            var newDate: Date? = null
            try {
                newDate = spf.parse(dateTime)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            newDate
        } else null
    }

    fun stringDateToFormattedDate(dateTime: String, requiredDateFormat: String): String {
        return if (dateTime.isNotEmpty()) {
            var spf = SimpleDateFormat("yyy-MM-dd")
            var newDate:Date ?=null
            try {
                newDate = spf.parse(dateTime)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat(requiredDateFormat)
            spf.format(newDate)
        } else ""
    }

}