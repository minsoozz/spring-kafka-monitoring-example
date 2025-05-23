package com.github.minsoozz.monitoring.consumer

import com.github.minsoozz.monitoring.model.Member
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaMessageListener {

    @KafkaListener(topics = ["test-topic"])
    fun listen(message: Member) {
        println("id: ${message.id}, name: ${message.name}, age: ${message.age}")
    }
}