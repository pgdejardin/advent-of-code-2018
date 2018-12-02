package io.pg.advent.day2

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DayTwoPartOneTest {

  @Test
  fun `hasTwoSameLetter should return false if empty string`() {
    // Given / When
    val result = "".hasNSameLetters(2)

    // Then
    assertFalse(result)
  }

  @Test
  fun `hasTwoSameLetter should return false if no char are common`() {
    // Given / When
    val result = "abcde".hasNSameLetters(2)

    // Then
    assertFalse(result)
  }

  @Test
  fun `hasTwoSameLetter should return true if at least one char comes two times`() {
    // Given / When
    val result = "abcdea".hasNSameLetters(2)

    // Then
    assertTrue(result)
  }

  @Test
  fun `hasThreeSameLetter should return false if empty string`() {
    // Given / When
    val result = "".hasNSameLetters(3)

    // Then
    assertFalse(result)
  }

  @Test
  fun `hasThreeSameLetter should return false if no char are common`() {
    // Given / When
    val result = "abcde".hasNSameLetters(3)

    // Then
    assertFalse(result)
  }

  @Test
  fun `hasThreeSameLetter should return true if at least one char comes three times`() {
    // Given / When
    val result = "abcdeaea".hasNSameLetters(3)

    // Then
    assertTrue(result)
  }

  @Test
  fun `Should return Pair(0, 0) if no strings has two and three letters in common`() {
    // Given
    val lines = listOf("abcde", "bcdef", "cdefg", "defgh")

    // When
    val (countTwo, countThree) = lines.countTwoAndThreeLetters()

    // Then
    assertEquals(0, countTwo)
    assertEquals(0, countThree)
  }

  @Test
  fun `Should return Pair(1, 0) if one strings has two letters in common but none has three`() {
    // Given
    val lines = listOf("abcde", "bcdef", "cdefge", "defgh")

    // When
    val (countTwo, countThree) = lines.countTwoAndThreeLetters()

    // Then
    assertEquals(1, countTwo)
    assertEquals(0, countThree)
  }

  @Test
  fun `Should return Pair(2, 3)`() {
    // Given
    val lines = listOf("aabbccc", "bbcdddef", "cccddd", "dddde")

    // When
    val (countTwo, countThree) = lines.countTwoAndThreeLetters()

    // Then
    assertEquals(2, countTwo)
    assertEquals(3, countThree)
  }

  @Test
  fun `Should return a checksum`() {
    // Given
    val lines = listOf("aabbccc", "bbcdddef", "cccddd", "dddde")

    // When
    val counts = lines.countTwoAndThreeLetters()
    val checksum = generateChecksum(counts)

    // Then
    assertEquals(6, checksum)
  }
}
