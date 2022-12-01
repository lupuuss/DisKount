buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.7.20"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.android.tools.build:gradle:7.2.2")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}
