package kim.bifrost.github.repository.database.dao

import androidx.paging.PagingSource
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
    suspend fun queryAll(): List<BookmarksQueryResult>

    // 冲突时会替换掉
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookmarksEntity)

    // 实现一下分页查询，配合paging食用
    @Transaction
    @Query("SELECT * FROM bookmarks LIMIT :limit OFFSET :offset")
    suspend fun queryByPage(limit: Int, offset: Int): List<BookmarksQueryResult>

    @Query("DELETE FROM bookmarks")
    suspend fun delete()

    @Query("DELETE FROM bookmarks WHERE repo_id = :repoId")
    suspend fun deleteByRepoId(repoId: Int)

    @Query("DELETE FROM bookmarks WHERE user_id = :userId")
    suspend fun deleteByUserId(userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<BookmarksEntity>)

    @Transaction
    @Query("SELECT * FROM bookmarks ORDER BY time DESC")
    fun pagingSource(): PagingSource<Int, BookmarksQueryResult>

    @Query("DELETE FROM bookmarks")
    suspend fun clearAll()
}