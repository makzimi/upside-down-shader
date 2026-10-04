# Upside-Down Shader

An Android Compose library that applies a mysterious "Upside-Down" visual effect with a colour grade, drifting snow and growing vines.

<img width="300px" src="https://github.com/user-attachments/assets/98d47bfa-98b3-43a1-bcc5-ec9c4799cc17">

## Project Structure

- **`upsidedown-shader/`** - Library module with AGSL shader implementation
- **`sample/`** - Demo app showcasing the shader with a dark cyberpunk theme

## Installation

```kotlin
dependencies {
    implementation("io.github.makzimi:upsidedown-shader:0.1.0")
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

The sample also plays the glitch transition before the Upside Down, using [glitch-shader](https://github.com/makzimi/glitch-shader).
