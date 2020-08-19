package mahmoud.moussa.newsapp.api

import io.reactivex.Single
import mahmoud.moussa.newsapp.api.model.NewsResponse
import mahmoud.moussa.newsapp.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("sources")
    suspend fun getSources(@Query("country") country:String?=null):SourcesResponse;

    @GET("top-headlines")
    suspend fun getHeadlines(@Query("country") country:String?=null,
                     @Query("category") category:String?=null):NewsResponse;

    @GET("everything")
    fun getAllNews(@Query("sources")sourceId:String?=null,
                   @Query("q")query:String?=null):Call<NewsResponse>;


}