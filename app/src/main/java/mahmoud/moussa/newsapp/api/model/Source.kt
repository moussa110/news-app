package mahmoud.moussa.newsapp.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Source(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Any? = null
):Serializable