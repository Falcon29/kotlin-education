package org.kotlined.cc.biz


import org.kotlined.cc.biz.general.initStatus
import org.kotlined.cc.biz.general.operation
import org.kotlined.cc.biz.general.stubs
import org.kotlined.cc.biz.stubs.*
import org.kotlined.cc.biz.validation.*
import org.kotlined.common.CCContext
import org.kotlined.common.CCCorSettings
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCTicketId
import org.kotlined.cor.rootChain
import org.kotlined.cor.worker

class CCProcessor(
    private val corSettings: CCCorSettings = CCCorSettings.NONE,
) {

    suspend fun exec(ctx: CCContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<CCContext> {
        initStatus("Начальный статус обработки непонятно чего")

        operation("Создание тикета", CCCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Успешное создание", corSettings)
                stubValidationBadTitle("Ошибка валидации заголовка")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.copy() }
                worker("Очистка id") { ticketValidating.id = CCTicketId.NONE }
                worker("Очистка заголовка") { ticketValidating.title = ticketValidating.title.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateTitleIsNotEmpty("Проверка заголовка")
                validateTitleHasContent("Проверка заголовка")
                finishValidation("Завершение проверок")
            }
        }
        operation("Назначение на оператора", CCCommand.ASSIGN) {
            stubs("Обработка стабов") {
                stubAssignSuccess("Успешно назначен на оператора", corSettings)
                stubValidationBadID("Ошибка валидации ID")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.copy() }
                worker("Очистка id") { ticketValidating.id = CCTicketId.NONE }
                validateId("Проверка ID")
                finishValidation("Завершение проверок")
            }
        }
        operation("Получение/чтение тикета оператором", CCCommand.GET) {
            stubs("Обработка стабов") {
                stubGetSuccess("Успешно получен оператором", corSettings)
                stubValidationBadID("Ошибка валидации ID")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.copy() }
                worker("Очистка id") { ticketValidating.id = CCTicketId.NONE }
                validateId("Проверка ID")
                finishValidation("Завершение проверок")
            }
        }
        operation("Ответ на тикет оператором", CCCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Успешно обновлен", corSettings)
                stubValidationBadTitle("Ошибка валидации заголовка")
                stubValidationBadDescription("Ошибка валидации описания")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.copy() }
                worker("Очистка id") { ticketValidating.id = CCTicketId.NONE }
                worker("Очистка заголовка") { ticketValidating.title = ticketValidating.title.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateTitleIsNotEmpty("Проверка заголовка")
                validateTitleHasContent("Проверка заголовка")
                validateDescription("Проверка описания")
                finishValidation("Завершение проверок")
            }
        }
//        operation("Получение/чтение ответа (обновленного тикета) клиентом", CCCommand.GET) {
//
//        }
    }.build()
}