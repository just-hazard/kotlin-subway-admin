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
    var confirmAddSection = false
    fun validCheckAndAddSection(section: Section) {

        when(true) {
            addNewUpBoundLastStation(section) -> changeAddSection()
            addUpStation(section) -> changeAddSection()
            addDownStation(section) -> changeAddSection()
        }
    }

    private fun changeAddSection() {
        this.confirmAddSection = true
    }


    private fun addNewUpBoundLastStation(section: Section) : Boolean {
        if(this.sections.first().upStation == section.downStation) {
            this.sections.add(0, section)
            return true
        }
        return false
    }

    private fun addSection(section: Section) {
        this.sections.add(section)
    }

    // 상행역 등록
    // 잠실역, 건대입구역
    // 종합운동장역, 건대입구역
    private fun addUpStation(section: Section) : Boolean {
        sections.stream().filter {
            isSameStation(it.downStation, section.downStation)
        }.findFirst().ifPresent {
            it.changeUpStation(section)
            addSection(section)
            return@ifPresent
        }
        return false
    }

    // 하행역 등록
    // 잠실역, 건대입구역
    // 잠실역, 종합운동장역
    private fun addDownStation(section: Section) : Boolean {
        sections.stream().filter {
            isSameStation(it.upStation, section.upStation)
        }.findFirst().ifPresent {
            it.changeDownStation(section)
            addSection(section)
            return@ifPresent
        }
        return false
    }

    private fun isSameStation(
        oldStation: Station,
        newStation: Station,
    ) = oldStation.name == newStation.name
}
