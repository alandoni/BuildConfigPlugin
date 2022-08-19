plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    kotlin("jvm") version "1.6.21"
}

buildscript {
    val kotlinVersion = "1.6.21"
    val gradleVersion = "7.2.1"

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$gradleVersion")
    }
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("GenerateBuildConfigPlugin") {
            id = "com.adqmobile.gradleplugins.buildconfig"
            implementationClass = "com.adqmobile.gradleplugins.buildconfig.GenerateBuildConfigPlugin"
            version = "1.0.0"
        }
    }
}

dependencies {
    val kotlinVersion = "1.6.21"
    val gradleVersion = "7.2.1"

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    // Android gradle plugin will allow us to access Android specific features
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}
