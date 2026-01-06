package com.mark.tasks

import com.displee.cache.CacheLibrary

abstract class CacheTask {
    abstract fun init(library: CacheLibrary)
}
