package id.gdev.regist.ui.screen.createevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.domain.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private var _createEventState = MutableStateFlow(Pair(false, ""))
    val createEventState get() = _createEventState.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun createNewEvent(event: Event) {
        viewModelScope.launch {
            registrationRepository.createNewEvent(event).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        _createEventState.value = Pair(true, it.data)
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                        _createEventState.value = Pair(false, it.message)
                    }
                }
            }
        }
    }
}