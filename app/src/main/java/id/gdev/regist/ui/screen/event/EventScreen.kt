package id.gdev.regist.ui.screen.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.gdev.regist.R
import id.gdev.regist.domain.model.Event
import id.gdev.regist.ui.component.EventCardItem
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.ui.screen.main.MainRouter
import id.gdev.regist.utils.TimeUtils.pastEvent
import id.gdev.regist.utils.TimeUtils.upComingEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    navController: NavController
) {
    val eventViewModel = hiltViewModel<EventViewModel>()
    LaunchedEffect(Unit) {
        eventViewModel.getAllEvent()
    }
    val listOfEvent by eventViewModel.event.collectAsStateWithLifecycle()
    var pastEvent by remember { mutableStateOf(emptyList<Event>()) }
    var upcomingEvent by remember { mutableStateOf(emptyList<Event>()) }

    LaunchedEffect(listOfEvent) {
        if (listOfEvent.isNotEmpty()){
            pastEvent = listOfEvent.filter { it.time.pastEvent }.sortedByDescending { it.time }
            upcomingEvent = listOfEvent.filter { it.time.upComingEvent }.sortedByDescending { it.time }
        }
    }

    var tabState by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Up Coming Event", "Past Event")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Logo GDG",
                        )
                        Text(text = "Registration App")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(MainRouter.CREATE_EVENT)
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Event")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            PrimaryTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = tabState
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabState == index,
                        onClick = { tabState = index },
                        text = {
                            Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    )
                }
            }
            ContentEvent(listOfEvent = if (tabState == 0) upcomingEvent else pastEvent) { eventId ->
                navController.navigate(MainRouter.EVENT_DETAIL.plus(MainArg.setArg(eventId)))
            }
        }
    }
}

@Composable
fun ContentEvent(
    listOfEvent: List<Event>,
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOfEvent) { event ->
            EventCardItem(event = event) { selectedEvent ->
                onEventClick.invoke(selectedEvent.id)
            }
        }
    }
}

