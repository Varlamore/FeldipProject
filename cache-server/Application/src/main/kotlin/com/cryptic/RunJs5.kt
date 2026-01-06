package com.cryptic

import com.mark.Builder
import com.mark.TaskType
import java.io.File

object RunJs5 {

    private val DATA_LOCATION = File("data")
    private val CACHE_LOCATION = File(DATA_LOCATION,"cache")

    fun init() {
        println("Found cache$CACHE_LOCATION")

        val app = Builder(TaskType.RUN_JS5)
            .cacheLocation(CACHE_LOCATION)
            .js5Revision(217)
        .build()
        app.initialize()

    }
}

fun main(args: Array<String>) {
    RunJs5.init()
}
