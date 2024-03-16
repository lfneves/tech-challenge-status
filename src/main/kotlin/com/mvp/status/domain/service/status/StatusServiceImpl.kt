package com.mvp.status.domain.service.status

import com.mvp.status.domain.model.exception.Exceptions
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
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
           throw Exceptions.NotFoundException(ERROR_ORDER_NOT_FOUND)
       }
    }
}