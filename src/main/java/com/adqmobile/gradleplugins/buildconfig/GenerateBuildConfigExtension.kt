package com.adqmobile.gradleplugins.buildconfig

import java.net.NetworkInterface
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.net.Inet4Address
import javax.inject.Inject

abstract class GenerateBuildConfigExtension @Inject constructor(objects: ObjectFactory) {
    val properties = objects.domainObjectContainer(PropertiesHandler::class.java)

    abstract val packageName: Property<String>
    abstract val objectNameStr: Property<String>

    fun properties(
        action: Action<NamedDomainObjectContainer<PropertiesHandler>>
    ) {
        action.execute(properties)
    }

    var `package`: String
        set(value) {
            packageName.set(value)
            packageName.disallowChanges()
        }
        get() = packageName.get()

    var objectName: String
        set(value) {
            objectNameStr.set(value)
            objectNameStr.disallowChanges()
        }
        get() = objectNameStr.get()

    companion object {
        internal fun Project.buildConfig(): GenerateBuildConfigExtension {
            return extensions.create("buildConfig", GenerateBuildConfigExtension::class.java)
        }
    }
}