package kim.bifrost.github.repository.database.dao

import androidx.room.*
import kim.bifrost.github.repository.database.entity.LocalRepoEntity

/**
 * kim.bifrost.github.repository.database.dao.LocalRepoDao
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 12:59
 */
@Dao
interface LocalRepoDao {
    // 冲突时会替换掉
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LocalRepoEntity)

    @Delete
    suspend fun delete(entity: LocalRepoEntity)
}