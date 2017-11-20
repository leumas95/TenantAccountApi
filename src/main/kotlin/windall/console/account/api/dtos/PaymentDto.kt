package windall.console.account.api.dtos

import java.time.LocalDateTime
import java.io.Serializable

class PaymentDto (
		var amount: Long
) : Serializable {
	
	@Suppress("unused")
	private constructor(): this(amount = 0)
	
}