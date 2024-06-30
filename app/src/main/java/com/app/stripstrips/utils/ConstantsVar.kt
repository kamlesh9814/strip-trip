package com.app.stripstrips.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TaskStackBuilder
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.app.stripstrips.R
import com.app.stripstrips.activity.MainActivity
import com.app.stripstrips.fragment.LoginFragment
import com.app.stripstrips.sharedPreferences.AppPrefs
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URISyntaxException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object ConstantsVar {

    const val BASE_URL = "https://app.stripandtrip.com/webservices/"
    val PLACES_API_KEY = "AIzaSyBv-NRJ5Sp3A5n_hRoI7xJg7D72r7NSSWY"

    var ISBACK = "0"

    const val GOOGLE_PLACES_SEARCH =
        "https://maps.googleapis.com/maps/api/place/autocomplete/json?"

    const val GOOGLE_PLACES_LAT_LONG =
        "https://maps.googleapis.com/maps/api/place/details/json?place_id="

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    var REMEMBER_ME: String = "remember_me"
    var EMAIL: String = "email"
    var PASSWORD: String = "password"

    /** ................................................................... **/
    /** video play **/
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getFilePath(mActivity: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                mActivity,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(
                    split[1]
                )
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            var cursor: Cursor? = null
            try {
                cursor = mActivity?.contentResolver
                    ?.query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**................................................................. **/
    fun showFinishAlertDialog(mActivity: Activity?, strMessage: String?) {
        val alertDialog = Dialog(mActivity!!)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.dialog_alert)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txtMessageTV = alertDialog.findViewById<TextView>(R.id.tvAreYouSure)
        val btnDismiss = alertDialog.findViewById<RelativeLayout>(R.id.btnDismiss)
        txtMessageTV.text = strMessage
        btnDismiss.setOnClickListener {
            alertDialog.dismiss()

        }
        alertDialog.show()
    }

    /** Trip Diary Dialog **/
    fun showDiaryAlertDialog(mActivity: Context) {
        val alertDialog = Dialog(mActivity)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.toast_alart)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnDismiss = alertDialog.findViewById<RelativeLayout>(R.id.btnDismiss)

        btnDismiss.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    /**...........................................................**/
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return try {
            val info = connectivityManager.activeNetworkInfo
            info != null && info.isConnectedOrConnecting
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @SuppressLint("NewApi")
    fun timeStampToDate(timeStamp: Long): String {
        Log.e("TAG", "timeStampToDate: $timeStamp")
        var formatter = SimpleDateFormat("hh:mm aa, dd MMM YYYY")
        return formatter.format(Date(timeStamp * 1000))

    }

    @SuppressLint("NewApi")
    fun dateToTimestamp(str_date: String): String {
        val formatter: DateFormat = SimpleDateFormat("hh:mm dd mm yyyy")
        val date = formatter.parse(str_date)
        System.out.println("Today is " + date!!.time)
        return date.time.toString()

    }

    @SuppressLint("NewApi")
    fun dateStampYear(timeStamp: Long): String {
        Log.e("TAG", "timeStampToDate: $timeStamp")
        var formatter = SimpleDateFormat("dd MMM,YYYY")
        return formatter.format(Date(timeStamp * 1000))

    }

    fun getImageUri(Context: Context?, inImage: Bitmap): Uri {
        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    fun ClearDataDialogAlart(mActivity: Context) {
        val alertDialog = Dialog(mActivity)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.clear_data_layout)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnDismiss = alertDialog.findViewById<RelativeLayout>(R.id.btnDismiss)

        btnDismiss.setOnClickListener {
            AppPrefs(mActivity).clearData()
           val intent = Intent(mActivity,MainActivity::class.java)
            mActivity.startActivity(intent)
            alertDialog.dismiss()
        }
    }

    fun AppDialogAlart(mActivity: Context, message: String?) {
        val alertDialog = Dialog(mActivity)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.app_dialogue_alart_layout)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnDismiss = alertDialog.findViewById<RelativeLayout>(R.id.btnDismiss)
        val tvPopup = alertDialog.findViewById<TextView>(R.id.tvPopup)
        tvPopup.text = message

        btnDismiss.setOnClickListener {
            AppPrefs(mActivity).clearData()
            alertDialog.dismiss()
        }
    }

/*
    fun showAccountDisableAlertDialog(mActivity: Activity?, mMessage: String) {
        val alertDialog = Dialog(mActivity!!)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.app_dialogue_alart_layout)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // set the custom dialog components - text, image and button
        val btnDismiss = alertDialog.findViewById<RelativeLayout>(R.id.btnDismiss)
        val tvPopup = alertDialog.findViewById<TextView>(R.id.tvPopup)
        tvPopup.text = mMessage
        btnDismiss.setOnClickListener {
            alertDialog.dismiss()
            val preferences: SharedPreferences =
                mActivity.let { NaijaDocsPreferences().getPreferences(it) }
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            editor.commit()
            val mIntent = Intent(mActivity, LoginChoiceAcriviry::class.java)
            TaskStackBuilder.create(mActivity).addNextIntentWithParentStack(mIntent)
                .startActivities()
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)
            finish()
            finishAffinity()
        }
        alertDialog.show()
    }*/
}

