package nextstep.subway.line.ui

import nextstep.subway.line.application.LineService
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/lines")
class LineController(private val lineService: LineService) {
    @PostMapping
    fun createLine(@RequestBody lineRequest: LineRequest): ResponseEntity<LineResponse> {
        val line = lineService.saveLine(lineRequest)
        return ResponseEntity.created(URI.create("/lines/" + line.id)).body(line)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findLines() : ResponseEntity<List<LineResponse>> {
        return ResponseEntity.ok().body(lineService.findAll())
    }


    @GetMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findLine(@PathVariable id: Long) : ResponseEntity<LineResponse> {
        return ResponseEntity.ok().body(lineService.findById(id))
    }

    @PutMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun changeLine(@PathVariable id: Long, @RequestBody request: LineRequest) : ResponseEntity<LineResponse> {
        return ResponseEntity.ok().body(lineService.changeLine(id, request))
    }

    @DeleteMapping(value = ["{id}"])
    fun deleteLine(@PathVariable id: Long) : ResponseEntity<*> {
        lineService.deleteLine(id)
        return ResponseEntity.noContent().build<Any>()
    }
}
