package kim.bifrost.lib_common.base.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * kim.bifrost.lib_common.base.adapter.BasePagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:22
 */
abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun getData(page: Int): List<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pos = params.key ?: 0
        return try {
            // Get data from DataSource
            val pageList = getData(pos)
            // Return data to RecyclerView by LoadResult
            LoadResult.Page(
                pageList,
                if (pos <= 0) null else pos - 1,
                if (pageList.isEmpty()) null else pos + 1
            )
        } catch (exception: Exception) {
            // Return exception by LoadResult
            LoadResult.Error(exception)
        }
    }
}