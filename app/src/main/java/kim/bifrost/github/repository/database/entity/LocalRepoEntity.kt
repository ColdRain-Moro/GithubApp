package kim.bifrost.github.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * kim.bifrost.github.repository.database.entity.LocalRepoEntity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 11:20
 */
@Entity(
    tableName = "local_repos"
)
data class LocalRepoEntity(
    // 使用网络请求返回数据的id，不用自己维护一个id
    @PrimaryKey
    val id: Int,
    val owner: String,
    @ColumnInfo(name = "avatar_url", typeAffinity = ColumnInfo.TEXT)
    val avatarUrl: String,
    val name: String,
    val desc: String?,
    val star: Int,
    val forks: Int,
    val language: String?,
    @ColumnInfo(name = "language_color", typeAffinity = ColumnInfo.TEXT)
    val languageColor: String?
)