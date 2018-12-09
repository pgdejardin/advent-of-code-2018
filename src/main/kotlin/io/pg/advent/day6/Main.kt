package io.pg.advent.day6

import arrow.core.Some
import arrow.instances.list.foldable.exists
import arrow.syntax.collections.firstOption
import io.pg.advent.utils.FileUtil
import kotlin.math.absoluteValue

fun main() {
  val lines = FileUtil.loadFile("/day6.txt")
  val sizePartOne = day6Part1(lines)
  println("What is the size of the largest area that isn't infinite?")
  println("It is $sizePartOne")
  println("---------------------------------------------------------")
  val sizePartTwo = day6Part2(lines)
  println("What is the size of the region containing all locations which have a total distance to all given coordinates of less than 10000?")
  println("It is $sizePartTwo")
}

data class Coordinate(
  val id: Int,
  val x: Int,
  val y: Int,
  val nearest: Int = id,
  val atEdge: Boolean = false,
  val total: Int = 0
)

fun parseInput(lines: List<String>) = lines.mapIndexed { index, s ->
  val coord = s.split(",").map { it.trim() }
  Coordinate(index + 1, coord.first().toInt(), coord[1].toInt())
}

fun maxXAndMaxY(coords: List<Coordinate>): Pair<Int, Int> {
  val (_, x) = coords.maxBy { it.x }!!
  val (_, _, y) = coords.maxBy { it.y }!!
  return Pair(x, y)
}

fun mapAreaOfCoords(maxX: Int, maxY: Int, points: List<Coordinate>): List<Coordinate> {
  return (0..maxX).map { x ->
    (0..maxY).map { y ->
      val isPoint = points.firstOption { it.x == x && it.y == y }
      when (isPoint) {
        is Some -> isPoint.t.copy(
          atEdge = Pair(isPoint.t.x, isPoint.t.y).isAtEdge(maxX, maxY),
          total = Pair(isPoint.t.x, isPoint.t.y).getTotalDistance(points)
        )
        else -> Coordinate(
          id = -1,
          x = x,
          y = y,
          nearest = nearestCoordOf(x, y, points),
          atEdge = Pair(x, y).isAtEdge(maxX, maxY),
          total = Pair(x, y).getTotalDistance(points)
        )
      }
    }
  }.flatten()
}

fun nearestCoordOf(x: Int, y: Int, points: List<Coordinate>): Int {
  val distance: Map.Entry<Int, List<Pair<Int, Int>>> = points.map {
    val distance = (it.x - x).absoluteValue + (it.y - y).absoluteValue
    Pair(it.id, distance)
  }
    .groupBy { it.second }
    .minBy { it.key }!!

  return if (distance.value.size == 1) distance.value.first().first else -1
}

fun Pair<Int, Int>.isAtEdge(maxX: Int, maxY: Int): Boolean {
  return this.first == 0 || this.first == maxX || this.second == 0 || this.second == maxY
}

fun maxOfFiniteArea(coords: List<Coordinate>): Int {
  val finiteAreas: Map<Int, List<Coordinate>> =
    coords.groupBy { it.nearest }.filterNot { it.value.exists { c -> c.atEdge } }
  val max = finiteAreas.maxBy { it.value.size }!!
  return max.value.size
}

fun Pair<Int, Int>.getTotalDistance(points: List<Coordinate>): Int {
  return points.fold(0) { acc, coordinate ->
    acc + ((this.first - coordinate.x).absoluteValue + (this.second - coordinate.y).absoluteValue)
  }
}

fun List<Coordinate>.getRegion(maxDistance: Int): List<Coordinate> {
  return this.filter { it.total < maxDistance }
}

fun day6Part1(lines: List<String>): Int {
  val parsedInput = parseInput(lines)
  val (maxX, maxY) = maxXAndMaxY(parsedInput)
  val area = mapAreaOfCoords(maxX, maxY, parsedInput)
  return maxOfFiniteArea(area)
}

fun day6Part2(lines: List<String>): Int {
  val parsedInput = parseInput(lines)
  val (maxX, maxY) = maxXAndMaxY(parsedInput)
  val area = mapAreaOfCoords(maxX, maxY, parsedInput)
  return area.getRegion(10000).size
}
