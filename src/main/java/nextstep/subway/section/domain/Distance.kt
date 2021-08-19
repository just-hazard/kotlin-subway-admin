package nextstep.subway.section.domain

import javax.persistence.Embeddable

@Embeddable
data class Distance(var distance: Int) {

    companion object {
        private const val DISTANCE_ZERO = 0
    }

    fun distanceCorrection(newSectionDistance: Distance) {
        this.distance -= newSectionDistance.distance
    }

    fun validConfirmNewDistance(newSectionDistance: Distance): Boolean {
        return this.distance <= newSectionDistance.distance
    }
    fun validConfirmGreaterThanZero() : Boolean {
        return this.distance > DISTANCE_ZERO
    }

    fun plusDistance(distance: Distance) {
        this.distance += distance.distance
    }
}
