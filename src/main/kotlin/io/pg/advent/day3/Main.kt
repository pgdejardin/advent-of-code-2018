package io.pg.advent.day3

import io.pg.advent.utils.FileUtil

fun main() {
  val lines = FileUtil.loadFile("/day3.txt")
  val inches = calculateFabricOverlap(lines)
  println("Overlap inches: $inches")
  val id = findIDofNotOverlapFabric(lines)
  println("Fabric ID which is not overlapping: $id")
}

fun findIDofNotOverlapFabric(lines: List<String>): String {
  val overlappingCoords = transformLinesToCoords(lines).flatten().groupBy { it }.filterValues { it.size > 1 }.keys
  return lines.map { it.toClaim() }.first { it.coordinates().intersect(overlappingCoords).isEmpty() }.id
}

fun calculateFabricOverlap(lines: List<String>): Int {
  val coords = transformLinesToCoords(lines)
  return coords.flatten()
    .groupBy { it }
    .filterValues { it.size > 1 }
    .size
}

fun transformLinesToCoords(lines: List<String>): List<List<Pair<Int, Int>>> = lines.map { transformInputToCoord(it) }

fun transformInputToCoord(input: String): List<Pair<Int, Int>> {
  val claim = input.toClaim()
  val xRange = claim.fromLeft..(claim.fromLeft + claim.width - 1)
  val yRange = claim.fromTop..(claim.fromTop + claim.height - 1)
  return xRange.flatMap { x -> yRange.map { y -> Pair(x, y) } }.sortedBy { it.first }
}

fun String.toClaim(): Claim {
  // "#1 @ 0,0: 1x1"
  val id = this.substring(1).substringBefore("@").trim()
  val leftAndTop = this.substringAfter("@").substringBefore(":").trim().split(",")
  val widthAndHeight = this.substringAfter(":").split("x")
  return Claim(
    id,
    leftAndTop.first().trim().toInt(),
    leftAndTop.last().trim().toInt(),
    widthAndHeight.first().trim().toInt(),
    widthAndHeight.last().trim().toInt()
  )
}

data class Claim(
  val id: String,
  val fromLeft: Int,
  val fromTop: Int,
  val width: Int,
  val height: Int
) {
  fun coordinates(): List<Pair<Int, Int>> {
    val xRange = this.fromLeft..(this.fromLeft + this.width - 1)
    val yRange = this.fromTop..(this.fromTop + this.height - 1)
    return xRange.flatMap { x -> yRange.map { y -> Pair(x, y) } }.sortedBy { it.first }
  }
}

