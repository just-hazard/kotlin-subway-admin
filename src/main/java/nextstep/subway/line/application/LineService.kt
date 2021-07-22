package nextstep.subway.line.application

import nextstep.subway.line.domain.LineRepository
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors.toList

@Service
@Transactional
class LineService(private val lineRepository: LineRepository) {
    fun saveLine(request: LineRequest): LineResponse {
        return LineResponse.of(lineRepository.save(request.toLine()))
    }

    @Transactional(readOnly = true)
    fun findAll(): List<LineResponse> {
        return lineRepository.findAll().stream()
            .map { LineResponse.of(it!!) }.collect(toList())
    }
}
