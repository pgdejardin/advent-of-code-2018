package io.pg.advent.day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DayTwoPartTwoTest {

  @Test
  fun `Should return a checksum`() {
    // Given
    val lines = listOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")

    // When
    val letters = commonsLetterMinusOne(lines)

    // Then
    assertEquals("fgij", letters)
  }

  @Test
  fun `Should compare to string and say if only one letter is diverging`() {
    // Given
    val str = "abcde"

    // When
    val trueResult = "amcde".hasJustOneLetterChanging(str)
    val falseResult = "amcdf".hasJustOneLetterChanging(str)

    // Then
    assertTrue { trueResult }
    assertFalse { falseResult }
  }

  @Test
  fun `Should compare to string return all commons letter`() {
    // Given
    val str = "abcde"

    // When
    val result = "amcde".getCommonsCharMaxOneCharDiverging(str)

    // Then
    assertEquals("acde", result)
  }
}
