package mahmoud.moussa.newsapp.ui.mainActivity

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mahmoud.moussa.newsapp.api.ApiManager
import mahmoud.moussa.newsapp.api.model.ArticlesItem
import mahmoud.moussa.newsapp.api.model.NewsResponse
import mahmoud.moussa.newsapp.api.model.SourcesItem
import mahmoud.moussa.newsapp.database.ArticlesDatabase
import mahmoud.moussa.newsapp.repos.impl.SourcesOfflineDataSourceImpl
import mahmoud.moussa.newsapp.repos.impl.SourcesOnlineDataSourceImpl
import mahmoud.moussa.newsapp.repos.impl.SourcesRepositoryImpl
import mahmoud.moussa.newsapp.repos.interfaces.NetworkHandler
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOfflineDataSource
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOnlineDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityViewModel(application: Application):AndroidViewModel(application),NetworkHandler {

    val sourcesLiveData=MutableLiveData<List<SourcesItem?>?>()
    val articlesItemLiveData=MutableLiveData<List<ArticlesItem?>?>()
    val showProgressDialogLiveData=MutableLiveData<Boolean>();
    val messageLiveData=MutableLiveData<String>();

    val onlineDataSource:SourcesOnlineDataSource=SourcesOnlineDataSourceImpl(ApiManager().getWebServices())
    val offlineDataSource:SourcesOfflineDataSource=SourcesOfflineDataSourceImpl(ArticlesDatabase.getInstance(application)?.SourcesDao())
    val sourcesRepository=SourcesRepositoryImpl(onlineDataSource,offlineDataSource,this)


    override fun isOnline(): Boolean {
/*        val context=getApplication<Application>().applicationContext
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        Log.e("state",return netInfo != null && netInfo.isConnected)
        return netInfo != null && netInfo.isConnected*/

        val context=getApplication<Application>().applicationContext
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm!!.activeNetworkInfo.isConnected
    }

    fun getNewsSources() {
        showProgressDialogLiveData.value=true;

        viewModelScope.launch {
            try {
                showProgressDialogLiveData.value=false;
                val list=sourcesRepository.getSources()
                sourcesLiveData.value=list;
            }catch (t:Throwable){
                showProgressDialogLiveData.value=false
                messageLiveData.value=t.localizedMessage;
            }

        }
    }
    fun showNewsById(id: String?) {
        showProgressDialogLiveData.value=true
        ApiManager().getWebServices()
            .getAllNews(id)
            .enqueue(object :Callback<NewsResponse>{
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    showProgressDialogLiveData.value=false
                    messageLiveData.value=t.localizedMessage;
                }

                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    showProgressDialogLiveData.value=false
                    if ("ok".equals(response.body()?.status))
                        articlesItemLiveData.value=response.body()?.articles
                    else
                        messageLiveData.value=response.message();
                }
            })
    }
    fun getNewsByQuery(p0: String?) {
        showProgressDialogLiveData.value=true
        ApiManager().getWebServices()
            .getAllNews(query = p0)
            .enqueue(object :Callback<NewsResponse>{
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    showProgressDialogLiveData.value=false
                    messageLiveData.value=t.localizedMessage;
                }

                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    showProgressDialogLiveData.value=false
                    if ("ok" == response.body()?.status)
                        articlesItemLiveData.value=response.body()?.articles;
                    else
                        messageLiveData.value= response.body()?.errorMessage;
                }
            })
    }
    fun showHeadlines(s: String,c:String?= null) {
        showProgressDialogLiveData.value=true;

        if (c==null){
          viewModelScope.launch {
              try {
                  val response=ApiManager().getWebServices().getHeadlines(country = s);
                  showProgressDialogLiveData.value=false
                  if ("ok" == response.status)
                      articlesItemLiveData.value=response.articles
                  else
                      messageLiveData.value=response.errorMessage
              }catch (t:Throwable){
                  showProgressDialogLiveData.value=false
                  messageLiveData.value=t.localizedMessage;
              }

          }
        }else{
            viewModelScope.launch {
                try {
                    val response=ApiManager().getWebServices().getHeadlines(country = s,category = c)
                    showProgressDialogLiveData.value=false
                    if ("ok" == response.status)
                        articlesItemLiveData.value=response.articles
                    else
                        messageLiveData.value=response.errorMessage
                }catch (t:Throwable){
                    showProgressDialogLiveData.value=false
                    messageLiveData.value=t.localizedMessage;
                }
            }
        }
    }

    fun refreshFavData(context: Context){
        var data= ArticlesDatabase.getInstance(context)?.articlesDao()?.getAllArticle();
        articlesItemLiveData.value=data;
    }
    fun removeFromFavorite(context: Context,item: ArticlesItem?) {
        ArticlesDatabase.getInstance(context)
            ?.articlesDao()?.deleteArticle(item)
    }
    fun addToFavorite(context: Context,item: ArticlesItem?) {
        ArticlesDatabase.getInstance(context)
            ?.articlesDao()?.insertArticle(item)
    }
    fun showFavoriteArticles(context: Context) {
        var item=ArticlesDatabase
            .getInstance(context)
            ?.articlesDao()
            ?.getAllArticle()
        articlesItemLiveData.value=item;
    }



}