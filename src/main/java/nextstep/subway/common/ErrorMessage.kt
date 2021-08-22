package nextstep.subway.common

class ErrorMessage {
    companion object {
        const val NON_EXISTENT_LINE = "해당 노선이 존재하지 않습니다."
        const val DISTANCE_OVER = "기존 거리보다 더 멀 수 없습니다."
        const val NON_EXISTENT_SECTION = "존재하지 않는 상하행역입니다."
        const val EXISTENT_SECTION = "이미 존재하는 상하행역입니다."
        const val CAN_NOT_REMOVE_SECTION = "존재하는 상하행역이 하나뿐이라 삭제할 수 없습니다."
        const val NON_EXISTENT_STATION = "해당 지하철역이 존재하지 않습니다."
    }
}
