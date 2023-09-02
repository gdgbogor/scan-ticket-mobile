package id.gdev.regist.data.source.remote.collection

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import id.gdev.regist.domain.model.Event

data class EventCollection(
    val name: String? = null,
    val location: String? = null,
    val time: Timestamp? = Timestamp.now(),
    val totalParticipant: Int? = 0,
    val checkInParticipant: Int? = 0,

    @DocumentId
    val id: String? = null
)

fun EventCollection.toEvent() = Event(
    this.id?:"",
    this.name?:"",
    this.location?:"",
    this.time?: Timestamp.now(),
    this.totalParticipant?:0,
    this.checkInParticipant?:0
)