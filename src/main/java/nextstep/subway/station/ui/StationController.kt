package nextstep.subway.station.ui

import nextstep.subway.station.application.StationService
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class StationController(private val stationService: StationService) {
    @PostMapping("/stations")
    fun createStation(@RequestBody stationRequest: StationRequest?): ResponseEntity<StationResponse> {
        val station = stationService.saveStation(stationRequest!!)
        return ResponseEntity.created(URI.create("/stations/" + station.id)).body(station)
    }

    @GetMapping(value = ["/stations"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun showStations(): ResponseEntity<List<StationResponse>> {
        return ResponseEntity.ok().body(stationService.findAllStations())
    }

    @DeleteMapping("/stations/{id}")
    fun deleteStation(@PathVariable id: Long?): ResponseEntity<*> {
        stationService.deleteStationById(id!!)
        return ResponseEntity.noContent().build<Any>()
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleIllegalArgsException(e: DataIntegrityViolationException?): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }
}
