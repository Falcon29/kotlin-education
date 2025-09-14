package org.kotlined.cc.kafka

import org.kotlined.cc.api.v2.apiV2RequestDeserialize
import org.kotlined.cc.api.v2.apiV2ResponseSerialize
import org.kotlined.cc.api.v2.mappers.fromTransport
import org.kotlined.cc.api.v2.mappers.toTransportTicket
import org.kotlined.cc.api.v2.models.IRequest
import org.kotlined.cc.api.v2.models.IResponse
import org.kotlined.common.CCContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: KafkaConfig): Topics {
        return Topics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: CCContext): String {
        val response: IResponse = source.toTransportTicket()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CCContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}