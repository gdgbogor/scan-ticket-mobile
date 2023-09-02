package id.gdev.regist.data

import id.gdev.regist.data.source.remote.RemoteDataSource
import id.gdev.regist.data.source.remote.ValueUpdate
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.domain.model.toCollection
import id.gdev.regist.domain.repository.IRegistrationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IRegistrationRepository {

    override suspend fun createNewEvent(event: Event): Flow<Result<String>> =
        remoteDataSource.createNewEvent(event.toCollection())

    override suspend fun getAllEvent(): Flow<Result<List<Event>>> =
        remoteDataSource.getAllEvent()

    override suspend fun getEventById(eventId: String): Flow<Result<Event>> =
        remoteDataSource.getEventById(eventId)

    override suspend fun addNewParticipant(
        eventId: String, listParticipant: List<ParticipantCollection>
    ): Flow<Result<String>> =
        remoteDataSource.addNewParticipant(eventId, listParticipant)

    override suspend fun getAllParticipant(eventId: String): Flow<Result<List<Participant>>> =
        remoteDataSource.getAllParticipant(eventId)

    override suspend fun getDetailParticipant(
        eventId: String,
        participantId: String
    ): Flow<Result<Participant>> = remoteDataSource.getDetailParticipant(eventId, participantId)

    override suspend fun updateCheckInParticipant(
        eventId: String,
        participantId: String,
        valueUpdate: ValueUpdate
    ): Flow<Result<String>> =
        remoteDataSource.updateCheckInParticipant(eventId, participantId, valueUpdate)

}