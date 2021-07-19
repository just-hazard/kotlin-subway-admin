package nextstep.subway.line.dto

import nextstep.subway.line.domain.Line
import java.time.LocalDateTime

class LineResponse {
    var id: Long? = null
        private set
    var name: String? = null
        private set
    var color: String? = null
        private set
    var createdDate: LocalDateTime? = null
        private set
    var modifiedDate: LocalDateTime? = null
        private set

    constructor() {}
    constructor(id: Long?, name: String?, color: String?, createdDate: LocalDateTime?, modifiedDate: LocalDateTime?) {
        this.id = id
        this.name = name
        this.color = color
        this.createdDate = createdDate
        this.modifiedDate = modifiedDate
    }

    companion object {
        fun of(line: Line): LineResponse {
            return LineResponse(line.id, line.name, line.color, line.createdDate, line.modifiedDate)
        }
    }
}
