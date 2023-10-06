package id.gdev.regist.data.source.remote.collection

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import id.gdev.regist.domain.model.OptionalCheckIn
import id.gdev.regist.domain.model.Participant

data class ParticipantCollection(
    var header: String? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var fullData: Map<String, String>? = null,
    var partnership: Boolean? = false,
    var checkIn: Boolean? = false,
    var optionalCheckIn: List<OptionalCheckInCollection>? = null,
    var lastCheckInTime: Timestamp? = Timestamp.now(),

    @DocumentId
    var id: String? = null
)

data class OptionalCheckInCollection(
    var label: String? = null,
    val checkIn: Boolean? = false
)

fun OptionalCheckInCollection.toData() = OptionalCheckIn(
    label = this.label ?: "",
    checkIn = this.checkIn ?: false,
)

fun ParticipantCollection.toParticipant() = Participant(
    this.id ?: "",
    this.header ?: "",
    this.title ?: "",
    this.subtitle ?: "",
    this.fullData ?: mapOf(),
    this.partnership ?: false,
    this.checkIn ?: false,
    this.optionalCheckIn?.map { it.toData() } ?: listOf(),
    this.lastCheckInTime
)

