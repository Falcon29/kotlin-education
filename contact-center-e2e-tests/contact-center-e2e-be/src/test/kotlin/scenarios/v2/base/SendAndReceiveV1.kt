package org.kotlined.cc.test.e2e.be.scenarios.v2.base

import co.touchlab.kermit.Logger
import org.kotlined.cc.api.v2.apiV2RequestSerialize
import org.kotlined.cc.api.v2.apiV2ResponseDeserialize
import org.kotlined.cc.api.v2.models.*
import org.kotlined.cc.test.e2e.be.base.client.Client

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV2RequestSerialize(request)
    log.i { "Send to v2/$path\n$requestBody" }

    val responseBody = sendAndReceive("v2", path, requestBody)
    log.i { "Received\n$responseBody" }

    return apiV2ResponseDeserialize(responseBody)
}