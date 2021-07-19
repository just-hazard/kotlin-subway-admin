package nextstep.subway.line.ui

import nextstep.subway.line.application.LineService
import nextstep.subway.line.dto.LineRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/lines")
class LineController(private val lineService: LineService) {
    @PostMapping
    fun createLine(@RequestBody lineRequest: LineRequest?): ResponseEntity<*> {
        val line = lineService.saveLine(lineRequest!!)
        return ResponseEntity.created(URI.create("/lines/" + line.id)).body(line)
    }
}
