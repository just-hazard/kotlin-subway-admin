package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line

class LineRequest(
    val name: String,
    val color: String
) {

    fun toLine(): Line {
        return Line(name, color)
    }
}
