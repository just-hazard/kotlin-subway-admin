package nextstep.subway.multitenant

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource

open class MultiTenantConnectionProvider : AbstractMultiTenantConnectionProvider() {

    private val dataSource: DataSource = TODO()

    init {
        addTenantConnectionProvider("dogpre", settingHikariDataSource())
        addTenantConnectionProvider("catpre", settingHikariDataSource1())
    }

    val connectProviderMap: HashMap<String, ConnectionProvider> = HashMap()

    override fun getAnyConnectionProvider(): ConnectionProvider? {
        return connectProviderMap["dogpre"]
    }


    override fun selectConnectionProvider(tenantIdentifier: String): ConnectionProvider? {
        return connectProviderMap[tenantIdentifier]
    }

    @PostConstruct
    private fun addTenantConnectionProvider(
        tenantId: String,
        tenantDataSource: DataSource
    ) {
        val connectionProvider = DatasourceConnectionProviderImpl()
        connectionProvider.dataSource = tenantDataSource
        connectProviderMap.put(tenantId,connectionProvider)
    }

    private fun settingHikariDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://shop-db-dev.cluster-cqsnogbbspz8.ap-northeast-2.rds.amazonaws.com:3306/dogpre?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"
        config.driverClassName = "org.mariadb.jdbc.Driver"
        config.username = "admin"
        config.password = "funnc1234"
        config.connectionInitSql = "SET NAMES utf8mb4"
        config.poolName = "dogpre"

        return HikariDataSource(config)
    }

    private fun settingHikariDataSource1(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://shop-db-dev.cluster-cqsnogbbspz8.ap-northeast-2.rds.amazonaws.com:3306/catpre?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"
        config.driverClassName = "org.mariadb.jdbc.Driver"
        config.username = "admin"
        config.password = "funnc1234"
        config.connectionInitSql = "SET NAMES utf8mb4"
        config.poolName = "dogpre"

        return HikariDataSource(config)
    }
}
