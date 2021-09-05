package nextstep.subway.multitenant.repositories

import nextstep.subway.multitenant.domain.CompanyCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyCouponRepository : JpaRepository<CompanyCoupon, Long> {
    fun findByCouponCode(couponCode: String) : CompanyCoupon
}
