package id.gdev.regist.ui.screen.createevent

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import id.gdev.regist.domain.model.Event
import id.gdev.regist.ui.component.DatePickerBuilder
import id.gdev.regist.ui.component.LargeDropdownMenu
import id.gdev.regist.ui.component.LoadingDialog
import id.gdev.regist.ui.component.TimePickerBuilder
import id.gdev.regist.ui.theme.RegistrationAppTheme
import id.gdev.regist.utils.BarcodeEncoding
import id.gdev.regist.utils.TimeUtils.dateSimple
import id.gdev.regist.utils.TimeUtils.hourMinutes
import id.gdev.regist.utils.getBarcodeEncoding
import id.gdev.regist.utils.getList
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController
) {
    val eventViewModel = hiltViewModel<CreateEventViewModel>()

    var isOpenDatePicker by remember { mutableStateOf(false) }
    var isOpenTimePicker by remember { mutableStateOf(false) }
    var selectedEncodingIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    val createEventState by eventViewModel.createEventState.collectAsStateWithLifecycle()
    val isLoading by eventViewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(createEventState.first) {
        if (createEventState.first) navController.navigateUp()
        if (createEventState.second.isNotBlank()) Toast.makeText(
            context, createEventState.second, Toast.LENGTH_SHORT
        ).show()
    }

    var eventName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var eventLocation by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var eventDate by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var eventTime by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var barcodeEncoder by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val eventCalendar by remember { mutableStateOf(Calendar.getInstance()) }

    val isFieldValid by remember {
        derivedStateOf {
            listOf(
                eventName,
                eventLocation,
                eventDate,
                eventTime,
                barcodeEncoder
            ).none { it.text.isBlank() }
        }
    }


    DatePickerBuilder(isOpen = isOpenDatePicker, onDismiss = { isOpenDatePicker = false }) {
        eventDate = TextFieldValue(it.dateSimple)
        val cal = Calendar.getInstance()
        cal.time = it.toDate()
        eventCalendar.apply {
            set(Calendar.YEAR, cal.get(Calendar.YEAR))
            set(Calendar.MONTH, cal.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
        }
    }
    TimePickerBuilder(showTimePicker = isOpenTimePicker, onDismiss = { isOpenTimePicker = false }) {
        eventTime = TextFieldValue(Timestamp(it.time).hourMinutes)
        eventCalendar.apply {
            set(Calendar.HOUR_OF_DAY, it.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, it.get(Calendar.MINUTE))
        }
    }

    if (isLoading) LoadingDialog {}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Event") },
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
                .fillMaxSize()
                .padding(it),
        ) {
            val (name, location, time, encoding, button) = createRefs()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, 32.dp)
                        end.linkTo(parent.end, 32.dp)
                        width = Dimension.fillToConstraints
                    },
                value = eventName,
                label = { Text(text = "Event Name") },
                onValueChange = { value ->
                    eventName = value
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(location) {
                        top.linkTo(name.bottom, 16.dp)
                        start.linkTo(parent.start, 32.dp)
                        end.linkTo(parent.end, 32.dp)
                        width = Dimension.fillToConstraints
                    },
                value = eventLocation,
                label = { Text(text = "Event Location") },
                onValueChange = { value ->
                    eventLocation = value
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(time) {
                        top.linkTo(location.bottom, 16.dp)
                        start.linkTo(parent.start, 32.dp)
                        end.linkTo(parent.end, 32.dp)
                        width = Dimension.fillToConstraints
                    }
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1.2f),
                    value = eventDate,
                    label = { Text(text = "Date") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    },
                    onValueChange = {},
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect { interaction ->
                                    if (interaction is PressInteraction.Release) {
                                        if (!isOpenDatePicker) isOpenDatePicker = true
                                    }
                                }
                            }
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = eventTime,
                    label = { Text(text = "Time") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = "Calendar"
                        )
                    },
                    onValueChange = {},
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect { interaction ->
                                    if (interaction is PressInteraction.Release) {
                                        if (!isOpenTimePicker) isOpenTimePicker = true
                                    }
                                }
                            }
                        }
                )
            }

            LargeDropdownMenu(
                modifier = Modifier.constrainAs(encoding) {
                    top.linkTo(time.bottom, 16.dp)
                    start.linkTo(parent.start, 32.dp)
                    end.linkTo(parent.end, 32.dp)
                    width = Dimension.fillToConstraints
                },
                label = "Barcode Encoding",
                items = BarcodeEncoding.values().getList(),
                selectedIndex = selectedEncodingIndex,
                onItemSelected = { index, _ ->
                    selectedEncodingIndex = index
                    barcodeEncoder = TextFieldValue(BarcodeEncoding.values().getList()[index])
//                    setupViewModel.updateParticipantHeader(csvHeader[index])
                },
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom, 32.dp)
                        start.linkTo(parent.start, 32.dp)
                        end.linkTo(parent.end, 32.dp)
                        width = Dimension.fillToConstraints
                    },
                onClick = {
                    eventViewModel.createNewEvent(
                        Event(
                            "", eventName.text, eventLocation.text,
                            Timestamp(eventCalendar.time),
                            barcodeEncoder.text.getBarcodeEncoding(),
                            0, 0
                        )
                    )
                },
                enabled = isFieldValid
            ) {
                Text(text = "Create Event")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateEventScreen() {
    RegistrationAppTheme {
        CreateEventScreen(rememberNavController())
    }
}