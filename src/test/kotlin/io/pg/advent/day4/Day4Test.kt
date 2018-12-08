package io.pg.advent.day4

import arrow.core.Option
import arrow.core.getOrElse
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals

internal class Day4Test {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m", Locale.ENGLISH)

  private val lines = listOf(
    "[1518-11-01 00:00] Guard #10 begins shift",
    "[1518-11-01 00:05] falls asleep",
    "[1518-11-01 00:25] wakes up",
    "[1518-11-01 00:30] falls asleep",
    "[1518-11-01 00:55] wakes up",
    "[1518-11-01 23:58] Guard #99 begins shift",
    "[1518-11-02 00:40] falls asleep",
    "[1518-11-02 00:50] wakes up",
    "[1518-11-03 00:05] Guard #10 begins shift",
    "[1518-11-03 00:24] falls asleep",
    "[1518-11-03 00:29] wakes up",
    "[1518-11-04 00:02] Guard #99 begins shift",
    "[1518-11-04 00:36] falls asleep",
    "[1518-11-04 00:46] wakes up",
    "[1518-11-05 00:03] Guard #99 begins shift",
    "[1518-11-05 00:45] falls asleep",
    "[1518-11-05 00:55] wakes up"
  )

  private val asleep = listOf(
    "11-01  #10  .....####################.....#########################.....",
    "11-02  #99  ........................................##########..........",
    "11-03  #10  ........................#####...............................",
    "11-04  #99  ....................................##########..............",
    "11-05  #99  .............................................##########....."
  )

  @Test
  fun `Should parse a line to BeginShift instance with id`() {
    val expected = BeginsShift(LocalDateTime.parse("1518-11-01 00:00", formatter), Option.just("10"))
    val shifts = lines.first().rawInformation()

    assertEquals(expected, shifts)
  }

  @Test
  fun `Should parse a line to FallsAsleep instance`() {
    val expected = FallsAsleep(LocalDateTime.parse("1518-11-01 00:05", formatter), Option.empty())
    val shifts = lines[1].rawInformation()

    assertEquals(expected, shifts)
  }

  @Test
  fun `Should parse a line to WakesUp instance `() {
    val expected = WakesUp(LocalDateTime.parse("1518-11-01 00:25", formatter), Option.empty())
    val shifts = lines[2].rawInformation()

    assertEquals(expected, shifts)
  }

  @Test
  fun `Should parse lines to List of RawInformation`() {
    val miniLines = listOf(
      "[1518-11-01 00:00] Guard #10 begins shift",
      "[1518-11-01 00:05] falls asleep",
      "[1518-11-01 00:25] wakes up",
      "[1518-11-01 23:58] Guard #99 begins shift",
      "[1518-11-02 00:40] falls asleep",
      "[1518-11-02 00:50] wakes up",
      "[1518-11-03 00:05] Guard #10 begins shift",
      "[1518-11-03 00:24] falls asleep",
      "[1518-11-03 00:29] wakes up"
    ).reversed()
    val expected = listOf(
      BeginsShift(LocalDateTime.parse("1518-11-01 00:00", formatter), Option.just("10")),
      FallsAsleep(LocalDateTime.parse("1518-11-01 00:05", formatter), Option.empty()),
      WakesUp(LocalDateTime.parse("1518-11-01 00:25", formatter), Option.empty()),
      BeginsShift(LocalDateTime.parse("1518-11-01 23:58", formatter), Option.just("99")),
      FallsAsleep(LocalDateTime.parse("1518-11-02 00:40", formatter), Option.empty()),
      WakesUp(LocalDateTime.parse("1518-11-02 00:50", formatter), Option.empty()),
      BeginsShift(LocalDateTime.parse("1518-11-03 00:05", formatter), Option.just("10")),
      FallsAsleep(LocalDateTime.parse("1518-11-03 00:24", formatter), Option.empty()),
      WakesUp(LocalDateTime.parse("1518-11-03 00:29", formatter), Option.empty())
    )

    val shifts = generateAndOrderLinesByDate(miniLines)

    assertEquals(expected, shifts)
  }

  @Test
  fun `Should get the sleeping minutes of a guard shift`() {
    val raws = listOf(
      BeginsShift(LocalDateTime.parse("1518-11-01 00:00", formatter), Option.just("10")),
      FallsAsleep(LocalDateTime.parse("1518-11-01 00:05", formatter), Option.just("10")),
      WakesUp(LocalDateTime.parse("1518-11-01 00:25", formatter), Option.just("10")),
      BeginsShift(LocalDateTime.parse("1518-11-03 00:05", formatter), Option.just("10")),
      FallsAsleep(LocalDateTime.parse("1518-11-03 00:24", formatter), Option.just("10")),
      WakesUp(LocalDateTime.parse("1518-11-03 00:29", formatter), Option.just("10"))
    ).groupBy { it.guardId.getOrElse { "0" }.toInt() }

//    val expected =
  }

  @Test
  fun `Should transform List of Input to List of Sheet`() {
    val sheets = generateGuardShifts(lines)

    val expected = listOf(
      Sheet(
        "10", listOf(
          Pair(parse("1518-11-01 00:05", formatter), parse("1518-11-01 00:25", formatter)),
          Pair(parse("1518-11-01 00:30", formatter), parse("1518-11-01 00:55", formatter)),
          Pair(parse("1518-11-03 00:24", formatter), parse("1518-11-03 00:29", formatter))
        )
      ),
      Sheet(
        "99", listOf(
          Pair(parse("1518-11-02 00:40", formatter), parse("1518-11-02 00:50", formatter)),
          Pair(parse("1518-11-04 00:36", formatter), parse("1518-11-04 00:46", formatter)),
          Pair(parse("1518-11-05 00:45", formatter), parse("1518-11-05 00:55", formatter))
        )
      )
    )

    assertEquals(expected, sheets)
  }

  @Test
  fun `Should get the guard ID and the minutes which he is the most asleep`() {
    val sheets = listOf(
      Sheet(
        "10", listOf(
          Pair(parse("1518-11-01 00:05", formatter), parse("1518-11-01 00:25", formatter)),
          Pair(parse("1518-11-01 00:30", formatter), parse("1518-11-01 00:55", formatter)),
          Pair(parse("1518-11-03 00:24", formatter), parse("1518-11-03 00:29", formatter))
        )
      ),
      Sheet(
        "99", listOf(
          Pair(parse("1518-11-02 00:40", formatter), parse("1518-11-02 00:50", formatter)),
          Pair(parse("1518-11-04 00:36", formatter), parse("1518-11-04 00:46", formatter)),
          Pair(parse("1518-11-05 00:45", formatter), parse("1518-11-05 00:55", formatter))
        )
      )
    )

    val expected = Pair("10", 24)

    assertEquals(expected, findMostAsleepWithMinutes(sheets))
  }

}
