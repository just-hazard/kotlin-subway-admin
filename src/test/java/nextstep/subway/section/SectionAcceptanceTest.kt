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
import nextstep.subway.station.domain.Station
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
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

    private lateinit var 건대입구역 : StationResponse
    private lateinit var 잠실역 : StationResponse
    private lateinit var 종합운동장역 : StationResponse
    private lateinit var 역삼역 : StationResponse
    private lateinit var 강남역 : StationResponse
    private lateinit var 서울대입구역 : StationResponse

    @BeforeEach
    fun setUp() {

        건대입구역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("건대입구역")).`as`(StationResponse::class.java)
        잠실역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("잠실역")).`as`(StationResponse::class.java)
        종합운동장역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("종합운동장역")).`as`(StationResponse::class.java)
        역삼역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("역삼역")).`as`(StationResponse::class.java)
        강남역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("강남역")).`as`(StationResponse::class.java)
        서울대입구역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("서울대입구역")).`as`(StationResponse::class.java)

        이호선 = LineAcceptanceTest.노선_생성_요청(LineRequest("이호선","초록색",건대입구역.id, 종합운동장역.id, 10)).`as`(LineResponse::class.java)
    }

    @Test
    fun `역과 역 사이에 새로운 역을 등록 (상행역)`() {

        // given when
        지하철_구간_등록_요청(이호선.id, SectionRequest(종합운동장역.id, 역삼역.id, 5))
        지하철_구간_등록_요청(이호선.id, SectionRequest(역삼역.id, 강남역.id, 5))
        지하철_구간_등록_요청(이호선.id, SectionRequest(강남역.id, 서울대입구역.id, 5))
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(잠실역.id, 종합운동장역.id, 5))

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("건대입구역", "잠실역", "종합운동장역", "역삼역", "강남역", "서울대입구역"))
    }

    @Test
    fun `역과 역 사이에 새로운 역을 등록 (하행역)`() {

        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(건대입구역.id, 잠실역.id, 5))

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("건대입구역", "잠실역", "종합운동장역"))
    }

    @Test
    fun `새로운 상행역 종점 등록`() {
        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(잠실역.id, 건대입구역.id, 5))

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("잠실역", "건대입구역", "종합운동장역"))
    }

    @Test
    fun `새로운 하행역 종점 등록`() {
        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(종합운동장역.id, 잠실역.id, 5))

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
        노선에_포함된_지하철_확인(response, listOf("건대입구역", "종합운동장역", "잠실역"))
    }

    @Test
    fun `기존 상하행역 길이보다 크거나 같을 경우 (예외처리)`() {
        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(건대입구역.id, 잠실역.id, 15))

        // then
        노선에_구간_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `이미 존재하는 상하행역 등록 (예외처리)`() {
        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(건대입구역.id, 종합운동장역.id, 5))

        // then
        노선에_구간_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `노선에 존재하지 않는 상하행역 (예외처리)`() {

        // given when
        val response = 지하철_구간_등록_요청(이호선.id, SectionRequest(역삼역.id, 강남역.id, 10))

        // then
        노선에_구간_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `구간 삭제`() {

        // given
        지하철_구간_등록_요청(이호선.id, SectionRequest(건대입구역.id, 잠실역.id, 5))

        // when
        val response = 구간_삭제_요청(이호선.id, 잠실역.id)

        // then
        노선에_구간_요청_확인(response, HttpStatus.OK)
    }

    @Test
    fun `구간 1개일 때 삭제요청 시 예외처리`() {

        // given when
        val response = 구간_삭제_요청(이호선.id, 건대입구역.id)

        // then
        노선에_구간_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR)
        // then
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

        private fun 구간_삭제_요청(lineId: Long, stationId: Long): ExtractableResponse<Response>  {
            return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .`when`()
                .delete("/lines/$lineId/sections?stationId=$stationId")
                .then().log().all().extract()
        }

        private fun 노선에_구간_요청_확인(response: ExtractableResponse<Response>, httpStatus: HttpStatus) {
            assertThat(response.statusCode()).isEqualTo(httpStatus.value())
        }

        private fun 노선에_포함된_지하철_확인(response: ExtractableResponse<Response>, expectedStations: List<String>) {
            val stations = response.jsonPath().getList("stations", StationResponse::class.java)
                .stream().map {
                    it.name
                }.toList()

            assertThat(expectedStations).isEqualTo(stations)
        }
    }
}
