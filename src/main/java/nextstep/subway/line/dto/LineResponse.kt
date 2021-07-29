package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line
import nextstep.subway.station.dto.StationResponse
import java.time.LocalDateTime

class LineResponse(
    val id: Long,
    val name: String,
    val color: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val stations: List<StationResponse>
) {

    companion object {
        fun of(line: Line): LineResponse {
            val stations = findSectionChangeStations(line)

            return LineResponse(line.id, line.name, line.color, line.createdDate, line.modifiedDate, stations)
        }

        private fun findSectionChangeStations(line: Line) = line.sections.sections.map {
            StationResponse.of(it.upStation)
            StationResponse.of(it.downStation)
        }.toList()
    }
}
