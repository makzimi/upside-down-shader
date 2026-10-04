package dev.maxkach.shaders.upsidedown.snow

import org.intellij.lang.annotations.Language

@Language("AGSL")
internal val SNOW_SHADER = """
    uniform float2 size;
    uniform float time;

    float2 hash2(float2 p) {
        float3 p3 = fract(float3(p.xyx) * float3(0.1031, 0.1030, 0.0973));
        p3 += dot(p3, p3.yzx + 33.33);
        return fract(float2((p3.x + p3.y) * p3.z, (p3.y + p3.z) * p3.x));
    }

    float snowLayer(float2 p, float t, float scale, float2 velocity) {
        float2 q = p * scale - velocity * t;
        float2 cell = floor(q);
        float2 f = q - cell;
        float2 rnd = hash2(cell);
        float2 center = float2(0.2) + 0.6 * rnd;
        center.x += 0.08 * sin(t * (0.6 + rnd.y) + rnd.x * 6.2831);
        float radius = 0.05 + 0.1 * rnd.y;
        float d = length(f - center);
        float core = smoothstep(radius, radius * 0.2, d);
        float halo = smoothstep(radius * 2.4, 0.0, d) * 0.25;
        return (core + halo) * (0.35 + 0.65 * rnd.x);
    }

    half4 main(float2 fragCoord) {
        float2 unit = fragCoord / max(size, float2(1.0));
        float aspect = size.x / max(size.y, 1.0);
        float2 p = float2(unit.x * aspect, unit.y);
        float flakes = 0.8 * snowLayer(p, time, 8.0, float2(0.05, 0.22));
        flakes += 0.9 * snowLayer(p, time + 40.0, 14.0, float2(0.03, 0.1));
        flakes += 0.7 * snowLayer(p, time + 80.0, 22.0, float2(-0.04, 0.16));
        half a = half(clamp(flakes, 0.0, 1.0) * 0.9);
        return half4(a, a, a, a);
    }
"""
