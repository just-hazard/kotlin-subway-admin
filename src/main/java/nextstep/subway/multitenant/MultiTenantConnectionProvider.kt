package nextstep.subway.multitenant

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class MultiTenantConnectionProvider : AbstractMultiTenantConnectionProvider() {

    init {
        addTenantConnectionProvider("dogpre", settingHikariDataSource())
        addTenantConnectionProvider("catpre", settingHikariDataSource1())
    }

    val connectProviderMap: HashMap<String, ConnectionProvider> = HashMap()

    override fun getAnyConnectionProvider(): ConnectionProvider? {
        return connectProviderMap["default"]
    }

    override fun selectConnectionProvider(tenantIdentifier: String?): ConnectionProvider? {
        return connectProviderMap[tenantIdentifier]
    }

    private fun addTenantConnectionProvider(
        tenantId: String,
        tenantDataSource: DataSource
    ) {
        val connectionProvider = DatasourceConnectionProviderImpl()
        connectionProvider.dataSource = tenantDataSource
        connectProviderMap[tenantId] = connectionProvider
    }

    private fun settingHikariDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://shop-db-dev.cluster-cqsnogbbspz8.ap-northeast-2.rds.amazonaws.com:3306/dogpre?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"
        config.driverClassName = "org.mariadb.jdbc.Driver"
        config.username = "shop_test"
        config.password = "funnc12014)(*&"
        config.connectionInitSql = "SET NAMES utf8mb4"
        config.poolName = "dogpre"

        return HikariDataSource(config)
    }

    private fun settingHikariDataSource1(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://shop-db-dev.cluster-cqsnogbbspz8.ap-northeast-2.rds.amazonaws.com:3306/catpre?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"
        config.driverClassName = "org.mariadb.jdbc.Driver"
        config.username = "shop_test"
        config.password = "funnc12014)(*&"
        config.connectionInitSql = "SET NAMES utf8mb4"
        config.poolName = "dogpre"

        return HikariDataSource(config)
    }
}
