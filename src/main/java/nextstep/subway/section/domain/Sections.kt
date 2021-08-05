package nextstep.subway.section.domain

import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.OneToMany

@Embeddable
class Sections(
    @OneToMany(mappedBy = "line", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sections: MutableList<Section>
) {
    constructor(line: Line, upStation: Station, downStation: Station, distance: Int) : this(mutableListOf()) {
        sections.add(Section(0,line, upStation, downStation, distance))
    }

    fun getSortStations() : List<Station> {
        val stations : MutableList<Station> = mutableListOf()
        sections.stream().forEach {
            stations.add(it.upStation)
            stations.add(it.downStation)
        }
        return stations
    }

    fun validCheckAndAddSection(section: Section) {
        addDownStation(section)
        this.sections.add(section)
    }

    // 하행역 등록
    // 시나리오
    // 잠실역 건대입구역 (기존)
    // 잠실역 종합운동장역
    private fun addDownStation(section: Section) {
        sections.stream().filter {
            isSameStation(it.upStation, section.upStation)
        }.findFirst().ifPresent {
            it.changeDownStation(section)
        }
    }

    private fun isSameStation(
        oldStation: Station,
        newStation: Station,
    ) = oldStation.name == newStation.name
}
