package nextstep.subway.utils

import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.InitializingBean
import javax.persistence.EntityManager
import com.google.common.base.CaseFormat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import javax.persistence.Entity
import javax.persistence.PersistenceContext
import javax.persistence.metamodel.EntityType

@Service
@ActiveProfiles("test")
class DatabaseCleanup : InitializingBean {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    private var tableNames: List<String>? = null
    override fun afterPropertiesSet() {
        tableNames = entityManager!!.metamodel.entities.stream()
            .filter { e: EntityType<*> ->
                e.javaType.getAnnotation(
                    Entity::class.java) != null
            }
            .map { e: EntityType<*> -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.name) }
            .collect(Collectors.toList())
    }

    @Transactional
    fun execute() {
        entityManager!!.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        for (tableName in tableNames!!) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
}
