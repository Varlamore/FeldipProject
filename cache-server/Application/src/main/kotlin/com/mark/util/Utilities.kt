package com.mark.util

import java.io.File
import me.tongfei.progressbar.ProgressBar

fun getFiles(directory: File, vararg extensions: String): List<File> {
    return directory.listFiles { file ->
        extensions.any { ext -> file.name.endsWith(".$ext", ignoreCase = true) }
    }?.toList() ?: emptyList()
}

fun progress(taskName: String, total: Int): ProgressBar {
    return ProgressBar(taskName, total.toLong())
}
