package id.gdev.regist.data

import androidx.paging.PagingData
import id.gdev.regist.data.source.remote.RemoteDataSource
import id.gdev.regist.data.source.remote.ValueUpdate
import id.gdev.regist.data.source.remote.collection.OptionalCheckInCollection
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.FilterField
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

    override fun getAllParticipant(
        eventId: String,
        filterField: FilterField
    ): Flow<PagingData<Participant>> =
        remoteDataSource.getAllParticipant(eventId, filterField)

    override fun getDataAllParticipant(
        eventId: String,
        filterField: FilterField
    ): Flow<Result<List<Participant>>> = remoteDataSource.getFullDataParticipant(eventId, filterField)

    override suspend fun getDetailParticipant(
        eventId: String,
        participantId: String
    ): Flow<Result<Participant>> = remoteDataSource.getDetailParticipant(eventId, participantId)

    override suspend fun findParticipantByHeader(
        eventId: String,
        header: String
    ): Flow<Result<Participant>> = remoteDataSource.findParticipantByHeader(eventId, header)

    override suspend fun updateCheckInParticipant(
        eventId: String,
        participantId: String,
        valueUpdate: ValueUpdate
    ): Flow<Result<String>> =
        remoteDataSource.updateCheckInParticipant(eventId, participantId, valueUpdate)

    override suspend fun updateOptionalCheckInParticipant(
        eventId: String,
        participantId: String,
        optionalCheckInCollection: List<OptionalCheckInCollection>
    ): Flow<Result<String>> =
        remoteDataSource.updateOptionalCheckIn(eventId, participantId, optionalCheckInCollection)

}