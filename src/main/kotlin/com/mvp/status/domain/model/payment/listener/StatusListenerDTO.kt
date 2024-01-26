package com.mvp.status.domain.model.payment.listener


import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class StatusListenerDTO(
//    @JsonProperty("id")
//    var id: String,
    @JsonProperty("externalId")
    var externalId: String,
    @JsonProperty("idClient")
    var idClient: Int,
    @JsonProperty("isFinished")
    var isFinished: Boolean,
    @JsonProperty("status")
    var status: String,
    @JsonProperty("totalPrice")
    var totalPrice: BigDecimal,
    @JsonProperty("waitingTime")
    var waitingTime: List<Int>
)