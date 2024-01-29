package com.mvp.status.application.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.status.application.v1.StatusController
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.service.message.SnsAndSqsService
import com.mvp.status.domain.service.status.StatusService
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@ActiveProfiles("test")
class StatusControllerTest {

    private lateinit var statusController: StatusController
    private val statusService: StatusService = mockk<StatusService>(relaxed = true)
    private val snsAndSqsService: SnsAndSqsService = mockk<SnsAndSqsService>(relaxed = true)
    private val statusRepository: StatusRepository = mockk<StatusRepository>(relaxed = true)

    private lateinit var statusEntity: StatusEntity
    private lateinit var orderByIdResponseDTO: OrderByIdResponseDTO

    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        statusController = StatusController(statusService)
        statusEntity = StatusEntity(
            externalId = "69b47112-6590-4a08-935a-af93b30ff8c8",
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            isFinished = false
        )

        every { statusRepository.save(statusEntity) } returns statusEntity
    }

    @Test
    fun testCheckoutOrder2() {
        val requestStatusDTO = RequestStatusDTO(externalId = "69b47112-6590-4a08-935a-af93b30ff8c8")
        val expectedResponse =  OrderByIdResponseDTO(
                                externalId = "4879d212-bdf1-413c-9fd1-5b65b50257bc",
                                idClient = 1,
                                totalPrice = BigDecimal.TEN,
                                status = "PENDING",
                                isFinished = false,
                            )

        every { statusService.getStatusByExternalId(requestStatusDTO) } returns expectedResponse

        val response = statusController.checkoutOrder(requestStatusDTO)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }

}