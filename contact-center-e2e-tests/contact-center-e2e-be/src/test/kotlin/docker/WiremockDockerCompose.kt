package org.kotlined.cc.test.e2e.be.docker

import org.kotlined.cc.test.e2e.be.base.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
