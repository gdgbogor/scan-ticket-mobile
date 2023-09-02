package id.gdev.regist.domain.repository

import id.gdev.regist.data.Result
import id.gdev.regist.data.source.remote.ValueUpdate
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.Participant
import kotlinx.coroutines.flow.Flow

interface IRegistrationRepository {

    suspend fun createNewEvent(event: Event): Flow<Result<String>>
    suspend fun getAllEvent(): Flow<Result<List<Event>>>
    suspend fun getEventById(eventId: String): Flow<Result<Event>>
    suspend fun addNewParticipant(
        eventId: String,
        listParticipant: List<ParticipantCollection>
    ): Flow<Result<String>>

    suspend fun getAllParticipant(eventId: String): Flow<Result<List<Participant>>>
    suspend fun getDetailParticipant(
        eventId: String, participantId: String
    ): Flow<Result<Participant>>

    suspend fun updateCheckInParticipant(
        eventId: String, participantId: String, valueUpdate: ValueUpdate
    ): Flow<Result<String>>
}