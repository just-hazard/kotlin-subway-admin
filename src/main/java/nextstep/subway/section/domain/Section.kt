package nextstep.subway.section.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.common.ErrorMessage
import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Section (
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long,
    @ManyToOne
    var line: Line,
    @ManyToOne
    var upStation: Station,
    @ManyToOne
    var downStation: Station,
    @Embedded
    var distance: Distance
) : BaseEntity() {
    fun changeUpStation(newSection: Section) {
        changeDistance(newSection.distance)
        // 상행역 등록 (역과 역 사이)
        this.downStation = newSection.upStation
    }

    fun changeDownStation(newSection: Section) {
        changeDistance(newSection.distance)
        // 하행역 등록 (역과 역 사이)
        val station = this.downStation
        this.downStation = newSection.downStation
        newSection.upStation = newSection.downStation
        newSection.downStation = station
    }

    private fun changeDistance(newSectionDistance: Distance) {
        if(confirmDistanceZero()){
            compareDistance(newSectionDistance)
            this.distance.distanceCorrection(newSectionDistance)
            return
        }
        this.distance = newSectionDistance
    }

    private fun compareDistance(distance: Distance) {
        if(this.distance.validConfirmNewDistance(distance)) {
            throw IllegalArgumentException(ErrorMessage.DISTANCE_OVER)
        }
    }

    private fun confirmDistanceZero(): Boolean {
        return distance.validConfirmGreaterThanZero()
    }

    fun changeUpStationAndDistance(scheduledToBeDeletedSection: Section) {
        this.upStation = scheduledToBeDeletedSection.upStation
        this.distance.plusDistance(scheduledToBeDeletedSection.distance)
    }
}
