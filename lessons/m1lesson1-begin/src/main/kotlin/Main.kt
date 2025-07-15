package org.kotlined

fun main() {
    val name = "Kotlin"
    println(sayHello(name))
}

fun sayHello(name: String) = "Hello, $name!"