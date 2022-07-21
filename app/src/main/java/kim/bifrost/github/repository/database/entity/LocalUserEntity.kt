package kim.bifrost.github.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * kim.bifrost.github.repository.database.entity.LocalUserEntity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 11:25
 */
@Entity(
    tableName = "local_user"
)
data class LocalUserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)