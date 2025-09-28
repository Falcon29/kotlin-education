package org.kotlined.common.helpers

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

inline fun CCContext.addError(vararg error: CCError) = errors.addAll(error)

inline fun CCContext.fail(error: CCError) {
    addError(error)
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