package mahmoud.moussa.newsapp.repos.interfaces

import mahmoud.moussa.newsapp.api.model.SourcesItem

interface SourcesOfflineDataSource {
    suspend fun cacheData(data:List<SourcesItem>);
    suspend fun getSources():List<SourcesItem>;
}