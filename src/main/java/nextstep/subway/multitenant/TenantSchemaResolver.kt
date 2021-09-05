package nextstep.subway.multitenant

import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.stereotype.Component

@Component
class TenantSchemaResolver : CurrentTenantIdentifierResolver {

    private val defaultTenant = "dogpre"

    override fun resolveCurrentTenantIdentifier(): String {
        val tenant: String? = TenantContext.getCurrentTenant()
        return tenant ?: defaultTenant
    }

    override fun validateExistingCurrentSessions(): Boolean {
        return true
    }
}
