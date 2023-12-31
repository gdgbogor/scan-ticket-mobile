package id.gdev.regist.ui.screen.detailparticipant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.data.source.remote.ValueUpdate
import id.gdev.regist.domain.model.Participant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailParticipantViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
): ViewModel() {

    private var _participant = MutableStateFlow(Participant.INIT)
    val participant get() = _participant.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun getDetailParticipant(
        evenId: String, participantId: String
    ){
        viewModelScope.launch {
            registrationRepository.getDetailParticipant(evenId, participantId).collect{
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        _participant.value = it.data
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun updateCheckIn(
        evenId: String, participantId: String, isCheck: Boolean
    ){
        viewModelScope.launch {
            registrationRepository.updateCheckInParticipant(
                evenId, participantId, if (isCheck) ValueUpdate.Increment else ValueUpdate.Decrement
            ).collect{
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                    }
                }
            }
        }
    }
}