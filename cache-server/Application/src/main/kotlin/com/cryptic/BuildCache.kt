package com.cryptic

import com.mark.Builder
import com.mark.TaskType
import com.mark.tasks.impl.RemoveXteas

object BuildCache {
    fun init() {
        val app = Builder(TaskType.BUILD)
            .cacheLocation(CACHE_LOCATION)
            .extraTasks(tasks)
        .build()
        app.initialize()
    }

}

fun main(args: Array<String>) {
    BuildCache.init()
}
