plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
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
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared module for DisKount app!"
        homepage = "None!"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "Shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.insert-koin:koin-core:3.3.3")
                api(platform("com.daftmobile.redukt:redukt-bom:1.0"))
                api("com.daftmobile.redukt:redukt-core")
                api("com.daftmobile.redukt:redukt-thunk")
                implementation("com.daftmobile.redukt:redukt-data-ktor")
                implementation("com.daftmobile.redukt:redukt-koin")
                implementation("io.github.aakira:napier:2.6.1")

                api(platform("io.ktor:ktor-bom:2.2.3"))
                implementation("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-logging:")
                implementation("io.ktor:ktor-client-content-negotiation:")
                implementation("io.ktor:ktor-serialization-kotlinx-json:")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.daftmobile.redukt:redukt-test-thunk")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val androidMain by getting {
            dependencies {
                api("io.insert-koin:koin-android:3.3.2")
                implementation("io.ktor:ktor-client-okhttp")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.daftmobile.redukt:redukt-swift")
                implementation("io.ktor:ktor-client-darwin")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}
