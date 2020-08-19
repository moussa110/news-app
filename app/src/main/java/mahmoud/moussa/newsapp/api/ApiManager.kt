package mahmoud.moussa.newsapp.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager {

    /* private fun isConnected(): Boolean {
        var isconnect=false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        isconnect= activeNetwork?.isConnectedOrConnecting == true
        return isconnect;
    }*/

    companion object{
        val API_KEY = "5760b823c5ac43449fd695bedfdf68e5";
        var LANGUAGE = "en"
    }

        private var retrofit: Retrofit? = null;

        private fun getInstance(): Retrofit {
            if (retrofit == null) {

//                val cacheSize = 10 * 1024 * 1024 // 10 MB
//                val cache = Cache(context.cacheDir, cacheSize.toLong())

                val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.e("api", message)
                    }
                })

                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client =
                    OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(HeaderInterceptor())
                        .addInterceptor(LanguageInterceptor())
                        //.addNetworkInterceptor(OnlineInterceptor())
                        //.addInterceptor(OfflineInterceptor(isConnected()))
                        //.cache(cache)
                        .build()

                retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            }
            return retrofit ?: Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

    fun getWebServices(): WebServices {
        return getInstance().create(WebServices::class.java);
    }
}

private class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

//        //query prameters
//        var request = chain.request()
//        val url: HttpUrl = request.url.newBuilder().addQueryParameter("apiKey", ApiManager.API_KEY).build()
//        request = request.newBuilder().url(url).build()
//        return chain.proceed(request)

        //header
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization","Bearer "+ApiManager.API_KEY)
            .build()
        return chain.proceed(request)
    }
}

private class LanguageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        //query prameters
        var request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("language", ApiManager.LANGUAGE).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)

        return chain.proceed(request)
    }
}

private class OnlineInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val maxAge = 60 // read from cache for 60 seconds even if there is internet connection

        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}

private class OfflineInterceptor(var isconnect:Boolean)  : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (!isconnect) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        return chain.proceed(request)
    }


}


