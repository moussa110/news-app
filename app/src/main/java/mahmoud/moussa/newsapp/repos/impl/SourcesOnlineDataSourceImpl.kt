package mahmoud.moussa.newsapp.repos.impl

import mahmoud.moussa.newsapp.api.WebServices
import mahmoud.moussa.newsapp.api.model.SourcesItem
import mahmoud.moussa.newsapp.repos.interfaces.SourcesOnlineDataSource

class SourcesOnlineDataSourceImpl(val webServices: WebServices):SourcesOnlineDataSource {
    override suspend fun getSources(): List<SourcesItem> {
        val resp=webServices.getSources()
        return resp.sources as List<SourcesItem>
    }
}