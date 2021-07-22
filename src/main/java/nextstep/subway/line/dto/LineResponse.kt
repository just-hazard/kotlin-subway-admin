package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import java.time.LocalDateTime

class LineResponse(
    val id: Long,
    val name: String,
    val color: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val stations: List<Station>
) {

    companion object {
        fun of(line: Line): LineResponse {
            return LineResponse(line.id, line.name, line.color, line.createdDate, line.modifiedDate, line.stations)
        }
    }
}
