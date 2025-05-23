package com.github.minsoozz.monitoring.controller

import com.github.minsoozz.monitoring.model.Member
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Executors
import kotlin.random.Random

@RestController
class TestController(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    @PostMapping("/test")
    fun send(@RequestBody member: Member) {
        val totalMessages = 100_000
        val threadCount = 10
        val messagesPerThread = totalMessages / threadCount

        val executor = Executors.newFixedThreadPool(threadCount)

        repeat(threadCount) {
            executor.submit {
                val threadName = Thread.currentThread().name
                repeat(messagesPerThread) { i ->
                    val randomized = member.copy(
                        id = "$threadName-$i",
                        name = "name-${Random.nextInt(1000)}",
                        age = Random.nextInt(10, 60)
                    )
                    kafkaTemplate.send("test-topic", randomized)
                }
            }
        }
        executor.shutdown()
    }
}