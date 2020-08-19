package mahmoud.moussa.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mahmoud.moussa.newsapp.api.model.ArticlesItem
import mahmoud.moussa.newsapp.api.model.SourcesItem

@Database(entities = arrayOf(ArticlesItem::class,SourcesItem::class),version = 1)
abstract class ArticlesDatabase: RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
    abstract fun SourcesDao(): SourcesDao

    companion object{

        val DB_NAME="DB-Favorite"
        private var articlesDatabase: ArticlesDatabase?=null

        fun getInstance(context: Context): ArticlesDatabase? {
            if (articlesDatabase == null){
                articlesDatabase = Room.databaseBuilder(
                    context,
                    ArticlesDatabase::class.java, DB_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return articlesDatabase
        }
    }

}