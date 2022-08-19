package com.adqmobile.gradleplugins.buildconfig

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class GenerateBuildConfigTask @Inject constructor(
    objects: ObjectFactory
) : DefaultTask() {
    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val objectName: Property<String>

    @get:Input
    val properties = objects.mapProperty(String::class.java, Property::class.java)

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun taskAction() {
        val folder = packageName.get().replace(".", File.separator)
        val srcFile = File(
            outputDirectory.get().asFile,
            "$folder${File.separator}${objectName.get()}.kt"
        )
        srcFile.parentFile.mkdirs()

        val properties = properties.get().entries.joinToString(
            System.lineSeparator()
        ) { prop ->
            if (prop.value.get() is String) {
                "\tconst val ${prop.key} = \"${prop.value.get()}\""
            } else {
                "\tconst val ${prop.key} = ${prop.value.get()}"
            }
        }
        val content = """package ${packageName.get()}

object ${objectName.get()} {
$properties
}
"""
        srcFile.writeText(content)

        println(content)
    }
}