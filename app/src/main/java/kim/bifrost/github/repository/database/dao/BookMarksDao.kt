package kim.bifrost.github.repository.database.dao

import androidx.room.*
import kim.bifrost.github.repository.database.entity.BookmarksEntity
import kim.bifrost.github.repository.database.entity.BookmarksQueryResult
import kotlinx.coroutines.flow.Flow

/**
 * kim.bifrost.github.repository.database.dao.BookMarkDao
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 0:19
 */
@Dao
interface BookMarksDao {
    @Transaction
    @Query("SELECT * FROM bookmarks")
    fun queryAll(): Flow<List<BookmarksQueryResult>>

    // 冲突时会替换掉
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: BookmarksEntity)

    // 实现一下分页查询，配合paging食用
    @Transaction
    @Query("SELECT * FROM bookmarks LIMIT :limit OFFSET :offset")
    fun queryByPage(limit: Int, offset: Int): List<BookmarksQueryResult>

    @Query("DELETE FROM bookmarks")
    fun delete()
}