package kim.bifrost.github.repository.network.model

import com.google.gson.annotations.SerializedName

/**
 * kim.bifrost.github.repository.network.model.SearchPager
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/22 19:26
 */
data class SearchPager<T>(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<T>
)