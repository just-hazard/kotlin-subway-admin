package nextstep.subway.station

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import nextstep.subway.AcceptanceTest
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
        // given
        val params: MutableMap<String, String> = HashMap()
        params["name"] = "강남역"

        // when
        val response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/stations")
            .then().log().all()
            .extract()

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(response.header("Location")).isNotBlank
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    fun createStationWithDuplicateName() {
        // given
        val params: MutableMap<String, String> = HashMap()
        params["name"] = "강남역"
        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/stations")
            .then().log().all()
            .extract()

        // when
        val response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/stations")
            .then()
            .log().all()
            .extract()

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    /// given
    @get:Test
    @get:DisplayName("지하철역을 조회한다.")
    val stations: Unit

    // when

        // then
        get() {
            /// given
            val params1: MutableMap<String, String> = HashMap()
            params1["name"] = "강남역"
            val createResponse1 = RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .`when`()
                .post("/stations")
                .then().log().all()
                .extract()
            val params2: MutableMap<String, String> = HashMap()
            params2["name"] = "역삼역"
            val createResponse2 = RestAssured.given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .`when`()
                .post("/stations")
                .then().log().all()
                .extract()

            // when
            val response = RestAssured.given().log().all()
                .`when`()["/stations"]
                .then().log().all()
                .extract()

            // then
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
            val expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map { it: ExtractableResponse<Response?> ->
                    it.header("Location").split("/").toTypedArray()[2].toLong()
                }
                .collect(Collectors.toList())
            val resultLineIds = response.jsonPath().getList(".", StationResponse::class.java).stream()
                .map { it: StationResponse -> it.id }
                .collect(Collectors.toList())
            Assertions.assertThat(resultLineIds).containsAll(expectedLineIds)
        }

    @DisplayName("지하철역을 제거한다.")
    @Test
    fun deleteStation() {
        // given
        val params: MutableMap<String, String> = HashMap()
        params["name"] = "강남역"
        val createResponse = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post("/stations")
            .then().log().all()
            .extract()

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
}
