plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

version = "1.0"

val ktorVersion = "2.1.3"

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
                api("io.insert-koin:koin-core:3.2.0")
                api("dev.redukt:redukt-core:1.0")
                api("dev.redukt:redukt-thunk:1.0")
                implementation("dev.redukt:redukt-data:1.0")
                implementation("dev.redukt:redukt-koin:1.0")
                implementation("io.github.aakira:napier:2.6.1")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

    }
}
