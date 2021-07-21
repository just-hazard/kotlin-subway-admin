package nextstep.subway.line.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Line(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    var name: String,
    var color: String,
    // orphanRemoval 고아객체 자동 삭제
    @OneToMany(mappedBy = "line", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var stations: MutableList<Station>
) : BaseEntity() {

    constructor(name: String, color: String): this(0,name, color, mutableListOf()) {
        this.name = name
        this.color = color
    }

    fun update(line: Line) {
        name = line.name
        color = line.color
    }
}
