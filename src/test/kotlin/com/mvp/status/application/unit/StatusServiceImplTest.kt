package com.mvp.status.application.unit

import com.mvp.status.domain.model.exception.Exceptions
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.service.status.StatusService
import com.mvp.status.domain.service.status.StatusServiceImpl
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
class StatusServiceImplTest {

    private var statusService: StatusService = mockk(relaxed = true)
    private val statusRepository: StatusRepository = mockk(relaxed = true)

    private lateinit var statusEntity: StatusEntity

    @BeforeEach
    fun setUp() {
        statusService = StatusServiceImpl(statusRepository)

        statusEntity = StatusEntity(
            externalId = "69b47112-6590-4a08-935a-af93b30ff8c8",
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            isFinished = false
        )
    }

    @Test
    fun `should return OrderByIdResponseDTO when status is found`() {
        every { statusRepository.save(any()) } returns statusEntity
        statusRepository.save(statusEntity)
        val requestStatusDTO = RequestStatusDTO("69b47112-6590-4a08-935a-af93b30ff8c8")
        val orderByIdResponseDTO = OrderByIdResponseDTO.fromOrderEntityToOrderByIdResponseDTO(statusEntity)

        every { statusRepository.findByExternalId(any()) } returns Optional.of(statusEntity)

        val result = statusService.getStatusByExternalId(requestStatusDTO)
        val expected = statusRepository.findByExternalId("69b47112-6590-4a08-935a-af93b30ff8c8")
        assertNotNull(expected)
        assertEquals(orderByIdResponseDTO.externalId, result.externalId)

    }
}
