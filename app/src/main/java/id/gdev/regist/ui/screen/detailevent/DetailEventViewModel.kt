package id.gdev.regist.ui.screen.detailevent

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.data.source.remote.FireStoreEvent
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.FilterField
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d
import id.gdev.regist.utils.Filter
import id.gdev.regist.utils.FilterSort
import id.gdev.regist.utils.shareCsv
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private var _event = MutableStateFlow(Event.INIT)
    val event get() = _event.asStateFlow()

    private var _participants: MutableStateFlow<PagingData<Participant>> =
        MutableStateFlow(PagingData.empty())
    val participants get() = _participants.asStateFlow()

    private val listOfParticipant = mutableListOf<Participant>()

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private var _isParticipantFound = MutableStateFlow(Pair(false, ""))
    val isParticipantFound get() = _isParticipantFound.asStateFlow()

    fun findParticipant(
        eventId: String,
        participantId: String
    ) {
        viewModelScope.launch {
            registrationRepository.findParticipantByHeader(eventId, participantId).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        _isParticipantFound.value = Pair(true, it.data.id)
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                        _isParticipantFound.value = Pair(false, it.message)
                    }
                }
            }
        }
    }

    fun clearSearch() {
        _isParticipantFound.value = Pair(false, "")
    }

    fun getEventById(eventId: String) {
        viewModelScope.launch {
            registrationRepository.getEventById(eventId).collect {
                when (it) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        _event.value = it.data
                    }

                    is Result.Failed -> {
                    }
                }
            }
        }
    }

    fun shareCsv(context: Context, event: Event) {
        viewModelScope.launch {
            registrationRepository.getDataAllParticipant(
                event.id,
                FilterField("lastCheckInTime", FilterSort.DESC)
            ).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        context.shareCsv(event.name, it.data)
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                        _isParticipantFound.value = Pair(false, it.message)
                    }
                }
            }
        }
    }

    suspend fun getAllParticipant(eventId: String, filterField: FilterField? = null) {
        registrationRepository.getAllParticipant(
            eventId,
            filterField = filterField ?: FilterField("lastCheckInTime", FilterSort.DESC)
        )
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _participants.value = it
            }
    }

    fun filterData(eventId: String, filter: String, sort: FilterSort) {
        viewModelScope.launch {
            getAllParticipant(eventId, FilterField(Filter.getFieldName(filter), sort))
        }
    }
}