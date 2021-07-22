package nextstep.subway.line

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
import nextstep.subway.line.dto.LineRequest
import nextstep.subway.line.dto.LineResponse
import org.assertj.core.api.AbstractSoftAssertions.assertAll
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest : AcceptanceTest() {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    fun createLine() {
        // given
        val 이호선 = LineRequest("2호선","초록색")

        // when
        // 지하철_노선_생성_요청
        val response: ExtractableResponse<Response> = 노선_생성_요청(이호선)

        // then
        // 지하철_노선_생성됨
        응답_확인(response, HttpStatus.CREATED.value())
        미디어타입_확인(response, MediaType.APPLICATION_JSON_VALUE)
        노선_데이터_확인(response,"초록색","2호선")
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    fun createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        노선_생성_요청(LineRequest("2호선","초록색"))

        // when
        // 지하철_노선_생성_요청
        val response = 노선_생성_요청(LineRequest("2호선","초록색"))

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
        노선_생성_요청(LineRequest("이호선","초록색"))
        노선_생성_요청(LineRequest("일호선","파란색"))
        노선_생성_요청(LineRequest("칠호선","연두색"))

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
        val createResponse = 노선_생성_요청(LineRequest("이호선","초록색"))
        노선_생성_요청(LineRequest("일호선","파란색"))

        // when
        // 지하철_노선_조회_요청
        val response = 노선_조회(createResponse)

        // then
        // 지하철_노선_응답됨
        응답_확인(response, HttpStatus.OK.value())
        미디어타입_확인(response, MediaType.APPLICATION_JSON_VALUE)
        노선_데이터_확인(response,"초록색","이호선")
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    fun updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        val createResponse = 노선_생성_요청(LineRequest("일호선","파란색"))
        val changeLine = LineRequest("이호선","초록색")
        // when
        // 지하철_노선_수정_요청
        changeLine(createResponse,changeLine)
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

        // when
        // 지하철_노선_제거_요청

        // then
        // 지하철_노선_삭제됨
    }

    private fun 노선_생성_요청(이호선: LineRequest) = RestAssured
        .given().log().all()
        .body(이호선)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .`when`()
        .post("/lines")
        .then().log().all().extract()

    private fun 노선_데이터_확인(response: ExtractableResponse<Response>, color: String, line: String) {
        assertThat(response.body().jsonPath().getString("color")).isEqualTo(color)
        assertThat(response.body().jsonPath().getString("name")).isEqualTo(line)
        assertThat(response.jsonPath().getString("createdDate")).isNotNull
        assertThat(response.jsonPath().getString("modifiedDate")).isNotNull
    }

    private fun 응답_확인(response: ExtractableResponse<Response>, httpStatus: Int) {
        assertThat(response.statusCode()).isEqualTo(httpStatus)
    }

    private fun 미디어타입_확인(response: ExtractableResponse<Response>, mediaType: String) {
        assertThat(response.header("Content-Type")).isEqualTo(mediaType)
    }

    private fun 노선_목록_검증(response: ExtractableResponse<Response>) {
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

    private fun headerLocation(createResponse: ExtractableResponse<Response>) =
        createResponse.header("Location")

    private fun 노선_조회(createResponse: ExtractableResponse<Response>) =
        RestAssured
            .given().log().all()
            .`when`()
            .get(headerLocation(createResponse))
            .then().log().all().extract()

    private fun 노선들_조회() = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .`when`()
        .get("/lines")
        .then().log().all().extract()

    private fun 노선_수정_확인(createResponse: ExtractableResponse<Response>, response: ExtractableResponse<Response>) {
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

    private fun changeLine(createResponse: ExtractableResponse<Response>, lineRequest: LineRequest) {
        RestAssured
            .given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .put(headerLocation(createResponse))
            .then().log().all().extract()
    }
}

