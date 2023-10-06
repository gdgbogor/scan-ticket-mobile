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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import getListFilter
import id.gdev.regist.ui.component.EmptyPage
import id.gdev.regist.ui.component.ErrorMessage
import id.gdev.regist.ui.component.LoadingDialog
import id.gdev.regist.ui.component.LoadingNextPageItem
import id.gdev.regist.ui.component.PageLoader
import id.gdev.regist.ui.component.ParticipantCardItem
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.ui.screen.main.MainRouter
import id.gdev.regist.ui.screen.scanner.checkModule
import id.gdev.regist.ui.screen.scanner.getScanner
import id.gdev.regist.utils.Filter
import id.gdev.regist.utils.PopMessage
import id.gdev.regist.utils.decode
import id.gdev.regist.utils.readCSV


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
    val isParticipantFound by detailEventViewModel.isParticipantFound.collectAsStateWithLifecycle()
    val isLoading by detailEventViewModel.isLoading.collectAsStateWithLifecycle()
    val listOfParticipant = detailEventViewModel.participants.collectAsLazyPagingItems()
    var showFilter by remember { mutableStateOf(false) }
    val listOfFilter by remember {
        mutableStateOf(getListFilter(listOf(Filter.CheckInDate, Filter.Title, Filter.Header, Filter.Subtitle)))
    }

    LaunchedEffect(Unit) {
        if (eventId?.isNotBlank() == true) {
            detailEventViewModel.getEventById(eventId)
            detailEventViewModel.getAllParticipant(eventId)
        }
    }

    if (isLoading) LoadingDialog {}

    LaunchedEffect(isParticipantFound) {
        if (isParticipantFound.first) {
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                set(MainArg.EVENT_ID, eventId)
                set(MainArg.EVENT_ENCODING, event.barcodeEncoding.name)
                set(MainArg.PARTICIPANT_ID, isParticipantFound.second)
            }
            detailEventViewModel.clearSearch()
            navController.navigate(MainRouter.DETAIL_PARTICIPANT)
        } else {
            if (isParticipantFound.second.isNotBlank()) popMessage.show(isParticipantFound.second)
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
                if (eventId?.isNotBlank() == true) {
                    detailEventViewModel.filterData(eventId, listOfFilter[index].name, sort)
                }
                showFilter = false
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = event.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
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
                        detailEventViewModel.shareCsv(context, event)
                    }) {
                        Icon(imageVector = Icons.Rounded.Share, contentDescription = "share")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (listOfParticipant.itemCount != 0)
                    checkModule(context, scanner) { isSuccess, message ->
                        if (isSuccess) scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                if (
                                    barcode.rawValue.toString()
                                        .isNotBlank() && !eventId.isNullOrBlank()
                                ) {
                                    detailEventViewModel.findParticipant(
                                        eventId,
                                        barcode.decode(event.barcodeEncoding)
                                    )
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
                    imageVector = if (listOfParticipant.itemCount != 0) Icons.Rounded.QrCodeScanner else Icons.Rounded.Add,
                    contentDescription = if (listOfParticipant.itemCount != 0) "Scan QR" else "Add File"
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
            if (listOfParticipant.itemCount != 0) items(listOfParticipant.itemCount) { index ->
                listOfParticipant[index]?.let { data ->
                    ParticipantCardItem(participant = data) { participant ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(MainArg.EVENT_ID, eventId)
                            set(MainArg.PARTICIPANT_ID, participant.id)
                        }
                        navController.navigate(MainRouter.DETAIL_PARTICIPANT)
                    }
                }
            }
            listOfParticipant.loadState.apply {
                when {
                    refresh is LoadState.Loading -> {
                        item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                    }

                    refresh is LoadState.Error -> {

                        item {
                            if (listOfParticipant.itemCount == 0) EmptyPage(
                                modifier = Modifier.fillParentMaxSize()
                            ) else ErrorMessage(
                                modifier = Modifier.fillParentMaxSize(),
                                onClickRetry = { listOfParticipant.retry() }
                            )
                        }
                    }

                    append is LoadState.Loading -> {
                        item { LoadingNextPageItem(modifier = Modifier) }
                    }

                    append is LoadState.Error -> {
                        item {
                            ErrorMessage(
                                modifier = Modifier,
                                onClickRetry = { listOfParticipant.retry() })
                        }
                    }
                }
            }
        }
    }
}