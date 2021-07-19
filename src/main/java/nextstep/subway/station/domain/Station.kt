package nextstep.subway.station.domain

import nextstep.subway.common.BaseEntity
import javax.persistence.*

@Entity
class Station : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(unique = true)
    var name: String? = null
        private set

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }
}
