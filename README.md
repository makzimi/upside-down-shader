# Upside-Down Shader

[![Maven Central](https://img.shields.io/maven-central/v/io.github.makzimi/upsidedown-shader)](https://central.sonatype.com/artifact/io.github.makzimi/upsidedown-shader)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An Android Compose library that applies a mysterious "Upside-Down" visual effect with a colour grade, drifting snow and growing vines.

<img width="300px" src="https://github.com/user-attachments/assets/e7f6033b-be22-40da-bc05-0305f1612b82">

## Project Structure

- **`upsidedown-shader/`** - Library module with AGSL shader implementation
- **`sample/`** - Demo app showcasing the shader with a dark cyberpunk theme

## Installation

The library is on Maven Central. Make sure `mavenCentral()` is in your repositories in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

Add the dependency:

```kotlin
dependencies {
    implementation("io.github.makzimi:upsidedown-shader:0.1.0")
}
```

Or with a version catalog:

```toml
[versions]
upsidedownShader = "0.1.0"

[libraries]
upsidedown-shader = { group = "io.github.makzimi", name = "upsidedown-shader", version.ref = "upsidedownShader" }
```

```kotlin
dependencies {
    implementation(libs.upsidedown.shader)
}
```

Requires Android 14 (`minSdk 34`): the vines are drawn with `Canvas.drawMesh` and an AGSL vertex shader.

## Usage

```kotlin
Modifier.shaderUpsideDown(
    darknessIntensity = 0.4f,
    particles = true, // snow
    vines = true,
)
```

`shaderUpsideDown` combines three modifiers that can also be used on their own:

- `upsideDownGrade()` - shifts colours to blue-cyan tones, desaturates, darkens and adds contrast.
- `upsideDownSnow()` - three layers of drifting snow on a jittered grid. Every pixel evaluates three flakes, however many are on screen.
- `upsideDownVines()` - four black vines growing from the edges and retracting, drawn as meshes with an AGSL vertex shader.

Keep the snow and the vines outside the grade (`upsideDownSnow().upsideDownVines().upsideDownGrade()`), so they are drawn on top instead of being colour-graded.

The sample also plays a glitch transition before the Upside Down. It uses [glitch-shader](https://github.com/makzimi/glitch-shader) from Maven Central (`io.github.makzimi:glitch-shader`).
