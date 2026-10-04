# AGENTS.md

## Project

- `upsidedown-shader/` is the library, published to Maven Central as `io.github.makzimi:upsidedown-shader`.
- `sample/` is the demo app. It is not published.
- Build with JDK 17. Gradle 8.13 does not run on newer JDKs:
  `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`

## Dependencies

- The library has `minSdk 34` because the vines use `Canvas.drawMesh`.
- The sample uses `io.github.makzimi:glitch-shader` from Maven Central, from
  https://github.com/makzimi/glitch-shader. Its version is `glitchShader` in
  `gradle/libs.versions.toml`. The glitch effect belongs in the sample only, not in the library.

## Code style

- Keep comments to a minimum. Only comment code that is hard to understand without it.
- Keep PR descriptions short: only what changed, in plain language.
- No "Co-Authored-By: Claude" or "Generated with Claude Code" in commits or PRs.

## Releasing a new version

Publishing uses the `com.vanniktech.maven.publish` plugin. The Maven Central token
(`mavenCentralUsername`, `mavenCentralPassword`) and the signing key (`signingInMemoryKey`,
`signingInMemoryKeyPassword`) are in `~/.gradle/gradle.properties` on the maintainer's machine.
Never commit them.

1. Merge the changes to `main` and pull:
   `git checkout main && git pull`
2. Bump the version in `coordinates(...)` in `upsidedown-shader/build.gradle.kts`.
   Update the version in `README.md` too. Commit and push.
3. Optional check. Publish locally and look at the POM in
   `~/.m2/repository/io/github/makzimi/upsidedown-shader/<version>/`:
   `./gradlew :upsidedown-shader:publishToMavenLocal`
4. Upload to Maven Central:
   `./gradlew :upsidedown-shader:publishToMavenCentral`
5. Open https://central.sonatype.com, go to View Deployments, wait for VALIDATED and press Publish.
   If it is FAILED, open the deployment to see the reason.
6. Tag the release:
   `git tag v<version> && git push origin v<version>`
7. Wait until the files appear at
   https://repo1.maven.org/maven2/io/github/makzimi/upsidedown-shader/<version>/
   This usually takes 10 to 30 minutes.

A published version can not be overwritten or deleted. To fix a bad release, publish a new version.
