package dev.maxkach.shaders.upsidedown.vines

import kotlin.random.Random

internal class VinesCycle(
  private val pauseSeconds: (ClosedFloatingPointRange<Double>) -> Double = { Random.nextDouble(it.start, it.endInclusive) },
) {
  private sealed interface Phase {
    data object Grow : Phase
    data object Retract : Phase
    data class Delay(val until: Double) : Phase
  }

  private var phase: Phase? = null
  private var phaseStart = 0.0
  private var completedCycles = 0

  var seed: UInt = BaseSeed
    private set

  fun progress(atSeconds: Double): Float {
    val current = phase ?: run {
      phase = Phase.Delay(until = atSeconds + FirstDelaySeconds)
      return 0f
    }
    val elapsed = atSeconds - phaseStart
    return when (current) {
      Phase.Grow -> {
        val progress = easeInOutCubic(minOf(elapsed / CycleSeconds, 1.0))
        if (progress >= 1.0) {
          phase = Phase.Retract
          phaseStart = atSeconds
        }
        progress.toFloat()
      }

      Phase.Retract -> {
        val progress = easeInOutCubic(maxOf(1.0 - elapsed / CycleSeconds, 0.0))
        if (progress <= 0.0) {
          completedCycles += 1
          seed = BaseSeed + completedCycles.toUInt() * SeedStep
          phase = Phase.Delay(until = atSeconds + pauseSeconds(PauseRange))
        }
        progress.toFloat()
      }

      is Phase.Delay -> {
        if (atSeconds >= current.until) {
          phase = Phase.Grow
          phaseStart = atSeconds
        }
        0f
      }
    }
  }

  private fun easeInOutCubic(t: Double): Double {
    if (t < 0.5) return 4 * t * t * t
    val f = 2 * t - 2
    return 1 + f * f * f / 2
  }

  companion object {
    const val BaseSeed: UInt = 1337u
    private const val SeedStep: UInt = 137u
    private const val CycleSeconds = 6.0
    private const val FirstDelaySeconds = 4.0
    private val PauseRange = 2.0..4.0
  }
}
