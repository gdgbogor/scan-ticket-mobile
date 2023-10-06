package id.gdev.regist.ui.screen.setup

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.domain.model.OptionalCheckIn
import id.gdev.regist.domain.model.toCollection
import id.gdev.regist.ui.component.LargeDropdownMenu
import id.gdev.regist.ui.component.LoadingDialog
import id.gdev.regist.ui.component.OptionalCheckIn
import id.gdev.regist.ui.component.ParticipantCardItem
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.ui.theme.RegistrationAppTheme
import id.gdev.regist.utils.CreateLog
import id.gdev.regist.utils.CreateLog.d

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    navController: NavController
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    val context = LocalContext.current
    val loading by setupViewModel.isLoading.collectAsStateWithLifecycle()
    val listOfHeader by remember {
        mutableStateOf(
            navController.previousBackStackEntry
                ?.savedStateHandle?.get<List<Array<String>>>(MainArg.SETUP_ARG)
                ?: emptyList()
        )
    }
    val eventId by remember {
        mutableStateOf(
            navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>(MainArg.EVENT_ID) ?: ""
        )
    }

    if (loading) LoadingDialog("Adding Participant ...")

    val previewParticipant by setupViewModel.previewParticipant.collectAsStateWithLifecycle()
    val addParticipantState by setupViewModel.addParticipantState.collectAsStateWithLifecycle()

    var listOfItem by remember { mutableStateOf(emptyList<Map<String, String>>()) }
    var csvHeader by remember { mutableStateOf(emptyList<String>()) }

    var selectedHeaderIndex by remember { mutableIntStateOf(-1) }
    var selectedTitleIndex by remember { mutableIntStateOf(-1) }
    var selectedSubtitleIndex by remember { mutableIntStateOf(-1) }

    var listCheck by remember { mutableStateOf(emptyList<OptionalCheckIn>()) }
    var isListCheckValid by remember { mutableStateOf(true) }

    val isCheckInOptionalExist by remember {
        derivedStateOf {
            listCheck.isNotEmpty()
        }
    }

    val isSubmitValid by remember {
        derivedStateOf {
            listOf(
                selectedHeaderIndex,
                selectedTitleIndex,
                selectedSubtitleIndex
            ).none { index -> index == -1 }
        }
    }

    LaunchedEffect(listOfHeader.isNotEmpty()) {
        if (csvHeader.isEmpty()) {
            csvHeader = listOfHeader.toMutableList().removeAt(0).toList()
            listOfHeader.subList(1, listOfHeader.size).map { data ->
                val mapResult = mutableMapOf<String, String>()
                data.mapIndexed { index, mappedData -> mapResult.put(csvHeader[index], mappedData) }
                mapResult
            }.let { listOfItem = it }
        }
    }

    LaunchedEffect(addParticipantState) {
        if (addParticipantState.second.isNotBlank()) {
            if (addParticipantState.first) navController.navigateUp()
            else Toast.makeText(context, addParticipantState.second, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Setup Component") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            val (content, item, titleSetup, header, btnPreview, btnSubmit) = createRefs()
            LazyColumn(
                modifier = Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btnPreview.top, 16.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ParticipantCardItem(
                        modifier = Modifier.fillMaxWidth(),
                        participant = previewParticipant
                    )
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Setup Card Component",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Select your imported csv header for card component"
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        LargeDropdownMenu(
                            label = "Header",
                            items = csvHeader,
                            selectedIndex = selectedHeaderIndex,
                            onItemSelected = { index, _ ->
                                selectedHeaderIndex = index
                                setupViewModel.updateParticipantHeader(csvHeader[index])
                            },
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LargeDropdownMenu(
                            label = "Title",
                            items = csvHeader,
                            selectedIndex = selectedTitleIndex,
                            onItemSelected = { index, _ ->
                                selectedTitleIndex = index
                                setupViewModel.updateParticipantTitle(csvHeader[index])
                            },
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LargeDropdownMenu(
                            label = "Subtitle",
                            items = csvHeader,
                            selectedIndex = selectedSubtitleIndex,
                            onItemSelected = { index, _ ->
                                selectedSubtitleIndex = index
                                setupViewModel.updateParticipantSubTitle(csvHeader[index])
                            },
                        )
                    }
                }
                item {
                    AnimatedVisibility(visible = isCheckInOptionalExist) {
                        Text(
                            text = "Optional CheckIn",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(listCheck.size) { index ->
                    OptionalCheckIn(
                        optCheckIn = listCheck[index],
                        onChange = { changeIndex, label ->
                            listCheck = listCheck.toMutableList().apply {
                                this[changeIndex].label = label
                            }
                            CreateLog.d("OnTextChange = $changeIndex, $label")
                            CreateLog.d("OnTextChange List = $listCheck")
                            isListCheckValid = listCheck.none { check -> check.label.isBlank() }
                        },
                        onClose = { selectedOpt ->
                            listCheck = listCheck.toMutableList().apply {
                                remove(selectedOpt)
                            }
                            isListCheckValid = listCheck.none { check -> check.label.isBlank() }
                        }
                    )
                }

                item {
                    Button(onClick = {
                        listCheck = listCheck.toMutableList().apply {
                            add(OptionalCheckIn(this.size, "", false))
                        }
                        isListCheckValid = listCheck.none { check -> check.label.isBlank() }

                    }) {
                        Text(text = "Add Optional CheckIn")
                    }
                }
            }



            OutlinedButton(
                modifier = Modifier.constrainAs(btnPreview) {
                    start.linkTo(parent.start, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    end.linkTo(btnSubmit.start, 8.dp)
                    width = Dimension.fillToConstraints
                },
                onClick = {
                    listOfItem.random().let { data ->
                        val currentParticipant = setupViewModel.getCurrentParticipant()
                        setupViewModel.updatePreviewParticipant(
                            data[currentParticipant.header] ?: "",
                            data[currentParticipant.title] ?: "",
                            data[currentParticipant.subtitle] ?: "",
                        )
                    }
                }
            ) {
                Text(text = "Preview")
            }
            Button(
                modifier = Modifier.constrainAs(btnSubmit) {
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(btnPreview.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                onClick = {
                    val currentParticipant = setupViewModel.getCurrentParticipant()
                    val allParticipant = mutableListOf<ParticipantCollection>()
                    listOfItem.forEach { item ->
                        allParticipant.add(
                            ParticipantCollection(
                                item[currentParticipant.header] ?: "",
                                item[currentParticipant.title] ?: "",
                                item[currentParticipant.subtitle] ?: "",
                                optionalCheckIn = listCheck.map { check -> check.toCollection() },
                                fullData = item
                            )
                        )
                    }
                    setupViewModel.addNewParticipant(eventId, allParticipant)
                },
                enabled = isSubmitValid && isListCheckValid
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSetupScreen() {
    RegistrationAppTheme {
        SetupScreen(rememberNavController())
    }
}