package kim.bifrost.github.repository.database.dao

import androidx.room.*
import kim.bifrost.github.repository.database.entity.TraceEntity
import kim.bifrost.github.repository.database.entity.TraceQueryResult
import kotlinx.coroutines.flow.Flow

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

    @Transaction
    @Query("SELECT * FROM trace WHERE time > :time - :duration and time < :time ORDER BY time DESC")
    fun query(time: Long, duration: Long): Flow<List<TraceQueryResult>>

    // 冲突时会替换掉
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TraceEntity)

    @Query("DELETE FROM trace")
    suspend fun delete()

    @Query("DELETE FROM trace WHERE repo_id = :repoId")
    suspend fun deleteByRepoId(repoId: Int)

    @Query("DELETE FROM trace WHERE user_id = :userId")
    suspend fun deleteByUserId(userId: Int)
}