package com.adqmobile.gradleplugins.buildconfig

import com.adqmobile.gradleplugins.buildconfig.GenerateBuildConfigExtension.Companion.buildConfig
import org.gradle.api.*
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.*
import java.io.File
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.net.Inet4Address
import java.net.NetworkInterface


class GenerateBuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        val extension = buildConfig()

        println("Initiating GenerateBuildConfig plugin")

        val outputDir = File("${project.buildDir}/generated/sources")

        tasks.register(
            "generateBuildConfig",
            GenerateBuildConfigTask::class.java,
        ) {
            outputs.dir(outputDir)
            outputDirectory.set(outputDir)
            packageName.set(extension.packageName)
            objectName.set(extension.objectNameStr)
            val map = extension.properties.asMap.entries.associate {
                it.key to it.value.theValue
            }
            properties.set(map)
        }
        try {
            tasks.getByName("compileKotlin") {
                dependsOn("generateBuildConfig")
            }
        } catch (e: UnknownTaskException) {
            tasks.getByName("preBuild") {
                dependsOn("generateBuildConfig")
            }
        }

        val multiplatform = plugins.findPlugin("org.jetbrains.kotlin.multiplatform") != null
        if (multiplatform) {
            addToKotlinMultiplatformSourceSet(outputDir)
        } else {
            addToKotlinSourceSet(outputDir)
        }
    }

    private fun Project.addToKotlinMultiplatformSourceSet(generatedSourceDir: File) {
        val kotlinExtension = kotlinMultiplatform()
        val kotlinSourceSets = kotlinExtension.sourceSets

        val kotlinSource = kotlinSourceSets.getByName(
            KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
        )

        val kotlinSourceSet = kotlinSource.kotlin
        kotlinSourceSet.srcDir(generatedSourceDir)

        val sourceSets = extensions.getByType(SourceSetContainer::class.java)
        sourceSets.all {
            java.srcDir(generatedSourceDir)
        }
    }

    private fun Project.addToKotlinSourceSet(generatedSourceDir: File) {
        val kotlinExtension = kotlinJvm()
        val kotlinSourceSets = kotlinExtension.sourceSets

        val kotlinSource = kotlinSourceSets.getByName("main")

        val kotlinSourceSet = kotlinSource.kotlin
        kotlinSourceSet.srcDir(generatedSourceDir)

        val sourceSets = extensions.getByType(SourceSetContainer::class.java)
        sourceSets.all {
            java.srcDir(generatedSourceDir)
        }

        println("SourceSets configured")
    }
}
