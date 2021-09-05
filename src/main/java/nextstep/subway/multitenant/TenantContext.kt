package nextstep.subway.multitenant

class TenantContext {

    companion object {
        private val currentTenant: ThreadLocal<String?> = InheritableThreadLocal()

        fun getCurrentTenant(): String? {
            return currentTenant.get()
        }

        fun setCurrentTenant(tenant: String?) {
            currentTenant.set(tenant)
        }

        fun clear() {
            currentTenant.set(null)
        }
    }
}
