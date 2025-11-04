plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 23
        targetSdk = 34
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    namespace = "com.slack.exercise.commonui"
}

dependencies {
    // Coroutines
    implementation(libs.coroutines.android)

    // Compose BOM and runtime for @Immutable
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.tooling.preview)

    testImplementation(libs.junit)
}
