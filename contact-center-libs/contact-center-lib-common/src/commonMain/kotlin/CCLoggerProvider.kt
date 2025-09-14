package org.kotlined

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CCLoggerProvider(
    private val provider: (String) -> ICCLogWrapper = { ICCLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): ICCLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): ICCLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): ICCLogWrapper = provider(function.name)
}