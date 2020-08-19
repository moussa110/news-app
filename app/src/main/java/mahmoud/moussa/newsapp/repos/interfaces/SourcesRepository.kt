package mahmoud.moussa.newsapp.repos.interfaces

import mahmoud.moussa.newsapp.api.model.SourcesItem

interface SourcesRepository {
    suspend fun getSources():List<SourcesItem>;
}