package mahmoud.moussa.newsapp.api.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class ArticlesItem(
	@PrimaryKey(autoGenerate = true)
	var id:Int?=null,

	@field:SerializedName("publishedAt")
	var publishedAt: String? = null,

	@field:SerializedName("author")
	var author: String? = null,

	@field:SerializedName("urlToImage")
	var urlToImage: String? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("source")
	@Ignore
	var source: Source? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@field:SerializedName("content")
	@Ignore
	var content: Any? = null
):Serializable