package nextstep.subway.line.domain

import nextstep.subway.common.BaseEntity
import javax.persistence.*

@Entity
class Line : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(unique = true)
    var name: String? = null
        private set
    var color: String? = null
        private set

    constructor() {}
    constructor(name: String?, color: String?) {
        this.name = name
        this.color = color
    }

    fun update(line: Line) {
        name = line.name
        color = line.color
    }
}
