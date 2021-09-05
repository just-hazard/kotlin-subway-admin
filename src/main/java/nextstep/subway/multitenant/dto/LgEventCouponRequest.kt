package com.funnc.shop.api.event.lguplus.dto

data class LgEventCouponRequest(
    val domainType: String,
    val couponCode: String,
    val companyCode: String)

