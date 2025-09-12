package org.kotlined

data class Topics(
    /**
     * Топик входящих в приложение сообщений
     */
    val input: String,
    /**
     * Топик для исходящих из приложения сообщений
     */
    val output: String,
)
