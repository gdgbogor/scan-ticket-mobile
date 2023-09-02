package id.gdev.regist.domain.model

import com.google.firebase.Timestamp
import id.gdev.regist.data.source.remote.collection.EventCollection
import java.util.Date

data class Event(
    val id: String,
    val name: String,
    val location: String,
    val time: Timestamp,
    val totalParticipant: Int,
    val checkInParticipant: Int,
) {
    companion object {
        val INIT
            get() = Event(
                "", "", "",
                Timestamp(Date(1692205200000)), 0, 0
            )
    }
}


fun Event.toCollection() = EventCollection(
    name, location, time, 0, 0
)