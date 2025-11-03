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
    namespace = "com.slack.exercise.common"
}

dependencies {
    implementation(libs.collection)
    implementation(libs.coroutines.android)
    testImplementation(libs.junit)
}
