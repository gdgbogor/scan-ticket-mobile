package id.gdev.regist.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.gdev.regist.data.Result
import id.gdev.regist.data.paging.ParticipantPagingSource
import id.gdev.regist.data.source.remote.collection.EventCollection
import id.gdev.regist.data.source.remote.collection.OptionalCheckInCollection
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.data.source.remote.collection.toEvent
import id.gdev.regist.data.source.remote.collection.toParticipant
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.FilterField
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d
import id.gdev.regist.utils.LIMIT_DATA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fireStoreEvent: FireStoreEvent
) {
    suspend fun createNewEvent(eventCollection: EventCollection) = callbackFlow<Result<String>> {
        trySend(Result.loading())
        fireStoreEvent.createNewEvent(eventCollection) { isSuccess, message ->
            if (isSuccess) trySend(Result.success(message)) else trySend(Result.failed(message))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed Create new Event"))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllEvent() = callbackFlow<Result<List<Event>>> {
        trySend(Result.loading())
        fireStoreEvent.getAllEvent { isSuccess, eventCollections ->
            if (isSuccess)
                trySend(Result.success(eventCollections.map { it.toEvent() }))
            else trySend(Result.failed("Data Event is Empty"))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed Get All Event"))
    }.flowOn(Dispatchers.IO)

    suspend fun getEventById(id: String) = callbackFlow<Result<Event>> {
        trySend(Result.loading())
        fireStoreEvent.getEventById(id) { isSuccess, eventCollection ->
            if (isSuccess) trySend(Result.success(eventCollection.toEvent()))
            else trySend(Result.failed("Failed to get event"))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed to get event"))
    }.flowOn(Dispatchers.IO)

    suspend fun addNewParticipant(
        eventId: String, listParticipant: List<ParticipantCollection>
    ) = callbackFlow<Result<String>> {
        trySend(Result.loading())
        fireStoreEvent.addEventParticipant(eventId, listParticipant) { isSuccess, message ->
            if (isSuccess) trySend(Result.success(message)) else trySend(Result.failed(message))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed add new participant"))
    }.flowOn(Dispatchers.IO)

    fun getAllParticipant(
        eventId: String,
        filterField: FilterField
    ): Flow<PagingData<Participant>> =
        Pager(PagingConfig(pageSize = LIMIT_DATA)) {
            ParticipantPagingSource(
                fireStoreEvent.getAllParticipant(
                    eventId,
                    filterField,
                    LIMIT_DATA.toLong()
                )
            )
        }.flow

    fun getFullDataParticipant(
        eventId: String, filterField: FilterField
    ) = callbackFlow<Result<List<Participant>>> {
        trySend(Result.loading())
        fireStoreEvent.getAllParticipant(eventId, filterField).get()
            .addOnSuccessListener {
                if (!it.isEmpty) trySend(
                    Result.success(
                        it.toObjects(ParticipantCollection::class.java)
                            .map { collection -> collection.toParticipant() })
                )
                else trySend(Result.failed("There's no participant yet"))
            }
            .addOnFailureListener {
                trySend(Result.failed("Failed to get all data participant"))
            }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed to get all data participant"))
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailParticipant(
        eventId: String, participantId: String
    ) = callbackFlow<Result<Participant>> {
        trySend(Result.loading())
        fireStoreEvent.getDetailParticipant(eventId, participantId) { isSuccess, participant ->
            if (isSuccess) trySend(Result.success(participant.toParticipant()))
            else trySend(Result.failed("Failed get data participant"))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed get data participant"))
    }.flowOn(Dispatchers.IO)

    suspend fun findParticipantByHeader(
        eventId: String, header: String
    ) = callbackFlow<Result<Participant>> {
        trySend(Result.loading())
        fireStoreEvent.findParticipantByHeader(eventId, header) { isSuccess, participant ->
            if (isSuccess) trySend(Result.success(participant.toParticipant()))
            else trySend(Result.failed("Participant is not found"))
        }
        awaitClose()
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed get data participant"))
    }.flowOn(Dispatchers.IO)

    suspend fun updateOptionalCheckIn(
        eventId: String, participantId: String,
        optionalCheckInCollection: List<OptionalCheckInCollection>
    ) = callbackFlow<Result<String>> {
        trySend(Result.loading())
        fireStoreEvent.updateOptionalCheckIn(
            eventId, participantId, optionalCheckInCollection
        ) { isSuccess, message ->
            if (isSuccess) trySend(Result.success(message))
            else trySend(Result.failed(message))
        }
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed update optional check in participant"))
    }.flowOn(Dispatchers.IO)

    suspend fun updateCheckInParticipant(
        eventId: String, participantId: String, valueUpdate: ValueUpdate
    ) = callbackFlow<Result<String>> {
        trySend(Result.loading())
        fireStoreEvent.updateCheckInParticipant(
            eventId, participantId, valueUpdate
        ) { isSuccess, message ->
            if (isSuccess) trySend(Result.success(message))
            else trySend(Result.failed(message))
        }
    }.catch {
        CreateLog.d("Failed = ${it.message}")
        emit(Result.failed("Failed update check in participant"))
    }.flowOn(Dispatchers.IO)
}