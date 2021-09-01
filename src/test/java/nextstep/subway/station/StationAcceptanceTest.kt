package nextstep.subway.station

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
import nextstep.subway.station.dto.StationRequest
import nextstep.subway.station.dto.StationResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*
import java.util.stream.Collectors

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest : AcceptanceTest() {
    @DisplayName("지하철역을 생성한다.")
    @Test
    fun createStation() {
        // given, when
        val response = 지하철역_생성_요청(StationRequest("강남역"))

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(response.header("Location")).isNotBlank
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    fun createStationWithDuplicateName() {
        // given
        지하철역_생성_요청(StationRequest("강남역"))

        // when
        val response = 지하철역_생성_요청(StationRequest("강남역"))

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    @DisplayName("지하철역을 조회한다.")
    fun selectStations() {
        /// given
        val createResponse1 = 지하철역_생성_요청(StationRequest("강남역"))
        val createResponse2 = 지하철역_생성_요청(StationRequest("역삼역"))

        // when
        val response = RestAssured.given().log().all()
            .`when`()["/stations"]
            .then().log().all()
            .extract()

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
            .map {
                it.header("Location").split("/").toTypedArray()[2].toLong()
            }
            .collect(Collectors.toList())
        val resultLineIds = response.jsonPath().getList(".", StationResponse::class.java).stream()
            .map { it.id }
            .collect(Collectors.toList())
        Assertions.assertThat(resultLineIds).containsAll(expectedLineIds)
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    fun deleteStation() {
        // given
        val createResponse = 지하철역_생성_요청(StationRequest("강남역"))

        // when
        val uri = createResponse.header("Location")
        val response = RestAssured.given().log().all()
            .`when`()
            .delete(uri)
            .then().log().all()
            .extract()

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    companion object {
        fun 지하철역_생성_요청(request: StationRequest) =
            RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .`when`()
                .post("/stations")
                .then().log().all()
                .extract()
    }
}
