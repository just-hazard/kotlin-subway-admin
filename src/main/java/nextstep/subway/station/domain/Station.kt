package nextstep.subway.station.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.domain.Line
import javax.persistence.*

@Entity
class Station (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    var name: String,

    ) : BaseEntity() {
    fun isSameStation(station: Station) : Boolean {
        return this.name == station.name
    }

    constructor(name: String) : this(0, name)
    }
