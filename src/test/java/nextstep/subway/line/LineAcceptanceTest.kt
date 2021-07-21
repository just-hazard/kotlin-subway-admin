package nextstep.subway.line

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
import nextstep.subway.line.dto.LineRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
        Http코드값_미디어타입_확인(response, HttpStatus.CREATED.value(),MediaType.APPLICATION_JSON_VALUE)
        노선_데이터_확인(response,"초록색","2호선")
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    fun createLine2() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_생성_요청

        // then
        // 지하철_노선_생성_실패됨
    }

    // when

    // then
    // 지하철_노선_목록_응답됨
    // 지하철_노선_목록_포함됨
    @get:Test
    @get:DisplayName("지하철 노선 목록을 조회한다.")
    val lines: Unit
        get() {
            // given
            // 지하철_노선_등록되어_있음
            // 지하철_노선_등록되어_있음

            // when
            // 지하철_노선_목록_조회_요청

            // then
            // 지하철_노선_목록_응답됨
            // 지하철_노선_목록_포함됨
        }

    // when

    // then
    // 지하철_노선_응답됨
    @get:Test
    @get:DisplayName("지하철 노선을 조회한다.")
    val line: Unit
        get() {
            // given
            // 지하철_노선_등록되어_있음

            // when
            // 지하철_노선_조회_요청

            // then
            // 지하철_노선_응답됨
        }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    fun updateLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_수정_요청

        // then
        // 지하철_노선_수정됨
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

    private fun Http코드값_미디어타입_확인(response: ExtractableResponse<Response>, httpStatus: Int, mediaType: String) {
        assertThat(response.statusCode()).isEqualTo(httpStatus)
        assertThat(response.header("Content-Type")).isEqualTo(mediaType)
    }
}
