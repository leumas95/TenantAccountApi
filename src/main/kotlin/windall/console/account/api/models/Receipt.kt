package windall.console.account.api.persistence

import com.fasterxml.jackson.annotation.JsonIgnore
import windall.console.account.api.dtos.PaymentDto
import windall.console.account.api.model.Tenant
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Receipt (
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id: Long? = null,
		
		val amount: Long,
		
		@JsonIgnore
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "tenant_id")
		val tenant: Tenant? = null,

		@Column(name = "payment_datetime")
		val paymentDateTime: LocalDateTime = LocalDateTime.now()

) {
	
	@Suppress("unused")
	private constructor(): this(amount = 0)
	
	constructor(dto: PaymentDto, tenant: Tenant) : this(amount = dto.amount, tenant = tenant)
	
}