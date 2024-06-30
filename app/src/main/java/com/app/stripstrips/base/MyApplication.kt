package com.app.stripstrips.base
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import com.app.stripstrips.R
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.util.*


class MyApplication : Application() {

    companion object {

   var mContext: MyApplication? = null

        fun getContext(): MyApplication {
            if (mContext == null)
                mContext = MyApplication()
            return mContext as MyApplication
        }

        var mLoadingView: ProgressBar? = null
        var mLoader: Dialog? = null

        fun showLoader(context: Context) {
            try {
                if (mLoader != null) {
                    mLoader!!.dismiss()
                    mLoader!!.cancel()
                }
                if (mLoadingView != null) {
                    //   mLoadingView!!.hide()
                }
                mLoader = Dialog(context)
                mLoader!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mLoader!!.setContentView(R.layout.layout_progress_dialog)
                Objects.requireNonNull(mLoader!!.window)
                    ?.setBackgroundDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT
                        )
                    )
                mLoader!!.setCancelable(true)
                mLoadingView = mLoader!!.findViewById(R.id.loadingView)
                //mLoadingView!!.show()
                mLoader!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideLoader() {
            if (mLoader != null) {
                mLoader!!.dismiss()
                mLoader!!.cancel()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        mContext = applicationContext as MyApplication

        val options = FirebaseOptions.Builder()
            .setApplicationId("1:1052116155442:android:984d22ae19399caee3102b") // Required for Analytics.
            .setProjectId("stripsandtrips") // Required for Firebase Installations.
            .setApiKey("AIzaSyCq5CVtcYoGYLE77F7_SRhoZ9Ll7wsYOSM") // Required for Auth.
            .build()
        FirebaseApp.initializeApp(applicationContext, options, "Strip&Trip")

       // FirebaseApp.initializeApp(mContext!!)
    }
}