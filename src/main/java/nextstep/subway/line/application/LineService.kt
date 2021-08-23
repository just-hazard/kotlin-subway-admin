package nextstep.subway.line.application

import nextstep.subway.common.ErrorMessage
import nextstep.subway.line.domain.Line
import nextstep.subway.line.domain.LineRepository
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import nextstep.subway.section.domain.Distance
import nextstep.subway.section.domain.Section
import nextstep.subway.section.dto.SectionRequest
import nextstep.subway.station.domain.StationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors.toList
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class LineService(private val lineRepository: LineRepository,
        private val stationRepository: StationRepository) {

    fun saveLine(request: LineRequest): LineResponse {
        val upStation = findStation(request.upStationId, ErrorMessage.NON_EXISTENT_UP_STATION)
        val downStation = findStation(request.downStationId, ErrorMessage.NON_EXISTENT_DOWN_STATION)

        return LineResponse.from(lineRepository.save(Line.of(request.name, request.color,
            upStation, downStation, Distance(request.distance))))
    }

    fun findAll(): List<LineResponse> {
        return lineRepository.findAll().stream()
            .map { LineResponse.from(it!!) }.collect(toList())
    }

    fun findById(id: Long): LineResponse {
        return LineResponse.from(findLine(id))
    }
    @Transactional(readOnly = false)
    fun changeLine(id: Long, request: LineRequest): LineResponse {
        val line = findLine(id)
        line.update(request.toLine())
        return LineResponse.from(line)
    }
    @Transactional(readOnly = false)
    fun deleteLine(id: Long) {
        lineRepository.deleteById(id)
    }
    @Transactional(readOnly = false)
    fun saveSection(id: Long, reqeust: SectionRequest) : LineResponse {
        val line = findLine(id)
        val upStation = findStation(id, ErrorMessage.NON_EXISTENT_STATION)
        val downStation = findStation(id, ErrorMessage.NON_EXISTENT_STATION)

        line.addSection(Section(0,line, upStation, downStation, Distance(reqeust.distance)))

        return LineResponse.from(line)
    }

    @Transactional(readOnly = false)
    fun deleteSection(lineId: Long, stationId: Long): LineResponse {
        val line = findLine(lineId)
        val station = findStation(stationId, ErrorMessage.NON_EXISTENT_STATION)

        line.removeSectionMessage(station)

        return LineResponse.from(line)
    }

    private fun findStation(stationId: Long, errorMessage: String) =
        stationRepository.findById(stationId).orElseThrow { EntityNotFoundException(errorMessage) }!!

    private fun findLine(id: Long) =
        lineRepository.findById(id).orElseThrow { EntityNotFoundException(ErrorMessage.NON_EXISTENT_LINE) }!!
}
