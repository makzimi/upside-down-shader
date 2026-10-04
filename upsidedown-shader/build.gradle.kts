plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.mavenPublish)
}

android {
    namespace = "dev.maxkach.shaders.upsidedown"
    compileSdk = 36

    defaultConfig {
        minSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
}

mavenPublishing {
    publishToMavenCentral()
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    coordinates("io.github.makzimi", "upsidedown-shader", "0.1.0")

    pom {
        name.set("Upside-Down Shader")
        description.set("A Stranger Things style Upside Down effect for Jetpack Compose: AGSL colour grade, grid snow and mesh-shader vines.")
        inceptionYear.set("2025")
        url.set("https://github.com/makzimi/upside-down-shader")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/makzimi/upside-down-shader/blob/main/LICENSE")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("makzimi")
                name.set("Maxim Kachinkin")
                url.set("https://github.com/makzimi")
            }
        }
        scm {
            url.set("https://github.com/makzimi/upside-down-shader")
            connection.set("scm:git:git://github.com/makzimi/upside-down-shader.git")
            developerConnection.set("scm:git:ssh://git@github.com/makzimi/upside-down-shader.git")
        }
    }
}
