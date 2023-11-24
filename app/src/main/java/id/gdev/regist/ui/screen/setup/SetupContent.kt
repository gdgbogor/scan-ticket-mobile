package id.gdev.regist.ui.screen.setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.OptionalCheckIn
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.domain.model.toCollection
import id.gdev.regist.ui.component.LargeDropdownMenu
import id.gdev.regist.ui.component.OptionalCheckIn
import id.gdev.regist.ui.component.ParticipantCardItem
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupContent(
    previewParticipant: Participant,
    csvHeader: List<String>,
    onBack: () -> Unit,
    updateParticipantHeader: (String) -> Unit,
    updateParticipantTitle: (String) -> Unit,
    updateParticipantSubtitle: (String) -> Unit
){

}