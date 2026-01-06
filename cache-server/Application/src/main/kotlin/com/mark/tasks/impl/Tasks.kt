package com.mark.tasks.impl

import com.mark.tasks.CacheTask
import com.displee.cache.CacheLibrary
import java.io.File

class PackMaps(val dir: File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Packing maps from ${dir.absolutePath}...")
    }
}

class PackModels(val dir: File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Packing models from ${dir.absolutePath}...")
    }
}

class RemoveXteas : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Removing XTEAs...")
    }
}
