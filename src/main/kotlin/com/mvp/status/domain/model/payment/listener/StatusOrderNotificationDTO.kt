package com.mvp.status.domain.model.payment.listener

import com.fasterxml.jackson.annotation.JsonProperty

data class StatusOrderNotificationDTO(
    @JsonProperty("orderDTO") val orderDTO: OrderDTO
)