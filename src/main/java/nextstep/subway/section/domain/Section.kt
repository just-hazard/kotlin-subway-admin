package nextstep.subway.section.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.*

@Entity
class Section (
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long,
    @ManyToOne
    var line: Line,
    @ManyToOne
    var upStation: Station,
    @ManyToOne
    var downStation: Station,

    val distance: Int
) : BaseEntity()
