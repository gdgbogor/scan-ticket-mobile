package id.gdev.regist.ui.screen.detailevent

import BottomSheet
import FilterSheet
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import getListFilter
import id.gdev.regist.ui.component.ParticipantCardItem
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.ui.screen.main.MainRouter
import id.gdev.regist.ui.screen.scanner.checkModule
import id.gdev.regist.ui.screen.scanner.getScanner
import id.gdev.regist.utils.Filter
import id.gdev.regist.utils.PopMessage
import id.gdev.regist.utils.readCSV
import id.gdev.regist.utils.shareCsv


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEventScreen(
    navController: NavController,
    eventId: String? = ""
) {
    val context = LocalContext.current
    val popMessage = PopMessage(context)
    val detailEventViewModel = hiltViewModel<DetailEventViewModel>()
    val scanner by remember { mutableStateOf(getScanner(context)) }
    val event by detailEventViewModel.event.collectAsStateWithLifecycle()
    val listOfParticipant by detailEventViewModel.participants.collectAsStateWithLifecycle()
    var showFilter by remember { mutableStateOf(false) }
    val listOfFilter by remember {
        mutableStateOf(getListFilter(listOf(Filter.CheckInDate, Filter.Title)))
    }

    LaunchedEffect(Unit) {
        if (eventId?.isNotBlank() == true) {
            detailEventViewModel.getEventById(eventId)
            detailEventViewModel.getAllParticipant(eventId)
        }
    }

    val startForResult = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        result?.let {
            val data = context.readCSV(it)
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                set(MainArg.SETUP_ARG, data)
                set(MainArg.EVENT_ID, eventId)
            }
            navController.navigate(MainRouter.SETUP)
        }
    }

    if (showFilter) {
        BottomSheet(
            onDismiss = { showFilter = false }
        ) {
            FilterSheet(listFilter = listOfFilter) { index, sort ->
                listOfFilter.map { filter -> filter.selected = false }
                listOfFilter[index].selected = true
                detailEventViewModel.filterData(listOfFilter[index].name, sort)
                showFilter = false
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = event.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showFilter = true
                    }) {
                        Icon(imageVector = Icons.Rounded.FilterList, contentDescription = "filter")
                    }
                    IconButton(onClick = {
                        context.shareCsv(event.name, listOfParticipant)
                    }) {
                        Icon(imageVector = Icons.Rounded.Share, contentDescription = "share")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (listOfParticipant.isNotEmpty())
                    checkModule(context, scanner) { isSuccess, message ->
                        if (isSuccess) scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                listOfParticipant.find {
                                    val resultData =
                                        it.fullData["Ticket number"]?.removePrefix("GOOGA23")
                                    val resultCode = barcode.rawValue.toString().split(":").last()
                                    resultData == resultCode
                                }.let { participant ->
                                    if (participant != null) {
                                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                                            set(MainArg.EVENT_ID, eventId)
                                            set(MainArg.PARTICIPANT_ID, participant.id)
                                        }
                                        navController.navigate(MainRouter.DETAIL_PARTICIPANT)
                                    } else popMessage.show("Participant is Not Found")
                                }
                            }
                            .addOnFailureListener { error ->
                                popMessage.show(error.message.toString())
                            }
                        else popMessage.show(message)
                    }
                else startForResult.launch("text/*")
            }) {
                Icon(
                    imageVector = if (listOfParticipant.isNotEmpty()) Icons.Rounded.QrCodeScanner else Icons.Rounded.Add,
                    contentDescription = if (listOfParticipant.isNotEmpty()) "Scan QR" else "Add File"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (listOfParticipant.isNotEmpty()) items(listOfParticipant) { participants ->
                ParticipantCardItem(participant = participants) { participant ->
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(MainArg.EVENT_ID, eventId)
                        set(MainArg.PARTICIPANT_ID, participant.id)
                    }
                    navController.navigate(MainRouter.DETAIL_PARTICIPANT)
                }
            }
        }
    }
}