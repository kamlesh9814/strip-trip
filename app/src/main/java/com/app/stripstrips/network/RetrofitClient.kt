package com.app.stripstrips.network
import android.util.Log
import androidx.databinding.ktx.BuildConfig
import com.app.stripstrips.base.MyApplication.Companion.getContext
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.utils.ConstantsVar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(ConstantsVar.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiInterface = retrofitBuilder.create(ApiInterface::class.java)!!

    private val client: OkHttpClient
        get() = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
            val request: Request
            val token = AppPrefs(getContext()).getToken("token")
            println("token $token")
            request = if (!token.isNullOrEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Token", token).build()

            } else {
                chain.request().newBuilder()
                    .build()
            }
            chain.proceed(request)
        }
            .addInterceptor(provideHttpLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.d("Http Request => ", message) }
        httpLoggingInterceptor.setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        )
        return httpLoggingInterceptor
    }
}