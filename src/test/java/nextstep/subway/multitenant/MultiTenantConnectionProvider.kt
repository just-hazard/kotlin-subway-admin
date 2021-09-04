package nextstep.subway.multitenant

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import org.springframework.stereotype.Component
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl
import java.util.*
import javax.sql.DataSource
import kotlin.collections.HashMap


@Component
class MultiTenantConnectionProvider : AbstractMultiTenantConnectionProvider() {

    val connectProviderMap: HashMap<String, ConnectionProvider> = HashMap()

    override fun getAnyConnectionProvider(): ConnectionProvider? {
        return connectProviderMap["default"]
    }

    override fun selectConnectionProvider(tenantIdentifier: String?): ConnectionProvider? {
        return connectProviderMap[tenantIdentifier]
    }

    private fun addTenantConnectionProvider(
        tenantId: String,
        tenantDataSource: DataSource,
        properties: Properties,
    ) {
        val connectionProvider = DatasourceConnectionProviderImpl()
        connectionProvider.dataSource = tenantDataSource
        connectionProvider.configure(properties)
        connectProviderMap[tenantId] = connectionProvider
    }
}
