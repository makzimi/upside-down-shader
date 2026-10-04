package dev.maxkach.shaders.upsidedown

import org.intellij.lang.annotations.Language

@Language("AGSL")
internal val UPSIDE_DOWN_SHADER = """
    uniform shader image;
    uniform float2 imageSize;
    uniform float darknessIntensity;

    vec3 rgb2hsv(vec3 c) {
        vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
        vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
        vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

        float d = q.x - min(q.w, q.y);
        float e = 1.0e-10;
        return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
    }

    vec3 hsv2rgb(vec3 c) {
        vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
        vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
        return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
    }

    half4 main(float2 fragCoord) {
        half4 originalColor = image.eval(fragCoord);
        vec3 color = originalColor.a > 0.0 ? originalColor.rgb / originalColor.a : vec3(0.0);

        vec3 hsv = rgb2hsv(color);

        hsv.x = hsv.x + 0.55;
        if (hsv.x > 1.0) hsv.x -= 1.0;

        hsv.y = hsv.y * 0.2;

        hsv.z = hsv.z * (1.0 - darknessIntensity * 0.5);

        vec3 adjustedColor = hsv2rgb(hsv);

        adjustedColor = (adjustedColor - 0.5) * 1.9 + 0.5;
        adjustedColor = clamp(adjustedColor, 0.0, 1.0);

        vec3 tintColor = vec3(0.2, 0.3, 0.35);
        adjustedColor = mix(adjustedColor, tintColor, darknessIntensity * 0.15);

        // RenderEffect expects premultiplied output.
        return half4(adjustedColor * originalColor.a, originalColor.a);
    }
"""
