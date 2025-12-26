package dev.maxkach.shaders.upsidedown

import org.intellij.lang.annotations.Language

@Language("AGSL")
val UPSIDE_DOWN_SHADER = """
    uniform shader image;
    uniform float2 imageSize;
    uniform float time;
    uniform float darknessIntensity;

    const int MAX_PARTICLES = 80;

    // Random function for particle generation
    float random(float2 seed) {
        return fract(sin(dot(seed, float2(12.9898, 78.233))) * 43758.5453123);
    }

    // Generate particle position and properties
    float4 getParticle(int id, float time) {
        float seed = float(id);

        // Random starting position
        float startX = random(float2(seed, 1.0));
        float startY = random(float2(seed, 2.0));

        // Random velocities - floating in all directions, very slowly
        float speedY = (random(float2(seed, 3.0)) - 0.5) * 0.030; // Random vertical drift
        float speedX = (random(float2(seed, 4.0)) - 0.5) * 0.030; // Random horizontal drift

        // Particle size - bigger particles
        float size = random(float2(seed, 5.0)) * 20.0 + 4.0; // Size between 4-24 pixels

        // Animate position
        float x = fract(startX + speedX * time);
        float y = fract(startY + speedY * time);

        return float4(x, y, size, 1.0);
    }

    // Check if current pixel is part of a particle
    float renderParticles(float2 uv, float time) {
        float particleAlpha = 0.0;

        for (int i = 0; i < MAX_PARTICLES; i++) {
            float4 particle = getParticle(i, time);
            float2 particlePos = particle.xy;
            float particleSize = particle.z;

            // Distance from current pixel to particle center
            float2 diff = (uv - particlePos) * imageSize;
            float dist = length(diff);

            // Render particle as soft circle
            if (dist < particleSize) {
                float alpha = 1.0 - (dist / particleSize);
                alpha = alpha * alpha; // Smoother falloff
                particleAlpha = max(particleAlpha, alpha * 0.6);
            }
        }

        return particleAlpha;
    }

    // Convert RGB to HSV
    vec3 rgb2hsv(vec3 c) {
        vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
        vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
        vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

        float d = q.x - min(q.w, q.y);
        float e = 1.0e-10;
        return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
    }

    // Convert HSV to RGB
    vec3 hsv2rgb(vec3 c) {
        vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
        vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
        return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / imageSize;

        // Get original image color
        half4 originalColor = image.eval(fragCoord);
        vec3 color = originalColor.rgb;

        // Convert to HSV for manipulation
        vec3 hsv = rgb2hsv(color);

        // Adjust hue - shift towards blue/cyan for upside-down vibe
        hsv.x = hsv.x + 0.55; // Shift hue towards blue-green
        if (hsv.x > 1.0) hsv.x -= 1.0;

        // Increase saturation for more intense color
        hsv.y = hsv.y * 0.2;

        // Slightly reduce brightness but not too much
        hsv.z = hsv.z * (1.0 - darknessIntensity * 0.5);

        // Convert back to RGB
        vec3 adjustedColor = hsv2rgb(hsv);

        // Increase contrast
        adjustedColor = (adjustedColor - 0.5) * 1.9 + 0.5;
        adjustedColor = clamp(adjustedColor, 0.0, 1.0);

        // Subtle blue-green tint overlay (much lighter than before)
        vec3 tintColor = vec3(0.2, 0.3, 0.35);
        adjustedColor = mix(adjustedColor, tintColor, darknessIntensity * 0.15);

        // Render particles
        float particleAlpha = renderParticles(uv, time);

        // Particle color (whitish-gray dust)
        vec3 particleColor = vec3(0.7, 0.7, 0.65);

        // Composite: adjusted image + particles
        vec3 finalColor = mix(adjustedColor, particleColor, particleAlpha);

        return half4(finalColor, originalColor.a);
    }
"""
