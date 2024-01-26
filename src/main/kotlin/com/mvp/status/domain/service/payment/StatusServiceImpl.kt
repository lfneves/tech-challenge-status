package com.mvp.status.domain.service.payment

import com.mvp.status.domain.model.exception.Exceptions
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.model.payment.listener.NotificationTopicMessageDTO
import com.mvp.status.domain.model.payment.listener.StatusListenerDTO
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import com.mvp.status.utils.ErrorMsgConstants.Companion.ERROR_ORDER_NOT_FOUND
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StatusServiceImpl(
    private val statusRepository: StatusRepository
): StatusService {
    private val logger: Logger = LoggerFactory.getLogger(StatusServiceImpl::class.java)

    override fun getStatusByExternalId(requestStatusDTO: RequestStatusDTO): OrderByIdResponseDTO {
        logger.info("StatusServiceImpl - getStatusByExternalId")
        val response = statusRepository.findByExternalId(requestStatusDTO.externalId)
       return if (response.isPresent) {
            OrderByIdResponseDTO.fromOrderEntityToOrderByIdResponseDTO(response.get())
        } else {
           throw Exceptions.RequestedElementNotFoundException(ERROR_ORDER_NOT_FOUND)
       }
    }

    override fun saveListenerTopicMessage(notificationTopicMessageDTO: NotificationTopicMessageDTO) {
        logger.info("StatusServiceImpl - saveListernerTopicMessage")
        val statusEntity = StatusEntity.fromOrderNotification(notificationTopicMessageDTO)
        statusRepository.save(statusEntity)
    }

    override fun saveListenerQueueMessage(statusListenerDTO: StatusListenerDTO) {
        logger.info("StatusServiceImpl - saveListenerQueueMessage")
        val statusEntity = StatusEntity.fromStatusListenerDTO(statusListenerDTO)
        statusRepository.save(statusEntity)
    }
}