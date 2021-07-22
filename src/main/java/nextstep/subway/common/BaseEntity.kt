package nextstep.subway.common

import javax.persistence.EntityListeners
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import org.springframework.data.annotation.LastModifiedDate
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity (
    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var modifiedDate: LocalDateTime = LocalDateTime.now()
)
