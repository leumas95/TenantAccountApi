package windall.console.account.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import windall.console.account.api.dtos.PaymentDto
import windall.console.account.api.dtos.TenantDto
import windall.console.account.api.persistence.Receipt
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Tenant (
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id: Long? = null,
		
		val name: String,
		
		@Column(name = "weekly_rent")
		val weeklyRent: Long,
		
		@Column(name = "paid_till")
		var paidTill: LocalDate,
		
		@Column(name = "rent_surplus")
		var rentSurplus: Long,
		
		@JsonIgnore
		@OneToMany(mappedBy = "tenant", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
	    val receipts: MutableList<Receipt> = ArrayList<Receipt>()

) {
	
	@Suppress("unused")
	private constructor(): this (
			name = "",
			weeklyRent = 0,
			paidTill = LocalDate.now(),
			rentSurplus = 0
	)
	
	constructor(dto: TenantDto): this (
			name = dto.name,
			weeklyRent = dto.weeklyRent,
			paidTill = dto.paidTill,
			rentSurplus = dto.rentSurplus
	)
	
	fun makePayment(payment: PaymentDto) {
		// Calculate how much money there is to work with
		val total = rentSurplus + payment.amount
		// Calculate how many full weeks rent can be covered
		val weeks = total / weeklyRent
		// Calculate how much will be left over
		rentSurplus = total % weeklyRent
		// Calculate the new paid to date
		paidTill = paidTill.plusWeeks(weeks.toLong())
		// Create a recipt from this payment and add it to the tenants records
		val receipt = Receipt(payment, this)
		receipts.add(receipt)
	}
	
}