package nextstep.subway.station.dto

import nextstep.subway.station.domain.Station
import java.time.LocalDateTime

class StationResponse {
    var id: Long? = null
        private set
    var name: String? = null
        private set
    var createdDate: LocalDateTime? = null
        private set
    var modifiedDate: LocalDateTime? = null
        private set

    constructor() {}
    constructor(id: Long?, name: String?, createdDate: LocalDateTime?, modifiedDate: LocalDateTime?) {
        this.id = id
        this.name = name
        this.createdDate = createdDate
        this.modifiedDate = modifiedDate
    }

    companion object {
        fun of(station: Station): StationResponse {
            return StationResponse(station.id, station.name, station.createdDate, station.modifiedDate)
        }
    }
}
