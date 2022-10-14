package com.iwdael.annotationprocessorparser.poet

import com.iwdael.annotationprocessorparser.Class
import com.iwdael.annotationprocessorparser.Parser
import java.io.File

val mapPoet = mutableMapOf<Int, Parser>()


fun Class.filePath(): String? {
    val sourceFile = this.element.javaClass.getDeclaredField("sourcefile")
        .apply { isAccessible = true }
        .get(this.element) ?: return null
    val userPath = sourceFile.javaClass.getDeclaredField("userPath")
        .apply { isAccessible = true }
        .get(sourceFile) ?: return null;
    val path = "$userPath"
    if (element.getAnnotation(Metadata::class.java) == null) return path

    val build = File.separator + "build" + File.separator
    val debug = File.separator + "debug" + File.separator
    val release = File.separator + "release" + File.separator
    val i = path.indexOf(build)
    val tempJavaPath = path.substring(0, i) +
            "${File.separator}src${File.separator}main${File.separator}java${File.separator}" +
            path.substring(
                (path.indexOf(release, i) + release.length)
                    .coerceAtLeast(path.indexOf(debug, i) + debug.length)
            )
    val javaPath = tempJavaPath.replace(".java", ".kt")
    if (File(javaPath).exists()) return javaPath
    val rootDir = File(path.substring(0, i))
    return rootDir.searchPathWithEnd(
        path.substring(
            (path.indexOf(release, i) + release.length)
                .coerceAtLeast(path.indexOf(debug, i) + debug.length)
        ).replace(".java", ".kt"),
        build
    )
}

fun Class.srcPath(`package`: String): String? {
    val sourceFile = this.filePath() ?: return null
    var dir = File(sourceFile).parentFile
    val dirLevel = `package`.split(".").size
    for (index in 0 until dirLevel) {
        dir = dir.parentFile
    }
    return dir.absolutePath
}


private fun File.searchPathWithEnd(path: String, vararg filters: String): String? {
    if (isFile) return null
    val list = listFiles()
    for (file in list) {
        if (file.absolutePath.endsWith(path)) {
            var f = false
            for (filter in filters) {
                if (!file.absolutePath.contains(filter)) {
                    f = true
                }
            }
            if (filters.isEmpty()) f = true
            if (f) return file.absolutePath
        }
        val path = file.searchPathWithEnd(path)
        if (path != null) {
            var f = false
            for (filter in filters) {
                if (!path.contains(filter)) {
                    f = true
                }
            }
            if (filters.isEmpty()) f = true
            if (f) return path
        }
    }
    return null
}