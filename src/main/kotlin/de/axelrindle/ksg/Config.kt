package de.axelrindle.ksg

import java.util.*
import kotlin.reflect.KProperty

object Config {

    private const val ENV_VARIABLE_PREFIX = "MK_"

    private class Environment(
        private val fallback: String? = null
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            val raw = System.getenv("$ENV_VARIABLE_PREFIX${property.name}")
            return Optional.ofNullable(raw).orElse(fallback)
        }
    }

    val APPLICATION_NAME: String by Environment("make-keystore")

}
