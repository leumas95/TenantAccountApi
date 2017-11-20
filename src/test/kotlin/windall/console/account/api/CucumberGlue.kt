package windall.console.account.api

import com.fasterxml.jackson.databind.ObjectMapper
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import windall.console.account.api.dtos.TenantDto
import windall.console.account.api.model.Tenant
import java.nio.charset.Charset
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = arrayOf(Application::class))
class CucumberGlue () {
	
	companion object {
		val logger = LoggerFactory.getLogger(CucumberGlue::class.java)
	}
	
	@Autowired
	internal lateinit var webApplicationContext: WebApplicationContext
	
	internal val contentType = MediaType(
				MediaType.APPLICATION_JSON.getType(),
				MediaType.APPLICATION_JSON.getSubtype(),
				Charset.forName("utf8")
	)
	
	internal lateinit var mockMvc: MockMvc
	
	internal var lastTennantNamed: String? = null;
	
	// Used during GIVEN steps to set up tenants
	internal val tenants = HashMap<String, TenantDto>()
	
	// Used during WHEN and THEN steps to interact with tenants
	internal val registeredTenants = HashMap<String, Tenant>()
	
	@Before
	fun setup() {
		logger.debug("Setting up Cucumber scenario...")
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
		
	@Given("^that I have a tenant called (.+) who's rent is \\$(\\d+) a week$")
	fun newTenant(name: String,	rent: Long) {
		logger.debug("New tenant: name='${name}', rent=\$${rent}")
		val tenant = TenantDto(name = name,	weeklyRent = rent)
		lastTennantNamed = name
		assertNull(tenants.put(name, tenant), "Tenants in test scenarios can't have duplicate names.")
	}
	
	@Given("^their current rent credit is \\$(\\d+)$")
	fun setTenantCredit(surplus: Long) {
		assertNotNull(lastTennantNamed, "Using 'their' as a name only works if you have already interacted with a tenant.")
		setTenantCredit(lastTennantNamed!!, surplus)
	}
	
	@Given("^(.+)'s current rent credit is \\$(\\d+)$")
	fun setTenantCredit(name: String, surplus: Long) {
		logger.debug("Update tenant: name='${name}', rentSurplus=\$${surplus}")
		val tenant = tenants.get(name)
		assertNotNull(tenant, "To set rent credit tenant must be previously described.")
		lastTennantNamed = name
		tenant!!.rentSurplus = surplus
	}
	
	@Given("^their current paid-to-date is (\\d{4}-\\d{2}-\\d{2})$")
	fun setTenantPaidTill(paidTill: String) {
		assertNotNull(lastTennantNamed, "Using 'their' as a name only works if you have already interacted with a tenant.")
		setTenantPaidTill(lastTennantNamed!!, paidTill)
	}
	
	@Given("^(.+)'s current paid-to-date is (\\d{4}-\\d{2}-\\d{2})$")
	fun setTenantPaidTill(name: String, paidTill: String) {
		logger.debug("Update tenant: name='${name}', paidTill=${paidTill}")
		val tenant = tenants.get(name)
		assertNotNull(tenant, "To set rent credit tenant must be previously described.")
		lastTennantNamed = name
		tenant!!.paidTill = LocalDate.parse(paidTill)
	}
	
	@Given("^they are registered in the account system$")
	fun registerTenant() {
		//Register all the tenants described
		for((name, requestTenant) in tenants) {
			logger.debug("Register tenant: name='${name}'")
			//Send the request
			val request = post("/tenants")
					.content(tenantDtoToJson(requestTenant))
					.contentType(contentType)
			val result = mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andReturn()
			//Check the responce has the same data you sent up and the database id has been populated
			val responceRegisteredTenant = jsonToTenant(result.response.contentAsString)
			assertNotNull(responceRegisteredTenant.id, "The registered tenant id is not set.")
			assertEquals(responceRegisteredTenant.name, requestTenant.name, "The registered tenant name is not what was expected.")
			assertEquals(responceRegisteredTenant.weeklyRent, requestTenant.weeklyRent, "The registered tenant weekly rent is not what was expected.")
			assertEquals(responceRegisteredTenant.paidTill, requestTenant.paidTill, "The registered tenant paid till date is not what was expected.")
			assertEquals(responceRegisteredTenant.rentSurplus, requestTenant.rentSurplus, "The registered tenant rent surplus is not what was expected.")
			//Save a refrence to the registeredTenant so we can do further operations on it
			registeredTenants.put(name, responceRegisteredTenant)
		}
	}
	
	@When("^they pay \\$(\\d+)")
	fun tenantPays(amount: Long) {
		assertNotNull(lastTennantNamed, "Using 'they' as a name only works if you have already interacted with a tenant.")
		tenantPays(lastTennantNamed!!, amount)
	}
	
	@When("^(.+) pays \\$(\\d+)")
	fun tenantPays(name: String, amount: Long) {
		logger.debug("Money from tenant: name='${name}', amount=\$${amount}")
		val requestRegisteredTenant = registeredTenants.get(name)
		assertNotNull(requestRegisteredTenant, "To pay a tenant must be registered in the system.")
		lastTennantNamed = name
		val request = post("/tenants/${requestRegisteredTenant!!.id}/pay")
			.content("{\"amount\": \"${amount}\"}")
			.contentType(contentType)
		val result = mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andReturn()
		val responceRegisteredTenant = jsonToTenant(result.response.contentAsString)
		assertEquals(responceRegisteredTenant.id, requestRegisteredTenant.id, "The registered tenant id is not the same.")
		assertEquals(responceRegisteredTenant.name, requestRegisteredTenant.name, "The registered tenant name is not what was expected.")
		assertEquals(responceRegisteredTenant.weeklyRent, requestRegisteredTenant.weeklyRent, "The registered tenant weekly rent is not what was expected.")
		registeredTenants.put(name, responceRegisteredTenant)
	}
	
	@Then("^their paid-to-date will be advanced to (\\d{4}-\\d{2}-\\d{2})$")
	fun checkPaidTill(paidTill: String) {
		assertNotNull(lastTennantNamed, "Using 'their' as a name only works if you have already interacted with a tenant.")
		checkPaidTill(lastTennantNamed!!, paidTill)
	}
	
	@Then("^(.+)'s paid-to-date will be advanced to (\\d{4}-\\d{2}-\\d{2})$")
	fun checkPaidTill(name: String, paidTill: String) {	
		logger.debug("Checking tenant paidTill: name='${name}'")
		val registeredTenant = registeredTenants.get(name)
		assertNotNull(registeredTenant, "This step is only for registered tenants.")
		lastTennantNamed = name
		assertEquals(registeredTenant!!.paidTill, LocalDate.parse(paidTill), "The registered tenant paid till date is not what was expected.")
	}
	
	@Then("^their rent credit will be updated to \\$(\\d+)$")
	fun checkRentSurplus(surplus: Long) {
		assertNotNull(lastTennantNamed, "Using 'their' as a name only works if you have already interacted with a tenant.")
		checkRentSurplus(lastTennantNamed!!, surplus)
	}
	
	@Then("^(.+)'s rent credit will be updated to \\$(\\d+)$")
	fun checkRentSurplus(name: String, surplus: Long) {
		logger.debug("Checking tenant rentSurplus: name='${name}'")
		val registeredTenant = registeredTenants.get(name)
		assertNotNull(registeredTenant, "This step is only for registered tenants.")
		lastTennantNamed = name
		assertEquals(registeredTenant!!.rentSurplus, surplus, "The registered tenant rent surplus is not what was expected.")
	}
	
	internal fun tenantDtoToJson(tenant: TenantDto) = """
			{
				"name": "${tenant.name}",
				"weeklyRent": "${tenant.weeklyRent}",
				"paidTill": "${tenant.paidTill}",
				"rentSurplus": "${tenant.rentSurplus}"
			}
			""".trimIndent()
	
	internal fun jsonToTenant(json: String): Tenant {
		val root = ObjectMapper().readTree(json);
		return Tenant(
				id = root.path("id").asLong(),
				name =  root.path("name").asText(),
				weeklyRent =  root.path("weeklyRent").asLong(),
				paidTill =  LocalDate.parse(root.path("paidTill").asText()),
				rentSurplus = root.path("rentSurplus").asLong()
		)
	}
	
}
