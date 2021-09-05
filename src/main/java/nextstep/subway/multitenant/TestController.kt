package nextstep.subway.multitenant

import com.funnc.shop.api.event.lguplus.dto.LgEventCouponRequest
import com.funnc.shop.api.event.lguplus.dto.LgEventCouponResponse
import nextstep.subway.multitenant.service.LgEventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    val lgEventService: LgEventService
) {

    @PostMapping("/v1/lg/event")
    fun registerLgCoupon(@RequestBody request: LgEventCouponRequest) : ResponseEntity<LgEventCouponResponse> {
        TenantContext.getCurrentTenant()
        return ResponseEntity.ok(lgEventService.registerLgCoupon(request))
    }
}
