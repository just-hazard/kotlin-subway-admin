package nextstep.subway.multitenant

import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@Component
class TenantConnectionProvider : MultiTenantConnectionProvider() {
    private val DEFAULT_TENANT = "dogpre"
    private var datasource: DataSource? = null

    fun TenantConnectionProvider(dataSource: DataSource?) {
        datasource = dataSource
    }

    @Throws(SQLException::class)
    override fun getAnyConnection(): Connection {
        return datasource!!.connection
    }

    @Throws(SQLException::class)
    override fun releaseAnyConnection(connection: Connection) {
        connection.close()
    }

    @Throws(SQLException::class)
    override fun getConnection(tenantIdentifier: String?): Connection? {
        val connection: Connection = anyConnection
        connection.setSchema(tenantIdentifier)
        return connection
    }

    @Throws(SQLException::class)
    override fun releaseConnection(tenantIdentifier: String?, connection: Connection) {
        connection.schema = DEFAULT_TENANT
        releaseAnyConnection(connection)
    }

    override fun supportsAggressiveRelease(): Boolean {
        return false
    }

    override fun isUnwrappableAs(aClass: Class<*>?): Boolean {
        return false
    }

    override fun <T> unwrap(aClass: Class<T>?): T? {
        return null
    }
}
