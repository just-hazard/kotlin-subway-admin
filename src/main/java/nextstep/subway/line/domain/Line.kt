package nextstep.subway.line.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.section.domain.Sections
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Line(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    var name: String,
    var color: String,
    @Embedded
    var sections: Sections = Sections(mutableListOf())
) : BaseEntity() {

    fun update(line: Line) {
        name = line.name
        color = line.color
    }

    constructor(name: String, color: String): this(0, name, color, Sections(mutableListOf()))

    companion object {
        fun of(name: String, color: String, upstation: Station, downStation: Station, distance: Int) : Line {
            return Line(0,name, color, Sections(this, upstation, downStation, distance))
        }
    }
}
