package mahmoud.moussa.newsapp.api.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: MutableList<ArticlesItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("code")
	val errorCode: String? = null,

	@field:SerializedName("message")
	val errorMessage: String? = null
)