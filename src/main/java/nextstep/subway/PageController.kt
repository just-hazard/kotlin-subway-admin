package nextstep.subway

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {
    @GetMapping(value = ["/", "/stations", "/lines", "/sections"], produces = [MediaType.TEXT_HTML_VALUE])
    fun index(): String {
        return "index"
    }
}
