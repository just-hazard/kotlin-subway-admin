package nextstep.subway.multitenant

import com.funnc.shop.api.event.lguplus.dto.LgEventCouponRequest
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("LGU+ 관련 이벤트")
class LGUPlusEventAcceptanceTest {

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
        }
    }

    @Test
    fun `이벤트 쿠폰 등록`() {
        // 시나리오
        // 이벤트 쿠폰 request가 넘어옴
        // 해당 값을 통해 (로그인 회원, 이미 등록된 코드인지, 존재하는 쿠폰인지, 유예기간에 해당하는지
        val request = LgEventCouponRequest("강아지","htbdHMFP","001")

        val response = RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/v1/lg/event")
            .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        // given
        // when
        // then
    }

//    private fun requestHeader() : Map<String, String> {
//        return mapOf(
//            ("X-Client-Id" to TestAuthRequestContext.DEV_CLIENT_ID),
//            ("X-Client-Secret" to TestAuthRequestContext.DEV_CLIENT_SECRET),
//            ("X-Client-User-Agent" to TestAuthRequestContext.DEV_CLIENT_USER_AGENT),
//            ("X-Client-User-Ip-Address" to TestAuthRequestContext.getIpAddress()))
//    }
}
