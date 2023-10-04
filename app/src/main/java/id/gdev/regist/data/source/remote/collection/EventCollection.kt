package id.gdev.regist.data.source.remote.collection

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import id.gdev.regist.domain.model.Event
import id.gdev.regist.utils.BarcodeEncoding
import id.gdev.regist.utils.getBarcodeEncoding

data class EventCollection(
    val name: String? = null,
    val location: String? = null,
    val time: Timestamp? = Timestamp.now(),
    val barcodeEncoding: String? = null,
    val totalParticipant: Int? = 0,
    val checkInParticipant: Int? = 0,

    @DocumentId
    val id: String? = null
)

fun EventCollection.toEvent() = Event(
    this.id ?: "",
    this.name ?: "",
    this.location ?: "",
    this.time ?: Timestamp.now(),
    this.barcodeEncoding?.getBarcodeEncoding() ?: BarcodeEncoding.NONE,
    this.totalParticipant ?: 0,
    this.checkInParticipant ?: 0
)