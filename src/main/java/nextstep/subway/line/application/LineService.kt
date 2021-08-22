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
        val upStation = stationRepository.findById(request.upStationId).orElseThrow { EntityNotFoundException("상행역이 존재하지 않습니다.") }
        val downStation = stationRepository.findById(request.downStationId).orElseThrow { EntityNotFoundException("하행역이 존재하지 않습니다.") }

        return LineResponse.from(lineRepository.save(Line.of(request.name, request.color, upStation!!, downStation!!, Distance(request.distance))))
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
        val upStation = findStation(reqeust.upStationId)
        val downStation = findStation(reqeust.downStationId)

        line.addSection(Section(0,line, upStation, downStation, Distance(reqeust.distance)))

        return LineResponse.from(line)
    }

    @Transactional(readOnly = false)
    fun deleteSection(lineId: Long, stationId: Long): LineResponse {
        val line = findLine(lineId)
        val station = findStation(stationId)

        line.removeSectionMessage(station)

        return LineResponse.from(line)
    }

    private fun findStation(stationId: Long) =
        stationRepository.findById(stationId).orElseThrow { EntityNotFoundException(ErrorMessage.NON_EXISTENT_STATION) }!!

    private fun findLine(id: Long) =
        lineRepository.findById(id).orElseThrow { EntityNotFoundException(ErrorMessage.NON_EXISTENT_LINE) }!!
}
