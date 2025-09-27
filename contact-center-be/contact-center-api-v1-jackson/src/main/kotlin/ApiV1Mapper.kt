package org.kotlined.cc.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import org.kotlined.cc.api.v1.models.IRequest
import org.kotlined.cc.api.v1.models.IResponse

val apiV1Mapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
} ?: throw RuntimeJsonMappingException("Something wrong with JSON mapper >.<")

@Suppress("unused")
fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("unused", "UNCHECKED_CAST")
fun <T: IRequest> apiV1RequestDeserialize(jsonAsString: String): T =
    apiV1Mapper.readValue(jsonAsString, IRequest::class.java) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("unused", "UNCHECKED_CAST")
fun <T: IResponse> apiV1ResponseDeserialize(jsonAsString: String): T =
    apiV1Mapper.readValue(jsonAsString, IResponse::class.java) as T