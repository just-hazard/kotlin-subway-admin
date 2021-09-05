package nextstep.subway.multitenant.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "fnc_company_coupon")
class CompanyCoupon(

    /**
     * PK
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    /**
     * gd_discount_coupon_issue
     *
     * FK, 회원 쿠폰 발급 정보
     */
    @Column(name = "coupon_no")
    val couponNo: Int,

    /**
     * 외부에 발행된 쿠폰 번호
     */
    @Column(name = "coupon_code")
    val couponCode: String,

    /**
     * 회사 구분 코드
     */
    @Column(name = "company_code")
    val companyCode: String,

    /**
     * 생성 날짜
     */
    @Column(name = "created_at")
    val createdAt : LocalDateTime
)
