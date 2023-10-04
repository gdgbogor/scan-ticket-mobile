package id.gdev.regist.domain.model

import com.google.firebase.Timestamp
import id.gdev.regist.data.source.remote.collection.EventCollection
import id.gdev.regist.utils.BarcodeEncoding
import java.util.Date

data class Event(
    val id: String,
    val name: String,
    val location: String,
    val time: Timestamp,
    val barcodeEncoding: BarcodeEncoding,
    val totalParticipant: Int,
    val checkInParticipant: Int,
) {
    companion object {
        val INIT
            get() = Event(
                "", "", "",
                Timestamp(Date(1692205200000)), BarcodeEncoding.NONE, 0, 0
            )
    }
}


fun Event.toCollection() = EventCollection(
    name, location, time, barcodeEncoding.toString(), 0, 0
)