package io.pg.advent.day5

import arrow.core.Try
import arrow.core.getOrDefault
import io.pg.advent.utils.FileUtil

fun main() {
  val input = FileUtil.loadFile("/day5.txt")
  val polymer = input.joinToString().polymerReaction()
  println("Polymer length: ${polymer.length}")
  val minPolymerSize = input.joinToString().minPolymerSize()
  println("Min size of polymers: $minPolymerSize")
}

fun Char.unitMatch(other: Char): Boolean = this.toLowerCase() == other.toLowerCase() && this != other

fun String.polymerReaction(): String {
  return this.fold("") { acc: String, c: Char ->
    if (Try { acc.last() }.getOrDefault { ' ' }.unitMatch(c)) {
      acc.substring(0, acc.length - 1)
    } else {
      "$acc$c"
    }
  }
}

fun String.minPolymerSize(): Int {
  val units = "abcdefghijklmnopqrstuvwxyz"
  val sizes: MutableMap<Int, String> = mutableMapOf()

  units.forEach {
    val regex = "[${it.toLowerCase()}${it.toUpperCase()}]".toRegex()
    val polymerWithoutUnit = this.replace(regex, "")
    val polymerAfterReaction = polymerWithoutUnit.polymerReaction()
    sizes[polymerAfterReaction.length] = polymerAfterReaction
  }

  return sizes.keys.min() ?: 0
}
