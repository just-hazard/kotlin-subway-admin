package nextstep.subway.multitenant

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@Component
class TenantConnectionProvider(private val datasource: DataSource) : MultiTenantConnectionProvider {
    private val DEFAULT_TENANT = "public"

    @Throws(SQLException::class)
    override fun getAnyConnection(): Connection {
        return datasource.connection
    }

    @Throws(SQLException::class)
    override fun releaseAnyConnection(connection: Connection) {
        connection.close()
    }

    @Throws(SQLException::class)
    override fun getConnection(tenantIdentifier: String): Connection {
        logger.info("Get connection for tenant {}", tenantIdentifier)
        val connection = anyConnection
        connection.schema = tenantIdentifier
        return connection
    }

    @Throws(SQLException::class)
    override fun releaseConnection(tenantIdentifier: String, connection: Connection) {
        logger.info("Release connection for tenant {}", tenantIdentifier)
        connection.schema = DEFAULT_TENANT
        releaseAnyConnection(connection)
    }

    override fun supportsAggressiveRelease(): Boolean {
        return false
    }

    override fun isUnwrappableAs(aClass: Class<*>?): Boolean {
        return false
    }

    override fun <T> unwrap(aClass: Class<T>): T? {
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TenantConnectionProvider::class.java)
    }
}
