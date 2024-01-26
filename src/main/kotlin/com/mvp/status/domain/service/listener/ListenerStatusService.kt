package com.mvp.status.domain.service.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mvp.status.domain.model.payment.listener.NotificationTopicMessageDTO
import com.mvp.status.domain.model.payment.listener.StatusListenerDTO
import com.mvp.status.domain.service.payment.StatusServiceImpl
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class ListenerStatusService {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var statusServiceImpl: StatusServiceImpl

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Async
    @SqsListener("STATUS_QUEUE")
    fun receiveQueueMessage(message: String) {
        logger.info("Received message: {}", message)

        val notificationMessage: StatusListenerDTO = objectMapper.readValue(message)
        logger.info("save receiveQueueMessage: {}", notificationMessage.externalId)
        statusServiceImpl.saveListenerQueueMessage(notificationMessage)
    }

    @Async
    @SqsListener("STATUS_SUBSCRIPTION_ORDER")
    fun receiveSubscriptionMessage(message: String) {
        logger.info("Received message: {}", message)

        val notificationMessage: NotificationTopicMessageDTO = objectMapper.readValue(message)
        logger.info("save receiveSubscriptionMessage: {}", notificationMessage.messageId)
        statusServiceImpl.saveListenerTopicMessage(notificationMessage)
    }
}