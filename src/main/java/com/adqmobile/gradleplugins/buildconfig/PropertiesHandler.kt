package com.adqmobile.gradleplugins.buildconfig

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

abstract class PropertiesHandler constructor(
    private val name: String,
    objects: ObjectFactory
): Named {
    override fun getName(): String = name

    internal val theValue: Property<Any> = objects.property(Any::class.java)

    var value: Any
        get() = theValue.get()
        set(newValue) {
            theValue.set(newValue)
            theValue.disallowChanges()
        }
}

fun NamedDomainObjectContainer<PropertiesHandler>.register(name: String, value: Any) {
    create(name) {
        this.value = value
    }
}