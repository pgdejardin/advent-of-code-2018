package io.pg.advent.day1

import arrow.syntax.collections.tail
import java.io.File
import java.lang.Integer.parseInt
import java.time.Instant

fun main(args: Array<String>) {
  println("Day 1")
  val lines: List<String> = FileUtil.loadFile()
//  val frequencyPartOne = findFrequencyPartOne(0, lines)
//  print(frequencyPartOne)
  val before = Instant.now()
  val frequencyPartTwo = findFrequencyPartTwo(0, lines)
  val after = Instant.now()
  println(frequencyPartTwo)
  println("Time took vor v2: ${after.epochSecond - before.epochSecond} secs")
}

object FileUtil {
  fun loadFile(): List<String> {
    val file = File(javaClass.getResource("/input.txt").path)
    return file.readLines()
  }
}

tailrec fun findFrequencyPartOne(start: Int = 0, changes: List<String>): Int =
  if (changes.isEmpty()) start else findFrequencyPartOne(start + parseInt(changes.first()), changes.tail())

tailrec fun findFrequencyPartTwo(
  start: Int = 0,
  changes: List<String>,
  seen: HashSet<Int> = hashSetOf(start),
  memo: List<String> = changes
): Int {
  if (changes.isEmpty() && seen.size == 1) {
    return start
  }

  val frequency = start + parseInt(changes.first())

  if (seen.contains(frequency)) {
    return frequency
  }

  seen.add(frequency)
  if (changes.tail().isEmpty()) {
    return findFrequencyPartTwo(frequency, memo, seen, memo)
  }

  return findFrequencyPartTwo(frequency, changes.tail(), seen, memo)
}
