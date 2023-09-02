package id.gdev.regist.data.source.remote.collection

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import id.gdev.regist.domain.model.Participant

data class ParticipantCollection(
    var header: String? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var fullData: Map<String, String>? = null,
    var partnership: Boolean? = false,
    var checkIn: Boolean? = false,
    var lastCheckInTime: Timestamp? = Timestamp.now(),

    @DocumentId
    var id: String? = null
)

fun ParticipantCollection.toParticipant() = Participant(
    this.id ?: "",
    this.header ?: "",
    this.title ?: "",
    this.subtitle ?: "",
    this.fullData ?: mapOf(),
    this.partnership ?: false,
    this.checkIn ?: false,
    this.lastCheckInTime
)

