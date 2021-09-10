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
        // 시나리오
        // request 코드를 통해 존재하는 코드인지 확인 ( 아닐경우 예외 )
        // 쿠폰 유효기간이 지났는지 확인 ( 예외 )
        // 사용된 쿠폰인지 확인 ( 예외 )
        // 쿠폰 등록
        val companyDogCoupon = companyCouponRepository.findByCouponCode(request.couponCode)
        val companyCatCoupon = companyCouponRepository.findByCouponCode(request.couponCode)

        return LgEventCouponResponse()
    }
}
