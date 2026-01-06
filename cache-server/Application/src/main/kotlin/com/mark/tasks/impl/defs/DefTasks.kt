package com.mark.tasks.impl.defs

import com.mark.tasks.CacheTask
import com.displee.cache.CacheLibrary
import java.io.File

class PackItems(val dir: File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Packing items from ${dir.absolutePath}...")
    }
}

class PackNpcs(val dir: File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Packing NPCs from ${dir.absolutePath}...")
    }
}

class PackObjects(val dir: File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        println("Packing objects from ${dir.absolutePath}...")
    }
}
