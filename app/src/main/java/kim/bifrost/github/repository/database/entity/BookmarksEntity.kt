package kim.bifrost.github.repository.database.entity

import androidx.room.*
import java.util.*

/**
 * kim.bifrost.github.repository.database.entity.BookmarksEntity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 21:15
 */
@Entity(tableName = "bookmarks", indices = [
    Index(value = ["user_id"], unique = true),
    Index(value = ["repo_id"], unique = true)
])
data class BookmarksEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    @ColumnInfo(name = "user_id")
    val userId: Int? = null,
    @ColumnInfo(name = "repo_id")
    val repoId: Int? = null,
    val time: Date,
    // repo / user
    val type: String,
)

data class BookmarksQueryResult(
    @Embedded
    val entity: BookmarksEntity,
    @Relation(parentColumn = "user_id", entityColumn = "id")
    val user: LocalUserEntity? = null,
    @Relation(parentColumn = "repo_id", entityColumn = "id")
    val repo: LocalRepoEntity? = null
)