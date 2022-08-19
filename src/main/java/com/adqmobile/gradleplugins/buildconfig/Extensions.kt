package com.adqmobile.gradleplugins.buildconfig

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.net.Inet4Address
import java.net.NetworkInterface

fun Project.kotlinJvm(): KotlinJvmProjectExtension {
    val kotlinJvm = project.extensions.findByType(KotlinJvmProjectExtension::class.java)
    if (kotlinJvm != null) {
        return kotlinJvm
    } else {
        throw GradleException("Project $name is not an Kotlin-JVM project")
    }
}

fun Project.kotlinMultiplatform(): KotlinMultiplatformExtension {
    val kotlinMultiplatform = project.extensions.findByType(
        KotlinMultiplatformExtension::class.java
    )
    if (kotlinMultiplatform != null) {
        return kotlinMultiplatform
    } else {
        throw GradleException("Project $name is not an Kotlin-JVM project")
    }
}

fun Project.getLocalIpv4(): String {
    return NetworkInterface.getNetworkInterfaces().toList()
        .filter { it.isUp && !it.isLoopback && !it.isVirtual }
        .flatMap { it.inetAddresses.toList() }
        .mapNotNull {
            if (!it.isLoopbackAddress && it is Inet4Address) {
                it.hostAddress
            } else {
                null
            }
        }
        .first()
}