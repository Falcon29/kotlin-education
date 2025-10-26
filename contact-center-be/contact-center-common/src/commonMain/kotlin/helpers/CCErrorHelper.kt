package org.kotlined.common.helpers

import org.kotlined.cc.logging.common.LogLevel
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCState

fun Throwable.asCCError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CCError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun CCContext.addError(error: CCError) = errors.add(error)
inline fun CCContext.addErrors(error: Collection<CCError>) = errors.addAll(error)

inline fun CCContext.fail(error: CCError) {
    addError(error)
    state = CCState.FAILING
}

inline fun CCContext.fail(errors: Collection<CCError>) {
    addErrors(errors)
    state = CCState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
) = CCError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = CCError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)