package io.pg.advent.day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DayOneTest {

  @Test
  fun `Should return the start frequency if there is no input`() {
    // Given
    val start = 2
    val input = emptyList<String>()

    // When
    val result = findFrequencyPartOne(start, input)

    // Then
    assertEquals(start, result)
  }

  @Test
  fun `Should return the start frequency plus the only value in the input`() {
    // Given
    val start = 1
    val input = listOf("4")

    // When
    val result = findFrequencyPartOne(start, input)

    // Then
    assertEquals(5, result)
  }

  @Test
  fun `Should return the start frequency plus the sum of the values in the input`() {
    // Given
    val start = 0
    val input = listOf("1", "-2", "3", "1")

    // When
    val result = findFrequencyPartOne(start, input)

    // Then
    assertEquals(3, result)
  }

  @Test
  fun `Should wait to have a 2nd iteration of the same frequency`() {
    val start = 0
    val input1 = listOf("1", "-2", "3", "1")
    val input2 = listOf("1", "-1")
    val input3 = listOf("3", "3", "4", "-2", "-4")
    val input4 = listOf("-6", "3", "8", "5", "-6")
    val input5 = listOf("7", "7", "-2", "-7", "-4")

    // When
    val result1 = findFrequencyPartTwo(start, input1)
    val result2 = findFrequencyPartTwo(start, input2)
    val result3 = findFrequencyPartTwo(start, input3)
    val result4 = findFrequencyPartTwo(start, input4)
    val result5 = findFrequencyPartTwo(start, input5)

    // Then
    assertEquals(2, result1)
    assertEquals(0, result2)
    assertEquals(10, result3)
    assertEquals(5, result4)
    assertEquals(14, result5)
  }

}
