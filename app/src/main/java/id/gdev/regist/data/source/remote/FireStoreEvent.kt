package id.gdev.regist.data.source.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.gdev.regist.data.source.remote.collection.EventCollection
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.FilterField
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d
import id.gdev.regist.utils.FilterSort

class FireStoreEvent {

    private val fireStore = Firebase.firestore

    fun createNewEvent(
        eventCollection: EventCollection,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .add(eventCollection)
            .addOnSuccessListener {
                onResult.invoke(true, "Success Create new Event")
            }
            .addOnFailureListener {
                CreateLog.d("onFailure = ${it.message}")
                onResult.invoke(false, "Failed Create new Event")
            }
        this::class.java
    }

    fun getAllEvent(
        onResult: (isSuccess: Boolean, List<EventCollection>) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    CreateLog.d("Listen failed = ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val listOfData = mutableListOf<EventCollection>()
                    snapshot.documents.forEach { docs ->
                        docs.toObject(EventCollection::class.java)?.let { listOfData.add(it) }
                    }
                    onResult.invoke(true, listOfData)
                } else {
                    CreateLog.d("Failed Data is Empty")
                    onResult.invoke(false, listOf())
                }
            }
    }

    fun getEventById(
        id: String,
        onResult: (isSuccess: Boolean, eventCollection: EventCollection) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .document(id)
            .get()
            .addOnSuccessListener {
                it.toObject(EventCollection::class.java)?.let { collection ->
                    onResult.invoke(true, collection)
                }
            }
            .addOnFailureListener {
                onResult.invoke(false, EventCollection())
            }
    }

    fun addEventParticipant(
        eventId: String, listParticipant: List<ParticipantCollection>,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        updateTotalParticipant(eventId, listParticipant.size) { isSuccess, message ->
            CreateLog.d("IsUpdate Success = $isSuccess, message = $message")
            if (isSuccess) {
                val listTaskSuccess = mutableListOf<Pair<Boolean, Int>>()
                listParticipant.forEachIndexed { index, participant ->
                    fireStore.collection(FireStoreDocument.EVENT)
                        .document(eventId)
                        .collection(FireStoreDocument.EVENT_PARTICIPANT)
                        .add(participant)
                        .addOnSuccessListener {
                            listTaskSuccess.add(Pair(true, index))
                            if (index == listParticipant.size - 1) {
                                if (listTaskSuccess.none { !it.first }) onResult.invoke(
                                    true,
                                    "Success add new Participant"
                                )
                                else {
                                    onResult.invoke(false, "Failed add new Participant")
                                    listTaskSuccess.filter { !it.first }.forEach { failedTask ->
                                        CreateLog.d("Task Failed for ${failedTask.second}")
                                    }

                                }
                            }
                        }
                        .addOnFailureListener {
                            CreateLog.d("onFailure-$index = ${it.message}")
                            listTaskSuccess.add(Pair(false, index))
                        }
                }
            } else onResult.invoke(false, "Failed add new Participant")
        }

    }

    fun getAllParticipant(eventId: String, filterField: FilterField): Query {
        return fireStore.collection(FireStoreDocument.EVENT)
            .document(eventId)
            .collection(FireStoreDocument.EVENT_PARTICIPANT)
            .orderBy(
                filterField.field,
                if (filterField.filterSort == FilterSort.ASC) Query.Direction.ASCENDING else Query.Direction.DESCENDING
            )
            .limit(25)
    }

    fun getDetailParticipant(
        eventId: String,
        participantId: String,
        onResult: (isSuccess: Boolean, participant: ParticipantCollection) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .document(eventId)
            .collection(FireStoreDocument.EVENT_PARTICIPANT)
            .document(participantId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    CreateLog.d("Listen failed = ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(ParticipantCollection::class.java).let {
                        if (it != null) onResult.invoke(true, it)
                        else onResult.invoke(false, ParticipantCollection())
                    }

                } else {
                    CreateLog.d("Failed Participant is not found")
                    CreateLog.d("Snapshot = $snapshot")
                    onResult.invoke(false, ParticipantCollection())
                }
            }
    }

    private fun updateTotalParticipant(
        eventId: String, totalParticipant: Int,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .document(eventId)
            .update("totalParticipant", totalParticipant)
            .addOnSuccessListener {
                onResult.invoke(true, "Success update total participant")
            }
            .addOnFailureListener {
                CreateLog.d("onFailure = ${it.message}")
                onResult.invoke(false, "Failed update total participant")
            }
    }

    fun updateCheckInParticipant(
        eventId: String, participantId: String, valueUpdate: ValueUpdate,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        fireStore.collection(FireStoreDocument.EVENT)
            .document(eventId)
            .collection(FireStoreDocument.EVENT_PARTICIPANT)
            .document(participantId)
            .update(
                mapOf(
                    "checkIn" to (valueUpdate == ValueUpdate.Increment),
                    "lastCheckInTime" to if (valueUpdate == ValueUpdate.Increment) Timestamp.now() else null
                )
            )
            .addOnSuccessListener {
                fireStore.collection(FireStoreDocument.EVENT)
                    .document(eventId)
                    .update(
                        "checkInParticipant",
                        FieldValue.increment(if (valueUpdate == ValueUpdate.Increment) 1 else -1)
                    )
                    .addOnSuccessListener {
                        onResult.invoke(true, "Success update check in participant")
                    }
                    .addOnFailureListener {
                        CreateLog.d("onFailure = ${it.message}")
                        onResult.invoke(false, "Failed update check in participant")
                    }
            }
            .addOnFailureListener {
                CreateLog.d("onFailure = ${it.message}")
                onResult.invoke(false, "Failed update check in participant")
            }
    }

}