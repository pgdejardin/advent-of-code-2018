package io.pg.advent.day6

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day6Test {

  private val input = listOf(
    "1, 1",
    "1, 6",
    "8, 3",
    "3, 4",
    "5, 5",
    "8, 9"
  )

  private val coords = listOf(
    Coordinate(1, 1, 1),
    Coordinate(2, 1, 6),
    Coordinate(3, 8, 3),
    Coordinate(4, 3, 4),
    Coordinate(5, 5, 5),
    Coordinate(6, 8, 9)
  )

  private val maxX = 8
  private val maxY = 9
  private val maxDistance = 32

  @Test
  fun `Should parse input`() {
    val coordsToTest = parseInput(input)

    assertEquals(coords, coordsToTest)
  }

  @Test
  fun `Should get a Pair on minX and maxY`() {
    val lengthOfXAndY = maxXAndMaxY(coords)

    assertEquals(Pair(8, 9), lengthOfXAndY)
  }

  @Test
  fun `Should get the nearest point id of x and y`() {
    val x1 = 0
    val y1 = 0
    val x2 = 1
    val y2 = 4

    val id1 = nearestCoordOf(x1, y1, coords)
    val id2 = nearestCoordOf(x2, y2, coords)

    assertEquals(1, id1)
    assertEquals(-1, id2)
  }

  @Test
  fun `Should have a representation of the area`() {
    val area = mapAreaOfCoords(maxX, maxY, coords)
    val expected = listOf(Coordinate(-1, 0, 0, 1, true, 54))

    assertEquals(expected.first(), area.first())
  }

  @Test
  fun `Should say for a given point if one his coord is at edge`() {
    val isAtEdge1 = Pair(3, 0).isAtEdge(maxX, maxY)
    val isAtEdge2 = Pair(3, 1).isAtEdge(maxX, maxY)

    assertEquals(true, isAtEdge1)
    assertEquals(false, isAtEdge2)
  }

  @Test
  fun `Should give the size of the max area which is finite`() {
    val coords = mapAreaOfCoords(maxX, maxY, this.coords)
    val maxFiniteArea = maxOfFiniteArea(coords)

    assertEquals(17, maxFiniteArea)
  }

  @Test
  fun `Should give the size of the max area which is finite from the input`() {
    val maxFiniteArea = day6Part1(input)

    assertEquals(17, maxFiniteArea)
  }

  @Test
  fun `Should resolve part 2 of day 6 from the input`() {
    val size = day6Part2(input)

    assertEquals(16, size)
  }

  @Test
  fun `Should get the distance from all points for a give coord`() {
    val distance = Pair(4, 3).getTotalDistance(coords)

    assertEquals(30, distance)
  }

  @Test
  fun `Should get the list of coord which are in the region`() {
    val area = mapAreaOfCoords(maxX, maxY, coords)
    val region = area.getRegion(maxDistance)

    assertEquals(16, region.size)
  }
}
