package mahmoud.moussa.newsapp.repos.impl

import android.util.Log
import mahmoud.moussa.newsapp.api.model.SourcesItem
import mahmoud.moussa.newsapp.repos.interfaces.NetworkHandler
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOfflineDataSource
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOnlineDataSource
import mahmoud.moussa.newsapp.repos.interfaces.SourcesRepository

class SourcesRepositoryImpl(
    val onlineDataSource: SourcesOnlineDataSource,
    val offlineDataSource: SourcesOfflineDataSource,
    val networkHandler: NetworkHandler
):SourcesRepository {

    override suspend fun getSources(): List<SourcesItem> {
        var sourcesList:List<SourcesItem>
        if (networkHandler.isOnline()){
            sourcesList=onlineDataSource.getSources()
            offlineDataSource.cacheData(sourcesList)
            Log.e("state","1")
        }else{
            Log.e("state","0")
            sourcesList=offlineDataSource.getSources()
        }
        return sourcesList;
    }
}