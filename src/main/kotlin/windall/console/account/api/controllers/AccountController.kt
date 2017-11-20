package windall.console.account.api.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import windall.console.account.api.dtos.PaymentDto
import windall.console.account.api.dtos.TenantDto
import windall.console.account.api.model.Tenant
import windall.console.account.api.repository.ReceiptRepository
import windall.console.account.api.repository.TenantRepository
import java.time.LocalDateTime

@RestController
@Api(value="Rental Accounts", description="Operations for tracking the amount of rent paid (in Australian dollars only) by a tenant’s lease.")
@RequestMapping("/tenants")
class AccountController(
		val tenantRepo: TenantRepository,
		val receiptRepo: ReceiptRepository
) {

	@GetMapping("/{id}")
	@ApiOperation(value = "Get a single tenant by id", produces = "application/json")
	fun findTenantByTenantId(@PathVariable id: Long) = tenantRepo.findOne(id)

	@GetMapping("/{id}/receipts")
	@ApiOperation(value = "List all Rent Receipts for a single Tenant", produces = "application/json")
	fun findReceiptsByTenantId(@PathVariable id: Long) = receiptRepo.findByTenant(tenantRepo.findOne(id))
	
	@GetMapping("/filter")
	@ApiOperation(value = "List all Tenants which have made a payment in the last “N” hours", produces = "application/json")
	fun findTenantsByReceiptAge(@RequestParam("paidinlast") maxHours: Long) = receiptRepo
			.findByRecentPayments(LocalDateTime.now().minusHours(maxHours))
			.map { it.tenant }
			.distinct()

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED) 
	@ApiOperation(value = "Create a new tenant", produces = "application/json")
	fun createTenant(@RequestBody input: TenantDto) = tenantRepo.save(Tenant(input))
	
	@PostMapping("/{id}/pay")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a single Rent Receipt", produces = "application/json")
	fun createReceipt(@PathVariable id: Long, @RequestBody input: PaymentDto): Tenant {
		val tenant = tenantRepo.findOne(id)
		tenant.makePayment(input)
		return tenantRepo.save(tenant)
	} 
	
}