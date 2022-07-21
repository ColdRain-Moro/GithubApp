package kim.bifrost.github.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kim.bifrost.github.repository.database.dao.BookMarksDao
import kim.bifrost.github.repository.database.dao.LocalRepoDao
import kim.bifrost.github.repository.database.dao.LocalUserDao
import kim.bifrost.github.repository.database.dao.TraceDao
import kim.bifrost.github.repository.database.entity.BookmarksEntity
import kim.bifrost.github.repository.database.entity.LocalRepoEntity
import kim.bifrost.github.repository.database.entity.LocalUserEntity
import kim.bifrost.github.repository.database.entity.TraceEntity
import kim.bifrost.lib_common.BaseApp

/**
 * kim.bifrost.github.repository.database.AppDatabase
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 21:13
 */
@Database(entities = [BookmarksEntity::class, TraceEntity::class, LocalRepoEntity::class, LocalUserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarksDao(): BookMarksDao

    abstract fun traceDao(): TraceDao

    abstract fun localRepoDao(): LocalRepoDao

    abstract fun localUserDao(): LocalUserDao

    companion object {

        val INSTANCE by lazy { create(BaseApp.appContext) }

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "github_app.db")
                .build()
    }
}