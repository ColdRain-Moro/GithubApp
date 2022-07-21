package kim.bifrost.github.repository.database.dao

import androidx.room.*
import kim.bifrost.github.repository.database.entity.TraceEntity
import kim.bifrost.github.repository.database.entity.TraceQueryResult

/**
 * kim.bifrost.github.repository.database.dao.TraceDao
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 0:22
 */
@Dao
interface TraceDao {
    @Transaction
    @Query("SELECT * FROM trace")
    suspend fun queryAll(): List<TraceQueryResult>

    @Transaction
    @Query("SELECT * FROM trace WHERE time > :time - :duration and time < :time ORDER BY time DESC")
    suspend fun queryByDate(time: Long, duration: Long): List<TraceQueryResult>

    // 冲突时会替换掉
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TraceEntity)

    // 实现一下分页查询，配合paging食用
    @Transaction
    @Query("SELECT * FROM trace LIMIT :limit OFFSET :offset")
    suspend fun queryByPage(limit: Int, offset: Int): List<TraceQueryResult>

    @Query("DELETE FROM trace")
    suspend fun delete()
}