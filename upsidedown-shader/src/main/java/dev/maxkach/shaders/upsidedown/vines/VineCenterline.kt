package dev.maxkach.shaders.upsidedown.vines

import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

internal class VineCenterline(
  val points: FloatArray,
  val count: Int,
) {
  fun x(index: Int): Float = points[index * 2]
  fun y(index: Int): Float = points[index * 2 + 1]
}

internal const val VinesCount = 4
internal const val VineMaxPoints = 1024
internal const val VineStepPx = 4f

internal fun vinesMaxLength(screenWidth: Float, screenHeight: Float): Float =
    hypot(screenWidth, screenHeight) * 0.53f

internal fun walkVineCenterline(vine: Int, seed: UInt, screenWidth: Float, screenHeight: Float): VineCenterline {
  require(vine in 0 until VinesCount) { "vine $vine" }
  val vineU = vine.toUInt()

  var rand1 = noise2((vine * 317 + 123).toFloat(), (seed * 73u).toFloat(), seed * 11u + vineU * 13u)
  var rand2 = noise2((vine * 419 + 456).toFloat(), (seed * 97u).toFloat(), seed * 17u + vineU * 19u)
  rand1 = (rand1 + 1f) * 0.5f
  rand2 = (rand2 + 1f) * 0.5f

  var startX: Float
  var startY: Float
  var dirX: Float
  var dirY: Float
  when (vine) {
    0 -> { startX = 0f; startY = screenHeight * (0.1f + rand1 * 0.8f); dirX = 1f; dirY = 0f }
    1 -> { startX = screenWidth; startY = screenHeight * (0.1f + rand2 * 0.8f); dirX = -1f; dirY = 0f }
    2 -> { startX = screenWidth * (0.1f + rand1 * 0.8f); startY = 0f; dirX = 0f; dirY = 1f }
    else -> { startX = screenWidth * (0.1f + rand2 * 0.8f); startY = screenHeight; dirX = 0f; dirY = -1f }
  }

  val points = FloatArray(VineMaxPoints * 2)
  points[0] = startX
  points[1] = startY
  var count = 1

  val maxLen = vinesMaxLength(screenWidth, screenHeight)
  var traveled = 0f
  val jitterSeed = seed + vineU * 13u

  for (i in 1 until VineMaxPoints) {
    val prevX = points[(i - 1) * 2]
    val prevY = points[(i - 1) * 2 + 1]

    if (i > 3) {
      val n = noise2(prevX * 0.006f, prevY * 0.006f, jitterSeed)
      val jitter = (n * 12f - 6f) * (3.14159f / 180f)
      val c = cos(jitter)
      val s = sin(jitter)
      val rx = dirX * c - dirY * s
      val ry = dirX * s + dirY * c
      val length = sqrt(rx * rx + ry * ry)
      dirX = rx / length
      dirY = ry / length
    }

    val nextX = prevX + dirX * VineStepPx
    val nextY = prevY + dirY * VineStepPx
    if (nextX < 0f || nextX > screenWidth || nextY < 0f || nextY > screenHeight) break

    points[i * 2] = nextX
    points[i * 2 + 1] = nextY
    count = i + 1
    traveled += VineStepPx
    if (traveled >= maxLen) break
  }

  return VineCenterline(points, count)
}

private fun hashUint2(x0: UInt, y0: UInt): UInt {
  var x = x0 * 1664525u + 1013904223u
  var y = y0 * 1664525u + 1013904223u
  x = x xor ((y shl 5) or (y shr 27))
  y = y xor ((x shl 7) or (x shr 25))
  return x xor y
}

private fun noise2(x: Float, y: Float, seed: UInt): Float {
  val hx = x.toUInt() xor (seed * 7411u)
  val hy = y.toUInt() xor (seed * 1931u)
  return (hashUint2(hx, hy) and 0xFFFFFFu).toFloat() / 0xFFFFFFu.toFloat()
}
