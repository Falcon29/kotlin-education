package org.kotlined.cc.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import org.kotlined.cc.api.v1.apiV1RequestSerialize
import org.kotlined.cc.api.v1.apiV1ResponseDeserialize
import org.kotlined.cc.api.v1.models.TicketCreateObject
import org.kotlined.cc.api.v1.models.TicketCreateRequest
import org.kotlined.cc.api.v1.models.TicketCreateResponse
import org.kotlined.cc.api.v1.models.TicketPriority
import java.util.Collections
import kotlin.collections.set
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = KafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = KafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        TicketCreateRequest(
                            ticket = TicketCreateObject(
                                title = "Ticket title",
                                description = "i need help",
                                priority = TicketPriority.HIGH
                            )
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TicketCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Ticket title", result.ticket?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}