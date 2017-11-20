package windall.console.account.api.dtos

import java.io.Serializable
import java.time.LocalDate

data class TenantDto (
		var name: String,
		var weeklyRent: Long,
		var paidTill: LocalDate = LocalDate.now(),
		var rentSurplus: Long = 0
) : Serializable {
	
	//Unused constructor for serialization
	@Suppress("unused")
	private constructor(): this(
			name = "",
			weeklyRent = 0
	)
	
}