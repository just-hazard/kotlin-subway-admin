package nextstep.subway.station.application

import nextstep.subway.station.domain.StationRepository
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import nextstep.subway.station.domain.Station
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional
class StationService(private val stationRepository: StationRepository) {
    fun saveStation(stationRequest: StationRequest): StationResponse {
        val persistStation = stationRepository.save(stationRequest.toStation())
        return StationResponse.of(persistStation)
    }

    @Transactional(readOnly = true)
    fun findAllStations(): List<StationResponse> {
        val stations = stationRepository.findAll()
        return stations.stream()
            .map { station: Station? -> station?.let { StationResponse.of(it) } }
            .collect(Collectors.toList())
    }

    fun deleteStationById(id: Long) {
        stationRepository.deleteById(id)
    }
}
