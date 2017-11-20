package windall.console.account.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import windall.console.account.api.model.Tenant
import windall.console.account.api.persistence.Receipt
import java.time.LocalDateTime

@Repository
interface ReceiptRepository : JpaRepository<Receipt, Long> {

	fun findByTenant(tenant: Tenant): List<Receipt>

	@Query("SELECT r FROM #{#entityName} r WHERE r.paymentDateTime >= ?1")
	fun findByRecentPayments(cutoffDate: LocalDateTime): List<Receipt>

}