package nextstep.subway.line.application

import nextstep.subway.line.domain.Line
import nextstep.subway.line.domain.LineRepository
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import nextstep.subway.station.domain.StationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors.toList
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class LineService(private val lineRepository: LineRepository,
        private val stationRepository: StationRepository) {

    fun saveLine(request: LineRequest): LineResponse {
        val upStation = stationRepository.findById(request.upStationId).orElseThrow { EntityNotFoundException("상행역이 존재하지 않습니다.") }
        val downStation = stationRepository.findById(request.downStationId).orElseThrow { EntityNotFoundException("하행역이 존재하지 않습니다.") }

        return LineResponse.of(lineRepository.save(Line.of(request.name, request.color, upStation!!, downStation!!, request.distance)))
    }

    @Transactional(readOnly = true)
    fun findAll(): List<LineResponse> {
        return lineRepository.findAll().stream()
            .map { LineResponse.of(it!!) }.collect(toList())
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): LineResponse {
        return LineResponse.of(findLine(id))
    }

    fun changeLine(id: Long, request: LineRequest): LineResponse {
        val line = findLine(id)
        line.update(request.toLine())
        return LineResponse.of(line)
    }

    private fun findLine(id: Long) =
        lineRepository.findById(id).orElseThrow { EntityNotFoundException("존재하지 않는 노선입니다.") }!!

    fun deleteLine(id: Long) {
        lineRepository.deleteById(id)
    }
}
