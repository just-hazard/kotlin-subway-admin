package nextstep.subway.multitenant.service

import com.funnc.shop.api.event.lguplus.dto.LgEventCouponRequest
import com.funnc.shop.api.event.lguplus.dto.LgEventCouponResponse
import nextstep.subway.multitenant.TenantContext
import nextstep.subway.multitenant.repositories.CompanyCouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LgEventService(
    private val companyCouponRepository: CompanyCouponRepository,
    private val tenantContext: TenantContext
) {

    @Transactional(readOnly = false)
    fun registerLgCoupon(request: LgEventCouponRequest): LgEventCouponResponse {
        tenantContext.setTenant("dogpre")
        val companyDogCoupon = companyCouponRepository.findByCouponCode(request.couponCode)
        tenantContext.setTenant("catpre")
        val companyCatCoupon = companyCouponRepository.findByCouponCode(request.couponCode)

        return LgEventCouponResponse()
    }
}
