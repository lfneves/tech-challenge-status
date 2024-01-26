package com.mvp.status.application.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mvp.status.domain.model.payment.listener.NotificationTopicMessageDTO
import com.mvp.status.domain.model.payment.listener.StatusListenerDTO
import com.mvp.status.domain.service.listener.ListenerStatusService
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
class ListenerStatusServiceTest {

    @Autowired private lateinit var statusRepository: StatusRepository
    @Autowired private lateinit var listenerStatusService: ListenerStatusService

    private lateinit var messageTopic: String
    private lateinit var messageQueue: String

    @BeforeEach
    fun setup() {
        messageTopic = """
            {
              "Type" : "Notification",
              "MessageId" : "7eea7041-8e50-570e-92b5-2e6c8ddc5",
              "TopicArn" : "arn:aws:sns:us-east-1:1111111:ORDER_TOPIC",
              "Message" : "{\n  \"orderDTO\" : {\n    \"id\" : 1,\n    \"externalId\" : \"69b47112-6590-4a08-935a-af93b30ff8c8\",\n    \"idClient\" : 1,\n    \"totalPrice\" : 29.99,\n    \"status\" : \"Pendente\",\n    \"waitingTime\" : [ 2024, 1, 19, 3, 46, 33, 53690000 ],\n    \"productList\" : [ {\n      \"id\" : 46,\n      \"idProduct\" : 2,\n      \"idOrder\" : 1\n    } ],\n    \"finished\" : false\n  }\n}",
              "Timestamp" : "2024-01-26T06:55:20.336Z",
              "SignatureVersion" : "1",
              "Signature" : "oNZl7rx0IAMuw4/7jiWixz955SKFd7cdYUNHANAGurxAu9H0sb6gGhVO8yc3BQe1Gnx6Pl5f6qbmBCj+zPPgyoa3gxdpEA9/kIy/imM+184mDVjQZxLOEBJxQKo5JbMD6seKByhpNfzEt9XGiIaj5V4vsr6G7SOMxpNAOAK5qRN4RHlXcERsbXyaTViBBTnAlNj0AW/5tB5jPgLczqAmUP4kt81rQ4RUYidGyA+yK79oYMQB65WDLx1cOMY5ycZpditi0FkDouZgBdvfwNaRIpzl9blg/gTHDqAGmUQhXYXeoHJO9rm68SpLdoJLpVCxoIdBEcZJ82LWWUlqWcg==",
              "SigningCertURL" : "https://sns.us-east-1.amazonaws.com/SimpleNotificationService-60eadc530605d63b8e62a523676ef735.pem",
              "UnsubscribeURL" : "https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:11111111:ORDER_TOPIC:8f3cb18a-01ec-4b0a-8b20-daf8b145e8c7"
            }
        """.trimIndent()

        messageQueue= """
            {
                "externalId": "69b47112-6590-4a08-935a-af93b30ff8c8",
                "idClient": 1,
                "totalPrice": 29.99,
                "status": "Aguardando Pagamento",
                "waitingTime": [
                    2024,
                    1,
                    26,
                    4,
                    34,
                    14,
                    911944952
                ],
                "isFinished": false
            }       
        """.trimIndent()
    }

    @Test
    fun `Receive Topic Message should process message and save data correctly`() {
        val notificationMessage: NotificationTopicMessageDTO = jacksonObjectMapper().readValue(messageTopic)
        val savedOrder = statusRepository.save(StatusEntity.fromOrderNotification(notificationMessage))

        listenerStatusService.receiveSubscriptionMessage(messageTopic)

        Assertions.assertNotNull(statusRepository.findByExternalId(savedOrder.externalId))
    }

    @Test
    fun `receiveMessage should process message and save data correctly`() {
        val notificationMessage: StatusListenerDTO = jacksonObjectMapper().readValue(messageQueue)
        val savedOrder = statusRepository.save(StatusEntity.fromStatusListenerDTO(notificationMessage))

        listenerStatusService.receiveQueueMessage(messageQueue)

        Assertions.assertNotNull(statusRepository.findByExternalId(savedOrder.externalId))
    }
}