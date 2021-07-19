package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line

class LineRequest {
    var name: String? = null
        private set
    var color: String? = null
        private set

    constructor() {}
    constructor(name: String?, color: String?) {
        this.name = name
        this.color = color
    }

    fun toLine(): Line {
        return Line(name, color)
    }
}
