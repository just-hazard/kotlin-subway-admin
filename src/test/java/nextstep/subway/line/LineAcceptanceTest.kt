package nextstep.subway.line

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import nextstep.subway.station.StationAcceptanceTest
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.streams.toList

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest : AcceptanceTest() {

    private lateinit var 잠실역 : StationResponse
    private lateinit var 건대입구역 : StationResponse
    private lateinit var 종합운동장역 : StationResponse


    @BeforeEach
    fun setUp() {
        잠실역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("잠실역")).`as`(StationResponse::class.java)
        건대입구역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("건대입구역")).`as`(StationResponse::class.java)
        종합운동장역 = StationAcceptanceTest.지하철역_생성_요청(StationRequest("종합운동장역")).`as`(StationResponse::class.java)
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    fun createLine() {
        // given
        val 이호선 = LineRequest("2호선","초록색",잠실역.id, 건대입구역.id, 10)

        // when
        // 지하철_노선_생성_요청
        val response = 노선_생성_요청(이호선)

        // then
        // 지하철_노선_생성됨
        응답_확인(response, HttpStatus.CREATED.value())
        미디어타입_확인(response, MediaType.APPLICATION_JSON_VALUE)
        노선_데이터_확인(response,"초록색","2호선")
        노선_포함_지하철_확인(response, "잠실역", "건대입구역")
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    fun createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        노선_생성_요청(LineRequest("2호선","초록색",잠실역.id, 건대입구역.id, 10))

        // when
        // 지하철_노선_생성_요청
        val response = 노선_생성_요청(LineRequest("2호선","초록색",잠실역.id, 건대입구역.id, 10))

        // then
        // 지하철_노선_생성_실패됨
        응답_확인(response, HttpStatus.CONFLICT.value())
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    fun lines(){
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        노선_생성_요청(LineRequest("이호선","초록색",잠실역.id, 건대입구역.id, 10))
        노선_생성_요청(LineRequest("일호선","파란색",잠실역.id, 건대입구역.id, 10))
        노선_생성_요청(LineRequest("칠호선","연두색",잠실역.id, 건대입구역.id, 10))

        // when
        // 지하철_노선_목록_조회_요청
        val response = 노선들_조회()

        // then
        // 지하철_노선_목록_응답됨
        응답_확인(response, HttpStatus.OK.value())
        // 지하철_노선_목록_포함됨
        노선_목록_검증(response)
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    fun findLine() {
        // given
        // 지하철_노선_등록되어_있음
        val createResponse = 노선_생성_요청(LineRequest("이호선","초록색",잠실역.id, 건대입구역.id, 10))
        노선_생성_요청(LineRequest("일호선","파란색",잠실역.id, 건대입구역.id, 10))

        // when
        // 지하철_노선_조회_요청
        val response = 노선_조회(createResponse)

        // then
        // 지하철_노선_응답됨
        응답_확인(response, HttpStatus.OK.value())
        미디어타입_확인(response, MediaType.APPLICATION_JSON_VALUE)
        노선_데이터_확인(response,"초록색","이호선")
        노선_포함_지하철_확인(response, "잠실역", "건대입구역")
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    fun updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        val createResponse = 노선_생성_요청(LineRequest("일호선","파란색",잠실역.id, 건대입구역.id, 10))
        val changeLine = LineRequest("이호선","초록색",잠실역.id, 건대입구역.id, 10)

        // when
        // 지하철_노선_수정_요청
        지하철_노선_수정(createResponse,changeLine)
        val response = 노선_조회(createResponse)

        // then
        // 지하철_노선_수정됨
        응답_확인(response, HttpStatus.OK.value())
        미디어타입_확인(response, MediaType.APPLICATION_JSON_VALUE)
        노선_수정_확인(createResponse,response)

    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    fun deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        val createResponse = 노선_생성_요청(LineRequest("이호선","초록색",잠실역.id, 건대입구역.id, 10))
        // when
        // 지하철_노선_제거_요청
        val response = deleteLine(createResponse)

        // then
        // 지하철_노선_삭제됨
        응답_확인(response, HttpStatus.NO_CONTENT.value())
    }

    companion object {
        fun 노선_포함_지하철_확인(response: ExtractableResponse<Response>, vararg expectedStations: String) {
            val stations = response.jsonPath().getList("stations",
                StationResponse::class.java)
                .stream().map{it.name}.toList()
            assertThat(stations).containsExactly(*expectedStations)
        }

        fun deleteLine(createResponse: ExtractableResponse<Response>) =
            RestAssured
                .given().log().all()
                .`when`()
                .delete(headerLocation(createResponse))
                .then().log().all().extract()

        fun 노선_생성_요청(이호선: LineRequest) = RestAssured
            .given().log().all()
            .body(이호선)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/lines")
            .then().log().all().extract()

        fun 노선_데이터_확인(response: ExtractableResponse<Response>, color: String, line: String) {

            assertThat(response.body().jsonPath().getString("color")).isEqualTo(color)
            assertThat(response.body().jsonPath().getString("name")).isEqualTo(line)
            assertThat(response.jsonPath().getString("createdDate")).isNotNull
            assertThat(response.jsonPath().getString("modifiedDate")).isNotNull
        }

        fun 응답_확인(response: ExtractableResponse<Response>, httpStatus: Int) {
            assertThat(response.statusCode()).isEqualTo(httpStatus)
        }

        fun 미디어타입_확인(response: ExtractableResponse<Response>, mediaType: String) {
            assertThat(response.header("Content-Type")).isEqualTo(mediaType)
        }

        fun 노선_목록_검증(response: ExtractableResponse<Response>) {
            response.jsonPath().getList(".",LineResponse::class.java)
                .forEach {
                    MatcherAssert.assertThat(it.id,
                        CoreMatchers.anyOf(CoreMatchers.equalTo(1L), CoreMatchers.equalTo(2L),CoreMatchers.equalTo(3L)))
                    MatcherAssert.assertThat(it.name,
                        CoreMatchers.anyOf(CoreMatchers.containsString("일호선"),CoreMatchers.containsString("이호선"),CoreMatchers.containsString("칠호선")))
                    MatcherAssert.assertThat(it.color,
                        CoreMatchers.anyOf(CoreMatchers.containsString("초록색"),CoreMatchers.containsString("파란색"),CoreMatchers.containsString("연두색")))
                    assertThat(it.createdDate).isNotNull
                    assertThat(it.modifiedDate).isNotNull
                }
        }

        fun headerLocation(createResponse: ExtractableResponse<Response>) =
            createResponse.header("Location")

        fun 노선_조회(createResponse: ExtractableResponse<Response>) =
            RestAssured
                .given().log().all()
                .`when`()
                .get(headerLocation(createResponse))
                .then().log().all().extract()

        fun 노선들_조회() = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .get("/lines")
            .then().log().all().extract()

        fun 노선_수정_확인(createResponse: ExtractableResponse<Response>, response: ExtractableResponse<Response>) {
            assertAll(
                Executable {
                    assertThat(response.jsonPath().getLong("id"))
                        .isEqualTo(createResponse.jsonPath().getString("id").toLong())
                },
                Executable {
                    assertThat(response.jsonPath().getString("name"))
                        .isNotEqualTo(createResponse.jsonPath().getString("name"))
                },
                Executable {
                    assertThat(response.jsonPath().getString("color"))
                        .isNotEqualTo(createResponse.jsonPath().getString("color"))
                },
                Executable {
                    assertThat(response.jsonPath().getString("createdDate")).isNotNull()
                },
                Executable {
                    assertThat(response.jsonPath().getString("modifiedDate"))
                        .isNotEqualTo(createResponse.jsonPath().getString("modifiedDate"))
                }
            )
        }

        fun 지하철_노선_수정(createResponse: ExtractableResponse<Response>, lineRequest: LineRequest) {
            RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .`when`()
                .put(headerLocation(createResponse))
                .then().log().all().extract()
        }
    }
}

