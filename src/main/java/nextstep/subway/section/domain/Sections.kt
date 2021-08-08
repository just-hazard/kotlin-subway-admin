package nextstep.subway.section.domain

import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.OneToMany
import kotlin.streams.toList

@Embeddable
class Sections(
    @OneToMany(mappedBy = "line", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sections: MutableList<Section>
) {
    constructor(line: Line, upStation: Station, downStation: Station, distance: Int) : this(mutableListOf()) {
        addSection(Section(0, line, upStation, downStation, distance))
    }

    fun getSortStations() : List<Station> {
        val stations : MutableList<Station> = mutableListOf()
        sections.stream()
            .forEach {
                when(compareDuplicateStation(it, stations)) {
                    true -> stations.add(it.downStation)
                    false -> stations.addAll(listOf(it.upStation,it.downStation))
                }
            }
        return stations
    }

    private fun compareDuplicateStation(
        it: Section,
        stations: MutableList<Station>,
    ): Boolean {
        if(stations.isEmpty()) {
            return false
        }
        return it.upStation == stations.last()
    }

    fun validCheckAndAddSection(section: Section) {
        addUpStation(section)
        addDownStation(section)
        addSection(section)
    }

    private fun addSection(section: Section) {
        this.sections.add(section)
    }

    // 상행역 등록
    // 잠실역, 건대입구역
    // 종합운동장역, 건대입구역
    private fun addUpStation(section: Section) {
        sections.stream().filter {
            isSameStation(it.downStation, section.downStation)
        }.findFirst().ifPresent {
            it.changeUpStation(section)
        }
    }

    // 하행역 등록
    // 잠실역, 건대입구역
    // 잠실역, 종합운동장역
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
