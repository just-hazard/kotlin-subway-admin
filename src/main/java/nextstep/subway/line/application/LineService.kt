package nextstep.subway.line.application

import nextstep.subway.line.domain.LineRepository
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LineService(private val lineRepository: LineRepository) {
    fun saveLine(request: LineRequest): LineResponse {
        val persistLine = lineRepository.save(request.toLine())
        return LineResponse.of(persistLine)
    }
}
