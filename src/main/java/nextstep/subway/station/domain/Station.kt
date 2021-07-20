package nextstep.subway.station.domain

import nextstep.subway.common.BaseEntity
import javax.persistence.*

@Entity
class Station (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    var name: String
    ) : BaseEntity() {
        constructor(name: String) : this(0, name)
    }
