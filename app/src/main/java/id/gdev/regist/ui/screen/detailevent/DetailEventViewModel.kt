package id.gdev.regist.ui.screen.detailevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d
import id.gdev.regist.utils.Filter
import id.gdev.regist.utils.FilterSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private var _event = MutableStateFlow(Event.INIT)
    val event get() = _event.asStateFlow()

    private var _participants = MutableStateFlow(emptyList<Participant>())
    val participants get() = _participants.asStateFlow()

    private val listOfParticipant = mutableListOf<Participant>()

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun getEventById(eventId: String) {
        viewModelScope.launch {
            registrationRepository.getEventById(eventId).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        _event.value = it.data
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun getAllParticipant(eventId: String) {
        viewModelScope.launch {
            registrationRepository.getAllParticipant(eventId).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        listOfParticipant.clear()
                        listOfParticipant.addAll(it.data)
                        _participants.value = listOfParticipant
                            .sortedByDescending { participant -> participant.lastCheckInTime }
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                        CreateLog.d(it.message)
                    }
                }
            }
        }
    }

    fun filterData(filter: String, sort: FilterSort) {
        _participants.value = when (filter) {
            Filter.Title -> {
                if (sort == FilterSort.ASC) listOfParticipant.sortedBy { it.title }
                else listOfParticipant.sortedByDescending { it.title }
            }

            Filter.CheckInDate -> {
                if (sort == FilterSort.ASC) listOfParticipant.sortedBy { it.lastCheckInTime }
                else listOfParticipant.sortedByDescending { it.lastCheckInTime }
            }

            else -> listOfParticipant.sortedByDescending { it.lastCheckInTime }
        }
    }
}