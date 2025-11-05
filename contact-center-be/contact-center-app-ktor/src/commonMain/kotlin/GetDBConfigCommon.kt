package org.kotlined.cc.app.ktor

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import org.kotlined.cc.inmemory.TicketRepoInMemory
import org.kotlined.common.repository.IRepoTicket
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect fun Application.getDatabaseConf(type: TicketDbType): IRepoTicket

enum class TicketDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.initInMemory(): IRepoTicket {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return TicketRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

data class CassandraConfig(
    val host: String = "localhost",
    val port: Int = 9042,
    val user: String = "cassandra",
    val pass: String = "cassandra",
    val keyspace: String = "test_keyspace"
) {
    constructor(config: ApplicationConfig) : this(
        host = config.property("$PATH.host").getString(),
        port = config.property("$PATH.port").getString().toInt(),
        user = config.property("$PATH.user").getString(),
        pass = config.property("$PATH.pass").getString(),
        keyspace = config.property("$PATH.keyspace").getString()
    )

    companion object {
        const val PATH = "contactcenter.repository.cassandra"
    }
}