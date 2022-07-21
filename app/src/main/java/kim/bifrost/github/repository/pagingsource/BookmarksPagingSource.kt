package kim.bifrost.github.repository.pagingsource

import kim.bifrost.github.repository.database.AppDatabase
import kim.bifrost.github.repository.database.entity.BookmarksQueryResult
import kim.bifrost.lib_common.base.adapter.BasePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * kim.bifrost.github.repository.pagingsource.BookmarksPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 10:25
 */
class BookmarksPagingSource : BasePagingSource<BookmarksQueryResult>() {
    override suspend fun getData(page: Int): List<BookmarksQueryResult> {
        return withContext(Dispatchers.IO) { AppDatabase.INSTANCE.bookmarksDao().queryByPage(20, 20 * page) }
    }
}