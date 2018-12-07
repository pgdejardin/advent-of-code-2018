package io.pg.advent.day3

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3Test {

  @Test
  fun `Should transform an input with coord 0, 0`() {
    val input = "#1 @ 0,0: 1x1"
    val coords = transformInputToCoord(input)
    val expected = listOf(Pair(0, 0))

    assertEquals(expected, coords)
  }

  @Test
  fun `Should transform an input into List of Coord`() {
    val input = "#1 @ 1,3: 4x4"
    val coords = transformInputToCoord(input)
    val expected = listOf(
      Pair(1, 3), Pair(2, 3), Pair(3, 3), Pair(4, 3),
      Pair(1, 4), Pair(2, 4), Pair(3, 4), Pair(4, 4),
      Pair(1, 5), Pair(2, 5), Pair(3, 5), Pair(4, 5),
      Pair(1, 6), Pair(2, 6), Pair(3, 6), Pair(4, 6)
    ).sortedBy { it.first }

    assertEquals(expected, coords)
  }

  @Test
  fun `Should parse an input and return a Claim which is origin (0, 0)`() {
    val input = "#1 @ 0,0: 1x1"
    val claim = input.toClaim()
    val expected = Claim("1", 0, 0, 1, 1)

    assertEquals(expected, claim)
  }

  @Test
  fun `Should parse an input and return a Claim`() {
    val input = "#1 @ 1,3: 4x4"
    val claim = input.toClaim()
    val expected = Claim("1", 1, 3, 4, 4)

    assertEquals(expected, claim)
  }

  @Test
  fun `Should get coordinates of the claim`() {
    val coords = "#1 @ 1,3: 4x4".toClaim().coordinates()
    val expected = listOf(
      Pair(1, 3), Pair(2, 3), Pair(3, 3), Pair(4, 3),
      Pair(1, 4), Pair(2, 4), Pair(3, 4), Pair(4, 4),
      Pair(1, 5), Pair(2, 5), Pair(3, 5), Pair(4, 5),
      Pair(1, 6), Pair(2, 6), Pair(3, 6), Pair(4, 6)
    ).sortedBy { it.first }

    assertEquals(expected, coords)
  }

  @Test
  fun `Should calculate inches overlap`() {
    val lines = listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")
    val expected = 4
    val inches = calculateFabricOverlap(lines)

    assertEquals(expected, inches)
  }

  @Test
  fun `Should get ID of un-overlapping fabric`() {
    val lines = listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")
    val expected = "3"
    val id = findIDofNotOverlapFabric(lines)

    assertEquals(expected, id)
  }
}
