package com.github.minsoozz.monitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaMonitoringExampleApplication

fun main(args: Array<String>) {
    runApplication<KafkaMonitoringExampleApplication>(*args)
}
