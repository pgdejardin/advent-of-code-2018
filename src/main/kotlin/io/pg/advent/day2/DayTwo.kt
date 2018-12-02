package io.pg.advent.day2

import arrow.core.firstOrNone
import arrow.core.getOrElse
import io.pg.advent.utils.FileUtil


fun main(args: Array<String>) {
  val lines = FileUtil.loadFile("/day2.txt")
  val checksumPartOne = generateChecksum(lines.countTwoAndThreeLetters())
  println("checksumPartOne: $checksumPartOne")
  val id = commonsLetterMinusOne(lines)
  println("ID: $id")
}

fun String.hasNSameLetters(n: Int): Boolean =
  this.toCharArray().groupBy { it }.mapValues { it.value.size }.containsValue(n)

fun List<String>.countTwoAndThreeLetters(): Pair<Int, Int> {
  var countTwo = 0
  var countThree = 0
  this.forEach {
    if (it.hasNSameLetters(2)) countTwo++
    if (it.hasNSameLetters(3)) countThree++
  }
  return Pair(countTwo, countThree)
}

fun generateChecksum(counts: Pair<Int, Int>): Int {
  return counts.first * counts.second
}

fun commonsLetterMinusOne(lines: List<String>): String {
  lines.map {
    val hasCommons = lines.firstOrNone { s -> it.hasJustOneLetterChanging(s) }
    val foundStr = hasCommons.getOrElse { "" }
    if (!foundStr.isEmpty()) return foundStr.getCommonsCharMaxOneCharDiverging(it)
  }
  return ""
}

fun String.hasJustOneLetterChanging(str: String): Boolean =
  this.mapIndexed { index: Int, c: Char -> str[index] == c }.filter { !it }.size == 1

fun String.getCommonsCharMaxOneCharDiverging(str: String): String =
  this.filterIndexed { index: Int, c: Char -> str[index] == c }
