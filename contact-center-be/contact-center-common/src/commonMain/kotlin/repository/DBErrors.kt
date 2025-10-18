package org.kotlined.common.repository

import org.kotlined.common.helpers.errorSystem
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: CCTicketId) = DBTicketResponseError(
    CCError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DBTicketResponseError(
    CCError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldTicket: CCTicket,
    expectedLock: CCTicketLock,
    exception: Exception = RepoConcurrencyException(
        id = oldTicket.id,
        expectedLock = expectedLock,
        actualLock = oldTicket.lock,
    ),
) = DBTicketResponseErrWithData(
    ticket = oldTicket,
    err = CCError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldTicket.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: CCTicketId) = DBTicketResponseError(
    CCError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ticket ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DBTicketResponseError(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)

class RepoConcurrencyException(id: CCTicketId, expectedLock: CCTicketLock, actualLock: CCTicketLock?): RepoTicketException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)

open class RepoTicketException(
    @Suppress("unused")
    val ticketId: CCTicketId,
    msg: String,
): RepoException(msg)

open class RepoException(msg: String): Exception(msg)