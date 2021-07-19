package nextstep.subway.station.dto

import nextstep.subway.station.domain.Station

class StationRequest {
    val name: String? = null
    fun toStation(): Station {
        return Station(name)
    }
}
