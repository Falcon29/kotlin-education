package org.kotlined

import org.kotlined.api.v1.apiV1RequestDeserialize
import org.kotlined.api.v1.apiV1ResponseSerialize
import org.kotlined.api.v1.models.IRequest
import org.kotlined.api.v1.models.IResponse
import org.kotlined.common.CCContext

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: KafkaConfig): Topics {
        return Topics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: CCContext): String {
        val response: IResponse = source.toTransportTicket()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CCContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}