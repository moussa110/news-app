package mahmoud.moussa.newsapp.database

import androidx.room.*
import mahmoud.moussa.newsapp.api.model.SourcesItem

@Dao
interface SourcesDao {

    @Insert
    fun insertSources(sourcesItem: List<SourcesItem>)

    @Delete
    fun deleteSources(sourcesItem: SourcesItem?)

    @Query("select * from SourcesItem")
    fun getAllSources():List<SourcesItem>

    @Query("select * from SourcesItem where country=:country")
    fun getSourcesByCountry(country:String):MutableList<SourcesItem?>
}