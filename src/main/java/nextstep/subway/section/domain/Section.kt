package nextstep.subway.section.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Section (
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long,
    @ManyToOne
    val line: Line,
    @ManyToOne
    val upstation: Station,
    @ManyToOne
    val downStation: Station,

    val distance: Int
) : BaseEntity()
