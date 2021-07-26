package nextstep.subway.section.domain

import nextstep.subway.common.BaseEntity
import nextstep.subway.line.domain.Line
import nextstep.subway.station.domain.Station
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Section (
    @GeneratedValue
    @Id
    val id: Long,
    @ManyToOne
    val line: Line,
    @ManyToOne
    val upstation: Station,
    @ManyToOne
    val downStation: Station
) : BaseEntity()
