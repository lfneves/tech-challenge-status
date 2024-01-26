package com.mvp.status.application.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.service.message.SnsAndSqsService
import com.mvp.status.domain.service.payment.StatusService
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.math.BigDecimal


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class StatusControllerTest {

    private val statusService: StatusService = mockk<StatusService>()
    private val snsAndSqsService: SnsAndSqsService = mockk<SnsAndSqsService>()

    @Autowired
    private lateinit var statusRepository: StatusRepository

    private lateinit var statusEntity: StatusEntity
    private lateinit var orderByIdResponseDTO: OrderByIdResponseDTO

    private val mapper = jacksonObjectMapper()

    @Autowired
    private val mockMvc: MockMvc? = null

    @LocalServerPort
    private var port: Int = 8097

    @BeforeEach
    fun setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc)
        MockKAnnotations.init(this)
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        statusEntity = StatusEntity(
            externalId = "69b47112-6590-4a08-935a-af93b30ff8c8",
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            isFinished = false
        )

        statusRepository.save(statusEntity)
    }

    @Test
    fun testCheckoutOrder() {
        val requestStatusDTO = RequestStatusDTO(externalId = "69b47112-6590-4a08-935a-af93b30ff8c8")

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(mapper.writeValueAsString(requestStatusDTO))
            .`when`()
            .get("/api/v1/status/check-status")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("externalId", equalTo("69b47112-6590-4a08-935a-af93b30ff8c8"))
    }

}