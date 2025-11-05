package org.kotlined.cc.app.ktor

import io.ktor.server.application.*
import org.kotlined.TicketRepository
import org.kotlined.common.repository.IRepoTicket

actual fun Application.getDatabaseConf(type: TicketDbType): IRepoTicket {
    val dbSettingPath = "${ConfigPaths.REPOSITORY}.${type.confName}"
    return when (val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', but it must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initCassandra(): IRepoTicket {
    val config = CassandraConfig(environment.config)
    return TicketRepository(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

object ConfigPaths {
    const val CC_ROOT = "contactcenter"
    const val REPOSITORY = "$CC_ROOT.repository"
}