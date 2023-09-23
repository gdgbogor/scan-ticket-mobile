package id.gdev.regist.ui.screen.detailevent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.domain.model.Event
import id.gdev.regist.domain.model.FilterField
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.Filter
import id.gdev.regist.utils.FilterSort
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

    suspend fun getAllParticipant(eventId: String, filterField: FilterField? = null) {
        _isLoading.value = true
        registrationRepository.getAllParticipant(
            eventId,
            filterField = filterField ?: FilterField("lastCheckInTime", FilterSort.DESC)
        )
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _isLoading.value = false
                _participants.value = it
            }
    }

    fun filterData(eventId: String, filter: String, sort: FilterSort) {
        Log.d("TAG", "filterData: $filter")
        Log.d("TAG", "filterData real: ${FilterField(Filter.getFieldName(filter), sort)}")
        viewModelScope.launch {
            getAllParticipant(eventId, FilterField(Filter.getFieldName(filter), sort))
        }
    }
}