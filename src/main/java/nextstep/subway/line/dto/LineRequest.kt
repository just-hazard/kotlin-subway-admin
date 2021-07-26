package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line

class LineRequest(
    val name: String,
    val color: String,
    val upStationId: Long,
    val downStationId: Long,
    val distance: Int
) {

    fun toLine(): Line {
        return Line(name, color)
    }
}
