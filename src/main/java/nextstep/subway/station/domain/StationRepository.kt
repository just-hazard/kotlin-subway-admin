package nextstep.subway.station.domain

import org.springframework.data.jpa.repository.JpaRepository
import nextstep.subway.station.domain.Station

interface StationRepository : JpaRepository<Station?, Long?> {
    override fun findAll(): List<Station?>
}
