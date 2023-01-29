plugins {
    id("com.android.application")
    kotlin("android")
}

val composeVersion = "1.4.0"

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.github.lupuuss.diskount.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.daftmobile.redukt:redukt-compose:1.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(platform("androidx.compose:compose-bom:2023.01.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    androidTestImplementation("com.daftmobile.redukt:redukt-test:1.0")
}