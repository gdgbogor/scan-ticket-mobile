package id.gdev.regist.data.source.remote

object FireStoreDocument {
    const val EVENT = "event"
    const val EVENT_PARTICIPANT = "participant"
}

enum class ValueUpdate{
    Increment, Decrement
}