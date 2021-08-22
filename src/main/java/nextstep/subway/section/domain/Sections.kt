package nextstep.subway.section.domain

import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.EntityNotFoundException
import javax.persistence.OneToMany

@Embeddable
class Sections(
    @OneToMany(mappedBy = "line", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sections: MutableList<Section>
) {
    companion object {
        private const val REMOVE_SECTION_SIZE = 2
        private const val INDEX_INCREASE = 1
    }

    constructor(line: Line, upStation: Station, downStation: Station, distance: Distance) : this(mutableListOf()) {
        this.sections.add(Section(0, line, upStation, downStation, distance))
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

    private var confirmAddSection: Boolean = false
    fun validCheckAndAddSection(section: Section) {
        nonExistentStation(section)
        makeSureItExistsSection(section)

        if(!confirmAddSection) {
            addNewUpBoundLastStation(section)
        }
        if(!confirmAddSection) {
            addUpStation(section)
        }
        if(!confirmAddSection) {
            addDownStation(section)
        }
        if(!confirmAddSection) {
            addNewDownBoundLastStation(section)
        }

        confirmAddSection = false
    }

    private fun nonExistentStation(section: Section) {
        val stations = getSortStations()
        if (!stations.contains(section.upStation) && !stations.contains(section.downStation)) {
            throw IllegalArgumentException("존재하지 않는 상하행역입니다.")
        }
    }

    private fun makeSureItExistsSection(section: Section) {
        this.sections.stream().filter {
            it.upStation == section.upStation && it.downStation == section.downStation
        }.findFirst().ifPresent {
            throw IllegalArgumentException("이미 존재하는 상하행역입니다.")
        }
    }

    private fun addNewDownBoundLastStation(section: Section) {
        if(this.sections.last().downStation == section.upStation) {
            this.sections.add(newLastSectionIndex(), section)
            confirmCheckAddSection(true)
        }
    }

    private fun newLastSectionIndex() = sections.size

    private fun addNewUpBoundLastStation(section: Section) {
        if(this.sections.first().upStation == section.downStation) {
            this.sections.add(0, section)
            confirmCheckAddSection(true)
        }
    }

    private fun addSection(index: Int, section: Section) {
        this.sections.add(index + INDEX_INCREASE, section)
        confirmCheckAddSection(true)
    }

    private fun confirmCheckAddSection(registrationOrNot: Boolean) {
        confirmAddSection = registrationOrNot
    }

    // 상행역 등록
    // 잠실역, 건대입구역
    // 종합운동장역, 건대입구역
    private fun addUpStation(section: Section) {
        sections.stream().filter {
            isSameStation(it.downStation, section.downStation)
        }.findFirst().ifPresent {
            it.changeUpStation(section)
            addSection(sections.indexOf(it), section)
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
            addSection(sections.indexOf(it), section)
        }
    }

    private fun isSameStation(
        oldStation: Station,
        newStation: Station,
    ) = oldStation.name == newStation.name


    // 잠실 종합
    // 종합 건대
    fun removeSection(station: Station) {
        // 예외 케이스 Section Size가 하나일 때 삭제 요청 시 예외
        validCheckSectionOnlyOne()
        // 시나리오 정리
        // 일치하는 하행역 검색 후 섹션 담기
        // 상행역을 기준으로 일치하는 섹션을 찾은 후 거리 및 상행역 교체
        // 기존의 삭제하려던 섹션 삭제
        val scheduledToBeDeletedSection = findSection(station)
        sections[findNextSectionIndex(scheduledToBeDeletedSection)].changeUpStationAndDistance(scheduledToBeDeletedSection)
        sections.remove(scheduledToBeDeletedSection)
    }

    private fun findNextSectionIndex(scheduledToBeDeletedSection: Section) : Int {
        return sections.indexOf(scheduledToBeDeletedSection) + INDEX_INCREASE
    }

    private fun findSection(station: Station) = sections.stream().filter {
        it.downStation.isSameStation(station)
    }.findFirst().orElseThrow {
        throw EntityNotFoundException("해당 지하철역이 존재하지 않습니다.")
    }

    private fun validCheckSectionOnlyOne() {
        if(sections.size < REMOVE_SECTION_SIZE) {
            throw IllegalArgumentException("존재하는 상하행역이 하나뿐이라 삭제할 수 없습니다.")
        }
    }
}
