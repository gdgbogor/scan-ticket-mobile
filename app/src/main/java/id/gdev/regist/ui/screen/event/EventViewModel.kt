package id.gdev.regist.ui.screen.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.domain.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private var _event = MutableStateFlow(emptyList<Event>())
    val event get() = _event.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun getAllEvent() {
        viewModelScope.launch {
            registrationRepository.getAllEvent().collect {
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
}