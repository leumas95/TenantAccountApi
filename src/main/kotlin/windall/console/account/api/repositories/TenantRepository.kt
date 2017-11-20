package windall.console.account.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import windall.console.account.api.model.Tenant

@Repository
interface TenantRepository : JpaRepository<Tenant, Long> {
	
}