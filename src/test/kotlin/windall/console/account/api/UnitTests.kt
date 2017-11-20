package windall.console.account.api

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import windall.console.account.api.dtos.TenantDto
import windall.console.account.api.model.Tenant
import windall.console.account.api.dtos.PaymentDto
import kotlin.test.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Test
	fun contextLoads() {
	}
	
	@Test
	fun exactPayments() {
		val rent = 10L
		val tenant = Tenant(TenantDto(name = "Test Tenant (Exact Payments)", weeklyRent = rent))
		val startingDate = tenant.paidTill
		var totalWeeks = 0L
		for (weeks in 1..5) {
			totalWeeks += weeks
			tenant.makePayment(PaymentDto(amount = weeks * rent))
			assertEquals(0L, tenant.rentSurplus, "Rent surplus was not the expected value.")
			assertEquals(startingDate.plusWeeks(totalWeeks), tenant.paidTill, "The paid till date was not the expected value")
		}
	}
	
	@Test
	fun paymentsWithRemainders() {
		val rent = 10L
		val tenant = Tenant(TenantDto(name = "Test Tenant (Payments With Remainders)", weeklyRent = rent))
		val startingDate = tenant.paidTill
		var totalWeeks = 0L
		var totalRemainder = 0L
		for (weeks in 1..5) {
			totalWeeks += weeks
			totalRemainder++ 
			tenant.makePayment(PaymentDto(amount = weeks * rent + 1))
			assertEquals(totalRemainder, tenant.rentSurplus, "Rent surplus was not the expected value.")
			assertEquals(startingDate.plusWeeks(totalWeeks), tenant.paidTill, "The paid till date was not the expected value")
		}
	}
	
}
