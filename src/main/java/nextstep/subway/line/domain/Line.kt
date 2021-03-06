package nextstep.subway.line.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.application.LineService
import nextstep.subway.section.domain.Distance
import nextstep.subway.section.domain.Section
import nextstep.subway.section.domain.Sections
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Line(
    @Column(unique = true)
    var name: String,
    var color: String,
    @Embedded
    var sections: Sections = Sections(mutableListOf())
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(line: Line) {
        name = line.name
        color = line.color
    }

    fun addSection(section: Section) {
        sections.validCheckAndAddSection(section)
        section.line = this
    }

    fun removeSectionMessage(station: Station) {
        this.sections.removeSection(station)
    }

    constructor(name: String, color: String): this(name, color, Sections(mutableListOf()))

    companion object {
        fun of(name: String, color: String, upStation: Station, downStation: Station, distance: Distance) : Line {
            val line = Line(name,color)
            line.sections = Sections(line, upStation, downStation, distance)
            return line
        }
    }
}
