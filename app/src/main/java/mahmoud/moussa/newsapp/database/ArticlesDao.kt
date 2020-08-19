package mahmoud.moussa.newsapp.database

import androidx.room.*
import mahmoud.moussa.newsapp.api.model.ArticlesItem

@Dao
interface ArticlesDao {

    @Insert
    fun insertArticle(articlesItem: ArticlesItem?)

    @Delete
    fun deleteArticle(articlesItem: ArticlesItem?)

    @Query("select * from ArticlesItem")
    fun getAllArticle():MutableList<ArticlesItem?>

    @Query("select * from ArticlesItem where title=:title")
    fun getFavorite(title:String):MutableList<ArticlesItem?>
}