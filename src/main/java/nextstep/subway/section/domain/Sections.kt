package nextstep.subway.section.domain

import nextstep.subway.common.ErrorMessage
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
        this.sections.add(Section(line, upStation, downStation, distance))
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
            throw IllegalArgumentException(ErrorMessage.NON_EXISTENT_SECTION)
        }
    }

    private fun makeSureItExistsSection(section: Section) {
        this.sections.stream().filter {
            it.upStation == section.upStation && it.downStation == section.downStation
        }.findFirst().ifPresent {
            throw IllegalArgumentException(ErrorMessage.EXISTENT_SECTION)
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

    // ????????? ??????
    // ?????????, ???????????????
    // ??????????????????, ???????????????
    private fun addUpStation(section: Section) {
        sections.stream().filter {
            isSameStation(it.downStation, section.downStation)
        }.findFirst().ifPresent {
            it.changeUpStation(section)
            addSection(sections.indexOf(it), section)
        }
    }

    // ????????? ??????
    // ?????????, ???????????????
    // ?????????, ??????????????????
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


    // ?????? ??????
    // ?????? ??????
    fun removeSection(station: Station) {
        // ?????? ????????? Section Size??? ????????? ??? ?????? ?????? ??? ??????
        validCheckSectionOnlyOne()
        // ???????????? ??????
        // ???????????? ????????? ?????? ??? ?????? ??????
        // ???????????? ???????????? ???????????? ????????? ?????? ??? ?????? ??? ????????? ??????
        // ????????? ??????????????? ?????? ??????
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
        throw EntityNotFoundException(ErrorMessage.NON_EXISTENT_STATION)
    }

    private fun validCheckSectionOnlyOne() {
        if(sections.size < REMOVE_SECTION_SIZE) {
            throw IllegalArgumentException(ErrorMessage.CAN_NOT_REMOVE_SECTION)
        }
    }
}
