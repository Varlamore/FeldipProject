package com.mark

import com.mark.tasks.CacheTask
import java.io.File
import com.displee.cache.CacheLibrary

enum class TaskType {
    UPDATE_REV,
    BUILD,
    RUN_JS5
}

class Builder(val type: TaskType) {
    private var cacheLocation: File? = null
    private var cacheRevision: Int = -1
    private var extraTasks: List<CacheTask> = emptyList()
    private var js5Revision: Int = -1

    fun cacheLocation(file: File) = apply { this.cacheLocation = file }
    fun cacheRevision(rev: Int) = apply { this.cacheRevision = rev }
    fun extraTasks(tasks: List<CacheTask>) = apply { this.extraTasks = tasks }
    fun js5Revision(rev: Int) = apply { this.js5Revision = rev }

    fun build(): App {
        return App(cacheLocation ?: File("cache"), cacheRevision, extraTasks)
    }
}

class App(val cacheLocation: File, val cacheRevision: Int, val tasks: List<CacheTask>) {
    fun initialize() {
        println("Initializing Cache Builder...")
        val library = CacheLibrary(cacheLocation.absolutePath)
        tasks.forEach { task ->
            println("Running task: ${task.javaClass.simpleName}")
            task.init(library)
        }
        // Always update for now as we don't have isDirty in this version
        println("Saving cache...")
        library.update()
        println("Cache update complete.")
    }
}

