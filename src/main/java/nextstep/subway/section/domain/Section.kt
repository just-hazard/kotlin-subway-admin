package nextstep.subway.section.domain

import nextstep.subway.common.BaseEntity
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

    var distance: Int
) : BaseEntity() {
    fun changeDownStation(newSection: Section) {
        changeDistance(newSection.distance)
        // 하행역 등록 (역과 역 사이)
        val station = this.downStation
        this.downStation = newSection.downStation
        newSection.upStation = newSection.downStation
        newSection.downStation = station

    }

    private fun changeDistance(distance: Int) {
        if(confirmDistanceZero()){
            compareDistance(distance)
            this.distance -= distance
            return
        }
        this.distance = distance
    }

    private fun compareDistance(distance: Int) {
        if(this.distance <= distance) {
            throw IllegalArgumentException("기존 거리보다 더 멀 수 없습니다.")
        }
    }

    private fun confirmDistanceZero(): Boolean {
        return this.distance != 0
    }
}
