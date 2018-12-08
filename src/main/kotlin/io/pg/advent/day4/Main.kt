package io.pg.advent.day4

import arrow.core.*
import arrow.instances.list.foldable.foldLeft
import arrow.syntax.collections.collect
import io.pg.advent.utils.FileUtil
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
  val lines = FileUtil.loadFile("/day4.txt")
  val shifts = generateGuardShifts(lines)

  val (id, minute) = findMostAsleepWithMinutes(shifts)
  println("id * minutes = ${id.toInt() * minute}")

  val (id2, minutes2) = findGuardWhoSleepsTheSameMinute(shifts)
  println("id2 * minutes2 = ${id2.toInt() * minutes2}")

}

val beginsShiftPattern: Regex = """\[(.*)] Guard #(\d+) begins shift""".toRegex()
val fallsAsleep: Regex = """\[(.*)] falls asleep""".toRegex()
val wakesUp: Regex = """\[(.*)] wakes up""".toRegex()
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m", Locale.ENGLISH)

interface RawInformation {
  val date: LocalDateTime
  val guardId: Option<String>
}

data class BeginsShift(override val date: LocalDateTime, override val guardId: Option<String>) : RawInformation

data class FallsAsleep(override val date: LocalDateTime, override val guardId: Option<String>) : RawInformation

data class WakesUp(override val date: LocalDateTime, override val guardId: Option<String>) : RawInformation

data class Sheet(val id: String, val sleeps: List<Pair<LocalDateTime, LocalDateTime>>)

fun String.rawInformation(): RawInformation {
  val begin = beginsShiftPattern.find(this)
  val fall = fallsAsleep.find(this)
  val wake = wakesUp.find(this)

  if (begin != null) {
    val (date, id) = begin.destructured
    val dateTime = LocalDateTime.parse(date, formatter)
    return BeginsShift(dateTime, Option.fromNullable(id))
  }

  if (fall != null) {
    val (date) = fall.destructured
    val dateTime = LocalDateTime.parse(date, formatter)
    return FallsAsleep(dateTime, Option.empty())
  }

  if (wake != null) {
    val (date) = wake.destructured
    val dateTime = LocalDateTime.parse(date, formatter)
    return WakesUp(dateTime, Option.empty())
  }

  throw IllegalStateException("Couldn't parse line $this")
}


fun generateAndOrderLinesByDate(lines: List<String>): List<RawInformation> =
  lines.map { it.rawInformation() }.sortedBy { it.date }

fun generateGuardShifts(lines: List<String>): List<Sheet> =
  generateAndOrderLinesByDate(lines)
    .foldLeft(Pair(None, listOf())) { acc: Pair<Option<String>, List<RawInformation>>, rawInformation ->
      when (rawInformation) {
        is BeginsShift -> Pair(rawInformation.guardId, acc.second)
        is FallsAsleep -> Pair(acc.first, acc.second.plus(rawInformation.copy(guardId = acc.first)))
        is WakesUp -> Pair(acc.first, acc.second.plus(rawInformation.copy(guardId = acc.first)))
        else -> TODO()
      }
    }
    .second
    .collect({ r: RawInformation ->
      Pair(
        r.guardId.getOrElse { "" },
        r
      )
    }.toPartialFunction { it.guardId.isDefined() })
    .groupBy { it.first }
    .mapValues { entry: Map.Entry<String, List<Pair<String, RawInformation>>> ->
      entry.value.map { it.second }.fold(
        Pair(
          None,
          emptyList()
        )
      ) { acc: Pair<Option<RawInformation>, List<Pair<LocalDateTime, LocalDateTime>>>, rawInformation: RawInformation ->
        when (rawInformation) {
          is FallsAsleep -> acc.copy(first = Some(rawInformation))
          is WakesUp -> {
            when (acc.first) {
              is Some -> {
                val fallsAsleep = (acc.first as Some<RawInformation>).t
                Pair(None, acc.second.plus(Pair(fallsAsleep.date, rawInformation.date)))
              }
              else -> acc
            }
          }
          else -> acc
        }
      }
    }
    .map { Sheet(it.key, it.value.second) }


fun findMostAsleepWithMinutes(raws: List<Sheet>): Pair<String, Int> {
  val mostAsleep = raws.maxBy { sheet: Sheet ->
    sheet.sleeps.fold(0) { acc: Long, pair: Pair<LocalDateTime, LocalDateTime> ->
      acc + Duration.between(pair.first, pair.second).toMinutes()
    }
  }!!

  val minuteMostAsleep = mostAsleep.sleeps.flatMap { it.first.minute until it.second.minute }.groupBy { it }
    .maxBy { it.value.size }!!.key

  return Pair(mostAsleep.id, minuteMostAsleep)
}

fun findGuardWhoSleepsTheSameMinute(raws: List<Sheet>): Pair<String, Int> {
  val guard = raws.map { sheet ->
    val minuteMostAsleep = sheet.sleeps.flatMap { pair -> pair.first.minute until pair.second.minute }
      .groupBy { minute -> minute }
      .maxBy { entry -> entry.value.size }!!
    Tuple3(sheet.id, minuteMostAsleep.key, minuteMostAsleep.value.size)
  }
    .maxBy { it.c }!!

  return Pair(guard.a, guard.b)
}
