package windall.console.account.api.dtos

import java.io.Serializable

class PaymentDto (
		var amount: Long
) : Serializable {
	
	//Unused constructor for serialization
	@Suppress("unused")
	private constructor(): this(amount = 0)
	
}