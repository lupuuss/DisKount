plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

version = "1.0"

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}

kotlin {
    android()
    js(IR) { browser() }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.insert-koin:koin-core:3.3.2")
                api("com.daftmobile.redukt:redukt-core:1.0")
                api("com.daftmobile.redukt:redukt-thunk:1.0")
                implementation("com.daftmobile.redukt:redukt-data:1.0")
                implementation("com.daftmobile.redukt:redukt-koin:1.0")
                implementation("io.github.aakira:napier:2.6.1")

                api(platform("io.ktor:ktor-bom:2.1.3"))
                implementation("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-logging:")
                implementation("io.ktor:ktor-client-content-negotiation:")
                implementation("io.ktor:ktor-serialization-kotlinx-json:")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.daftmobile.redukt:redukt-test-thunk:1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val androidMain by getting {
            dependencies {
                api("io.insert-koin:koin-android:3.3.2")
                implementation("io.ktor:ktor-client-okhttp")
            }
        }

    }
}
