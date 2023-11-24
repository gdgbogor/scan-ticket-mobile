package id.gdev.regist.domain.model

import com.google.firebase.Timestamp
import id.gdev.regist.data.source.remote.collection.OptionalCheckInCollection
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.data.source.remote.collection.toParticipant

data class Participant(
    var id: String,
    var header: String,
    var title: String,
    var subtitle: String,
    var fullData: Map<String, String>,
    var partnership: Boolean? = false,
    var checkIn: Boolean? = false,
    var ticketTypeKey: String? = null,
    var optionalCheckIn: List<OptionalCheckIn> = listOf(),
    var lastCheckInTime: Timestamp? = Timestamp.now()
) {
    companion object {
        val INIT get() = ParticipantCollection().toParticipant()
    }
}

data class OptionalCheckIn(
    var id: Int? = null, var label: String, var checkIn: Boolean? = false
)

fun OptionalCheckIn.toCollection() = OptionalCheckInCollection(
    label, checkIn
)

fun Participant.toCollection() = ParticipantCollection(this.header,
    this.title,
    this.subtitle,
    this.fullData,
    optionalCheckIn = optionalCheckIn.map { it.toCollection() })
