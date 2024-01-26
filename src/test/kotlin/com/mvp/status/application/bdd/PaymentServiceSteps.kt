package com.mvp.status.application.bdd

import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.service.payment.StatusService
import com.mvp.status.infrastruture.entity.StatusEntity
import com.mvp.status.infrastruture.repository.StatusRepository
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceSteps {

    @Autowired
    private lateinit var statusService: StatusService
    @Autowired
    private lateinit var statusRepository: StatusRepository

    private lateinit var orderResponse: OrderByIdResponseDTO
    private var orderId: Long = 0
    private lateinit var  statusEntity: StatusEntity

    @Given("Entity")
    fun setup() {
        statusEntity = StatusEntity(
            externalId = "4879d212-bdf1-413c-9fd1-5b65b50257bc",
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            isFinished = false
        )
    }

    @Given("I have an order with ID {long}")
    fun i_have_an_order_with_id(id: Long) {
        orderId = id
    }

    @Then("Save order Repository")
    fun save_order_repository() {
        statusRepository.save(statusEntity)
    }

    @When("I request the order by this ID")
    fun i_request_the_order_by_this_id() {
        orderResponse = statusService.getStatusByExternalId(RequestStatusDTO(externalId = statusEntity.externalId))
    }

    @Then("I should receive the order details")
    fun i_should_receive_the_order_details() {
        Assertions.assertNotNull(orderResponse)
        Assertions.assertEquals("4879d212-bdf1-413c-9fd1-5b65b50257bc", orderResponse.externalId)
    }
}
