package com.mvp.status.domain.model.payment.listener


import com.fasterxml.jackson.annotation.JsonProperty

data class StatusListenerDTO(
    @JsonProperty("externalId")
    var externalId: String,
    @JsonProperty("idClient")
    var idClient: Int,
    @JsonProperty("isFinished")
    var isFinished: Boolean,
    @JsonProperty("status")
    var status: String,
    @JsonProperty("totalPrice")
    var totalPrice: Int,
    @JsonProperty("waitingTime")
    var waitingTime: List<Int>
)