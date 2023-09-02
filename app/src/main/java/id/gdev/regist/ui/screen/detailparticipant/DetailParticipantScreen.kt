package id.gdev.regist.ui.screen.detailparticipant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.gdev.regist.ui.component.ParticipantCardItem
import id.gdev.regist.ui.component.rememberQrBitmapPainter
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.utils.DottedShape
import id.gdev.regist.utils.PopMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailParticipantScreen(
    navController: NavController
) {
    val detailParticipantViewModel = hiltViewModel<DetailParticipantViewModel>()
    val participant by detailParticipantViewModel.participant.collectAsStateWithLifecycle()
    val eventId by remember {
        mutableStateOf(
            navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>(MainArg.EVENT_ID) ?: ""
        )
    }

    val participantId by remember {
        mutableStateOf(
            navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>(MainArg.PARTICIPANT_ID) ?: ""
        )
    }

    val popMessage = PopMessage(LocalContext.current)
    var isDetailOpen by remember { mutableStateOf(false) }

    LaunchedEffect(eventId, participantId) {
        if (eventId.isNotBlank() && participantId.isNotBlank()) {
            detailParticipantViewModel.getDetailParticipant(eventId, participantId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Participant") },
                navigationIcon = {
                    Row {
                        Text(text = "")
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (data, button) = createRefs()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(data) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(button.top)
                        height = Dimension.fillToConstraints
                    },
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ParticipantCardItem(participant = participant)
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Detail Information",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = {
                                    isDetailOpen = !isDetailOpen
                                }) {
                                    Icon(
                                        imageVector = if (isDetailOpen) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = ""
                                    )
                                }
                            }
                            AnimatedVisibility(visible = isDetailOpen) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        Modifier
                                            .height(1.dp)
                                            .fillMaxWidth()
                                            .background(
                                                Color.Gray,
                                                shape = DottedShape(step = 10.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    participant.fullData.forEach { data ->
                                        if (data.value.isNotBlank()) {
                                            Text(
                                                text = data.key,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = data.value)
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "QR Code", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card {
                            Image(
                                modifier = Modifier.padding(8.dp),
                                painter = rememberQrBitmapPainter(
                                    content = "12345:${
                                        participant.fullData["Ticket number"]
                                            ?.removePrefix("GOOGA23")
                                    }",
                                    size = 200.dp
                                ), contentDescription = ""
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .constrainAs(button) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                if (participant.checkIn == true) OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        detailParticipantViewModel.updateCheckIn(eventId, participantId, false)
                    }
                ) {
                    Text(text = "Un Check-In")
                } else Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        detailParticipantViewModel.updateCheckIn(eventId, participantId, true)
                    }
                ) {
                    Text(text = "Check-In")
                }
            }
        }
    }
}