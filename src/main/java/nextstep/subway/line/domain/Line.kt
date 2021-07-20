package nextstep.subway.line.domain

import nextstep.subway.common.BaseEntity
import javax.persistence.*

@Entity
class Line(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    var name: String,
    var color: String
) : BaseEntity() {

    constructor(name: String, color: String): this(0,name, color) {
        this.name = name
        this.color = color
    }

    fun update(line: Line) {
        name = line.name
        color = line.color
    }
}
