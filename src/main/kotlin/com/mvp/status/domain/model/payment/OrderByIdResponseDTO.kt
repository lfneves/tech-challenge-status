package com.mvp.status.domain.model.payment

import com.mvp.status.domain.model.payment.listener.Product
import com.mvp.status.domain.model.status.StatusDTO
import com.mvp.status.infrastruture.entity.StatusEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class OrderByIdResponseDTO(
    var externalId: String = "",
    var idClient: Int? = null,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var status: String = "",
    var waitingTime: LocalDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime(),
    var isFinished: Boolean = false,
) {
    fun toStatusDTO(): StatusDTO {
        return StatusDTO(
            externalId = this.externalId,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            waitingTime = this.waitingTime,
            isFinished = this.isFinished
        )
    }

    companion object {
        fun fromOrderEntityToOrderByIdResponseDTO(statusEntity: StatusEntity): OrderByIdResponseDTO {
            return OrderByIdResponseDTO(
                externalId = statusEntity.externalId,
                idClient = statusEntity.idClient,
                totalPrice = statusEntity.totalPrice,
                status = statusEntity.status,
                waitingTime = statusEntity.waitingTime,
                isFinished = statusEntity.isFinished
            )
        }
    }
}

data class OrderProductResponseDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
    var productName: String? = null,
    var categoryName: String? = null,
    var price: BigDecimal = BigDecimal.ZERO
)