package mahmoud.moussa.newsapp.api.model

import com.google.gson.annotations.SerializedName

data class SourcesResponse(

	@field:SerializedName("sources")
	val sources: List<SourcesItem>? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("code")
	val errorCode: String? = null,

	@field:SerializedName("message")
	val errorMessage: String? = null
	)