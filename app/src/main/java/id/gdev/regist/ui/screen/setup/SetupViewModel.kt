package id.gdev.regist.ui.screen.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.Result
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
): ViewModel() {

    private var participant = Participant(
        "",
        DefaultSetup.INIT_HEADER,
        DefaultSetup.INIT_TITLE,
        DefaultSetup.INIT_SUBTITLE,
        mapOf()
    )

    private val _previewParticipant = MutableStateFlow(Participant(
        "",
        DefaultSetup.INIT_HEADER,
        DefaultSetup.INIT_TITLE,
        DefaultSetup.INIT_SUBTITLE,
        mapOf()
    ))
    val previewParticipant get() = _previewParticipant.asStateFlow()

    fun updatePreviewParticipant(header: String, title: String, subtitle: String){
        val newValue = previewParticipant.value.copy(
            header = header,
            title = title,
            subtitle = subtitle
        )
        _previewParticipant.value = newValue
    }

    fun getCurrentParticipant() = participant

    fun updateParticipantHeader(header: String){ participant.header = header }
    fun updateParticipantTitle(title: String){ participant.title = title }
    fun updateParticipantSubTitle(subtitle: String){ participant.subtitle = subtitle }

    private var _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private var _addParticipantState = MutableStateFlow(Pair(false, ""))
    val addParticipantState get() = _addParticipantState.asStateFlow()

    fun addNewParticipant(eventId: String, listOfParticipant: List<ParticipantCollection>) {
        viewModelScope.launch {
            registrationRepository.addNewParticipant(eventId, listOfParticipant).collect {
                when (it) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        _addParticipantState.value = Pair(true, "Success add new participant")
                    }

                    is Result.Failed -> {
                        _isLoading.value = false
                        CreateLog.d(it.message)
                        _addParticipantState.value = Pair(false, it.message)
                    }
                }
            }
        }
    }

    private object DefaultSetup {
        const val INIT_HEADER = "Header"
        const val INIT_TITLE = "Title"
        const val INIT_SUBTITLE = "Subtitle"
    }
}
