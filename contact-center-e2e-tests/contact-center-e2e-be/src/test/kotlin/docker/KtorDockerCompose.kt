package org.kotlined.cc.test.e2e.be.docker

import org.kotlined.cc.test.e2e.be.base.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)
