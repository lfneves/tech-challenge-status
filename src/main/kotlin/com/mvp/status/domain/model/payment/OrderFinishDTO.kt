package com.mvp.status.domain.model.payment

data class OrderFinishDTO(
    var isFinished: Boolean = false,
    val externalId: String = ""
)