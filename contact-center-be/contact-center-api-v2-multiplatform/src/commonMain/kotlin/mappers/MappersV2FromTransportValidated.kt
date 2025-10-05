package org.kotlined.cc.api.v2.mappers

import org.kotlined.cc.api.v2.models.TicketCreateRequest
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCStubs

// Демонстрация форматной валидации в мапере
private sealed interface Result<T, E>
private data class Ok<T, E>(val value: T) : Result<T, E>
private data class Err<T, E>(val errors: List<E>) : Result<T, E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T, E> Result<T, E>.getOrExec(default: T, block: (Err<T, E>) -> Unit = {}): T = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T, E> Result<T, E>.getOrNull(block: (Err<T, E>) -> Unit = {}): T? = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<CCStubs, CCError> = when (this) {
    "success" -> Ok(CCStubs.SUCCESS)
    "badId" -> Ok(CCStubs.BAD_ID)
    "badTitle" -> Ok(CCStubs.BAD_TITLE)
    "badDescription" -> Ok(CCStubs.BAD_DESCRIPTION)
//    "dbError" -> Ok(CCStubs.DB_ERROR)
    null -> Ok(CCStubs.NONE)
    else -> Err(
        CCError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun CCContext.fromTransportValidated(request: TicketCreateRequest) {
    command = CCCommand.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(CCStubs.NONE) { err: Err<CCStubs, CCError> ->
            errors.addAll(err.errors)
            state = CCState.FAILING
        }
}