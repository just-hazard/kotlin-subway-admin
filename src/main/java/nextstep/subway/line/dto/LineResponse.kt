package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import nextstep.subway.station.dto.StationResponse
import java.time.LocalDateTime
import kotlin.streams.toList

class LineResponse(
    val id: Long,
    val name: String,
    val color: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val stations: List<StationResponse>
) {

    companion object {
        fun from(line: Line): LineResponse {
            val stations = line.sections.getSortStations()
            return LineResponse(line.id, line.name, line.color, line.createdDate, line.modifiedDate, convertStationAtStationResponse(stations))
    }

        private fun convertStationAtStationResponse(stations: List<Station>) =
            stations.stream().map {
                StationResponse.of(it)
            }.toList()
    }
}
