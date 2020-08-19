package mahmoud.moussa.newsapp.repos.impl

import mahmoud.moussa.newsapp.api.model.SourcesItem
import mahmoud.moussa.newsapp.database.SourcesDao
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOfflineDataSource

class SourcesOfflineDataSourceImpl(val sourcesDao: SourcesDao?):SourcesOfflineDataSource{
    override suspend fun cacheData(data: List<SourcesItem>) {
        sourcesDao?.insertSources(data)
    }

    override suspend fun getSources(): List<SourcesItem> {
        return sourcesDao?.getAllSources()!!
    }

}