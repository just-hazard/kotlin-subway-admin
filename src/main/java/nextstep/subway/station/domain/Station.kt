package nextstep.subway.station.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.domain.Line
import javax.persistence.*

@Entity
class Station (
    @Column(unique = true)
    var name: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun isSameStation(station: Station) : Boolean {
        return this.name == station.name
    }
}
