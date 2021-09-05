package nextstep.subway.multitenant

import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.stereotype.Component

@Component
class TenantContext {

    companion object {
        val DEFAULT_TENANT_IDENTIFIER = "dogpre"

        val TENANT_IDENTIFIER = ThreadLocal<String>()
    }

    fun setTenant(tenantIdentifier: String?) {
        TENANT_IDENTIFIER.set(tenantIdentifier)
    }

    fun reset(tenantIdentifier: String?) {
        TENANT_IDENTIFIER.remove()
    }

    class TenantIdentifierResolver : CurrentTenantIdentifierResolver {
        override fun resolveCurrentTenantIdentifier(): String {
            val currentTenantId: String = TENANT_IDENTIFIER.get()
            return currentTenantId ?: DEFAULT_TENANT_IDENTIFIER
        }

        override fun validateExistingCurrentSessions(): Boolean {
            return false
        }
    }
}
