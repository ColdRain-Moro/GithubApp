package kim.bifrost.github.repository.pagingsource

import android.util.Log
import kim.bifrost.github.repository.database.AppDatabase
import kim.bifrost.github.view.adapter.TraceItem
import kim.bifrost.lib_common.base.adapter.BasePagingSource
import kim.bifrost.lib_common.extensions.TAG
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * kim.bifrost.github.repository.pagingsource.TracePagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 23:55
 */
class TracePagingSource : BasePagingSource<TraceItem>() {
    override suspend fun getData(page: Int): List<TraceItem> {
        val datetime = System.currentTimeMillis() - page * TimeUnit.DAYS.toMillis(1)
        return AppDatabase.INSTANCE.traceDao()
            .queryByDate(datetime, TimeUnit.DAYS.toMillis(1))
            .map {
                if (it.entity.type == "repo") {
                    TraceItem.Repo(it.repo!!)
                } else {
                    TraceItem.User(it.user!!)
                }
            }.run {
                mutableListOf<TraceItem>().apply {
                    if (this@run.isNotEmpty()) {
                        add(TraceItem.Divider(Date(datetime)))
                    }
                    addAll(this@run)
                }
            }
    }
}