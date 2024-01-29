package com.mvp.status.application.v1


import com.mvp.status.domain.model.payment.OrderByIdResponseDTO
import com.mvp.status.domain.model.payment.RequestStatusDTO
import com.mvp.status.domain.service.status.StatusService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/status")
class StatusController(
    private val statusService: StatusService,
) {

    @GetMapping("/check-status")
    @Operation(
        summary = "Verifica o status",
        description = "Efetua o pagamento atualizando os status",
        tags = ["Status"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun checkoutOrder(@RequestBody requestStatusDTO: RequestStatusDTO): ResponseEntity<OrderByIdResponseDTO> {
        return ResponseEntity.ok(statusService.getStatusByExternalId(requestStatusDTO))
    }
}