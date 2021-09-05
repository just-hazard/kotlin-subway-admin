package nextstep.subway.multitenant

import org.hibernate.MultiTenancyStrategy
import org.hibernate.cfg.Environment
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource


@Configuration
class HibernateConfig {
    @Autowired
    private val jpaProperties: JpaProperties? = null
    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter {
        return HibernateJpaVendorAdapter()
    }

    @Bean
    fun entityManagerFactory(
        dataSource: DataSource?,
        multiTenantConnectionProviderImpl: MultiTenantConnectionProvider?,
        currentTenantIdentifierResolverImpl: CurrentTenantIdentifierResolver?,
    ): LocalContainerEntityManagerFactoryBean {
        val jpaPropertiesMap: MutableMap<String, Any?> = HashMap(
            jpaProperties!!.properties)
        jpaPropertiesMap[Environment.MULTI_TENANT] = MultiTenancyStrategy.SCHEMA
        jpaPropertiesMap[Environment.MULTI_TENANT_CONNECTION_PROVIDER] = multiTenantConnectionProviderImpl
        jpaPropertiesMap[Environment.MULTI_TENANT_IDENTIFIER_RESOLVER] = currentTenantIdentifierResolverImpl
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("nextstep.subway*")
        em.jpaVendorAdapter = jpaVendorAdapter()
        em.jpaPropertyMap = jpaPropertiesMap
        return em
    }
}
