package com.mvp.status.domain.service.status

import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.model.payment.listener.NotificationTopicMessageDTO
import com.mvp.status.domain.model.payment.listener.StatusListenerDTO

interface StatusService {

    fun getStatusByExternalId(requestStatusDTO: RequestStatusDTO): OrderByIdResponseDTO

}