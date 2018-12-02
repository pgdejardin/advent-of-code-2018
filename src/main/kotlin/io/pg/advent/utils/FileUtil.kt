package io.pg.advent.utils

import java.io.File

object FileUtil {
  fun loadFile(path: String): List<String> {
    val file = File(javaClass.getResource(path).path)
    return file.readLines()
  }
}
