package io.pg.advent.day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {

  @Test
  fun `Should compare two char and say if they match`() {
    val unit1 = 'a'
    val unit2 = 'A'
    val unit3 = 'b'

    val match1 = unit1.unitMatch(unit2)
    val match2 = unit1.unitMatch(unit3)

    assertEquals(true, match1)
    assertEquals(false, match2)
  }

  @Test
  fun `Polymer should react itself to give a final polymer`() {
    val polymer = "dabAcCaCBAcCcaDA"
    val expected = "dabCBAcaDA"

    val newPolymer = polymer.polymerReaction()

    assertEquals(expected, newPolymer)
  }

  @Test
  fun `Should get a Pair with the size of min polymer and polymer`() {
    val polymer = "dabAcCaCBAcCcaDA"
    val expercted = 4

    val min = polymer.minPolymerSize()

    assertEquals(4, min)
  }

}
