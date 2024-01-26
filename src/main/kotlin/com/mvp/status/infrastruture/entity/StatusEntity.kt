package com.mvp.status.infrastruture.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.listener.NotificationTopicMessageDTO
import com.mvp.status.domain.model.payment.listener.StatusListenerDTO
import com.mvp.status.domain.model.payment.listener.StatusOrderNotificationDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Document(collection = "statusEntity")
data class StatusEntity(
    @Id
    @Field("external_id")
    val externalId: String = "",
    @Field("id_client")
    var idClient: Int? = null,
    @Field("username")
    var username: String? = null,
    @Field("total_price")
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    @Field("status")
    var status: String = "",
    @Field("updateStatus")
    var updateStatus: String = "",
    @Field("waiting_time")
    var waitingTime: LocalDateTime = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
    @Field("is_finished")
    var isFinished: Boolean = false,
    val className: String = "com.mvp.status.infrastructure.entity.StatusEntity"
) {
    companion object {

        private val mapper = jacksonObjectMapper()

        fun fromOrderNotification(notificationMessage: NotificationTopicMessageDTO): StatusEntity {
            val notification: StatusOrderNotificationDTO = mapper.readValue(notificationMessage.message)
            return StatusEntity(
                externalId = notification.orderDTO.externalId,
                idClient = notification.orderDTO.idClient,
                totalPrice = notification.orderDTO.totalPrice,
                status = notification.orderDTO.status,
                waitingTime = notification.orderDTO.waitingTime,
                isFinished = notification.orderDTO.isFinished
            )
        }

        fun fromStatusListenerDTO(statusListenerDTO: StatusListenerDTO): StatusEntity {
            return StatusEntity(
                externalId = statusListenerDTO.externalId,
                idClient = statusListenerDTO.idClient,
                totalPrice = statusListenerDTO.totalPrice,
                status = statusListenerDTO.status,
                isFinished = statusListenerDTO.isFinished
            )
        }

        fun fromOrderByIdResponseDTO(orderByIdResponseDTO: OrderByIdResponseDTO): StatusEntity {
            return StatusEntity(
                externalId = orderByIdResponseDTO.externalId,
                idClient = orderByIdResponseDTO.idClient,
                totalPrice = orderByIdResponseDTO.totalPrice,
                status = orderByIdResponseDTO.status,
                waitingTime = orderByIdResponseDTO.waitingTime,
                isFinished = orderByIdResponseDTO.isFinished
            )
        }
    }
}