package nextstep.subway.station.dto

import com.sun.istack.NotNull
import nextstep.subway.station.domain.Station


class StationRequest(
    @get:NotNull
    var name: String
) {
    fun toStation(): Station {
        return Station(name)
    }
}
