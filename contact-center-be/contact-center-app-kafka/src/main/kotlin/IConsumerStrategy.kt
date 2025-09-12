package org.kotlined

import org.kotlined.common.CCContext

interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: KafkaConfig): Topics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: CCContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: CCContext)
}
