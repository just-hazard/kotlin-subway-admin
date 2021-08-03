package nextstep.subway.section

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
import nextstep.subway.line.LineAcceptanceTest
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import nextstep.subway.section.dto.SectionRequest
import nextstep.subway.section.dto.SectionResponse
import nextstep.subway.station.StationAcceptanceTest
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest : AcceptanceTest() {

    private lateinit var 이호선 : LineResponse

    private lateinit var 잠실역 : StationResponse
    private lateinit var 건대입구역 : StationResponse
    private lateinit var 종합운동장역 : StationResponse

    @BeforeEach
    fun setUp() {

        잠실역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("잠실역")).`as`(StationResponse::class.java)
        건대입구역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("건대입구역")).`as`(StationResponse::class.java)
        종합운동장역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("종합운동장역")).`as`(StationResponse::class.java)

        이호선 = LineAcceptanceTest.노선_생성_요청(LineRequest("이호선","초록색",잠실역.id, 건대입구역.id, 10)).`as`(LineResponse::class.java)
    }

    @Test
    fun `역과 역 사이에 새로운 역을 등록 (상행역)`() {

        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(종합운동장역.id, 건대입구역.id, 5));

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("잠실역", "종합운동장역", "건대입구역"))
    }

    @Test
    fun `역과 역 사이에 새로운 역을 등록 (하행역)`() {

        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(잠실역.id, 종합운동장역.id, 5));

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("잠실역", "종합운동장역", "건대입구역"))
    }

    companion object {
        private fun 지하철_구간_등록_요청(lineId: Long, request: SectionRequest): ExtractableResponse<Response> {
                    return RestAssured
                        .given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .`when`()
                        .post("/lines/$lineId/sections")
                        .then().log().all().extract()
        }

        private fun 노선에_구간_요청_확인(response: ExtractableResponse<Response>, httpStatus: HttpStatus) {
            Assertions.assertThat(response.statusCode()).isEqualTo(httpStatus)
        }
        private fun 노선에_포함된_지하철_확인(response: ExtractableResponse<Response>, expectedStations: List<String>) {
            val stations = response.jsonPath().getList("stations", SectionResponse::class.java)
                .stream().map {
                    it.name
                }.toList()
            Assertions.assertThat(stations).containsExactly(expectedStations.toString())
        }
    }
}
