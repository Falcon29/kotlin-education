package org.kotlined.cc.test.e2e.be.docker

import org.kotlined.cc.test.e2e.be.base.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "docker-compose-kafka.yml"
)
