package dev.maxkach.shaders.upsidedown.vines

import org.intellij.lang.annotations.Language

// A mesh vertex shader cannot read other vertices, so each vertex carries its prev and next points.
internal const val VineVertexFloats = 8
internal const val VineVertexStrideBytes = VineVertexFloats * Float.SIZE_BYTES

internal fun packVineVertices(line: VineCenterline): FloatArray {
  val out = FloatArray(line.count * 2 * VineVertexFloats)
  var o = 0
  for (i in 0 until line.count) {
    val prev = maxOf(i - 1, 0)
    val next = minOf(i + 1, line.count - 1)
    for (side in floatArrayOf(1f, -1f)) {
      out[o++] = line.x(i)
      out[o++] = line.y(i)
      out[o++] = line.x(prev)
      out[o++] = line.y(prev)
      out[o++] = line.x(next)
      out[o++] = line.y(next)
      out[o++] = i.toFloat()
      out[o++] = side
    }
  }
  return out
}

@Language("AGSL")
internal val VINES_VERTEX_SHADER = """
    uniform float2 screenSize;
    uniform float progress;
    uniform float mainWidth;
    uniform float pointCount;

    Varyings main(const Attributes attributes) {
        Varyings varyings;

        float maxLen = length(screenSize) * 0.53;
        float targetLen = maxLen * progress;
        float steps = targetLen / 4.0;
        float grown = steps > 0.0 ? ceil(steps) : 0.0;
        float validPoints = min(pointCount, 1.0 + grown);

        float i = attributes.meta.x;
        float side = attributes.meta.y;
        float2 p = attributes.position;
        float2 d = float2(1.0, 0.0);
        float halfWidth = 0.0;

        if (i >= validPoints) {
            // Not grown yet: collapse onto the previous point with zero width.
            p = attributes.prev;
        } else {
            if (i == 0.0 && validPoints > 1.0) {
                d = attributes.next - p;
            } else if (i == validPoints - 1.0 && i > 0.0) {
                d = p - attributes.prev;
            } else if (i > 0.0 && i < validPoints - 1.0) {
                d = attributes.next - attributes.prev;
            }
            float t = i / max(1.0, validPoints - 1.0);
            halfWidth = mix(mainWidth, 6.0, t) * 0.5;
        }

        float2 nrm = length(d) > 0.001 ? float2(-d.y, d.x) / length(d) : float2(0.0, 1.0);
        varyings.position = p + nrm * halfWidth * side;
        return varyings;
    }
"""

@Language("AGSL")
internal val VINES_FRAGMENT_SHADER = """
    float2 main(const Varyings varyings, out float4 color) {
        color = float4(0.0, 0.0, 0.0, 0.92);
        return varyings.position;
    }
"""
