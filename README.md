# Upside-Down Shader

An Android Compose library that applies a mysterious "Upside-Down" visual effect with animated particles and color manipulation.

<img width="300px" src="https://github.com/user-attachments/assets/98d47bfa-98b3-43a1-bcc5-ec9c4799cc17">

## Project Structure

- **`upsidedown-shader/`** - Library module with AGSL shader implementation
- **`sample/`** - Demo app showcasing the shader with a dark cyberpunk theme

## Usage

```kotlin
Modifier.shaderUpsideDown(darknessIntensity = 0.4f)
```

The shader creates the effect by shifting colors to blue-cyan tones, rendering 80+ floating particles, and applying atmospheric adjustments.

