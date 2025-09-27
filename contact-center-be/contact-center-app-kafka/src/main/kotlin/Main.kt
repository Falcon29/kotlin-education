package org.kotlined.cc.kafka

fun main() {
    val config = KafkaConfig()
    val consumer = KafkaConsumer(config, listOf(ConsumerStrategyV1(), ConsumerStrategyV2()))
    consumer.start()
}
