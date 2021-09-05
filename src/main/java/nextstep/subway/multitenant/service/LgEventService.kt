package nextstep.subway.multitenant.service

import com.funnc.shop.api.event.lguplus.dto.LgEventCouponRequest
import com.funnc.shop.api.event.lguplus.dto.LgEventCouponResponse
import nextstep.subway.multitenant.repositories.CompanyCouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LgEventService(
    private val companyCouponRepository: CompanyCouponRepository,
) {

    @Transactional(readOnly = false)
    fun registerLgCoupon(request: LgEventCouponRequest): LgEventCouponResponse {
        val companyDogCoupon = companyCouponRepository.findByCouponCode(request.couponCode)
        val companyCatCoupon = companyCouponRepository.findByCouponCode(request.couponCode)

        return LgEventCouponResponse()
    }
}
