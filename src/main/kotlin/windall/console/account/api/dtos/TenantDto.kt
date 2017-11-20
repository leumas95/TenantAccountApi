package windall.console.account.api.dtos

import java.time.LocalDate
import java.io.Serializable

data class TenantDto (
		var name: String,
		var weeklyRent: Long,
		var paidTill: LocalDate = LocalDate.now(),
		var rentSurplus: Long = 0
) : Serializable {
	
	@Suppress("unused")
	private constructor(): this(
			name = "",
			weeklyRent = 0
	)
	
}