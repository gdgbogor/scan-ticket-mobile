package id.gdev.regist.ui.screen.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.gdev.regist.R
import id.gdev.regist.ui.component.EventCardItem
import id.gdev.regist.ui.screen.main.MainArg
import id.gdev.regist.ui.screen.main.MainRouter

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOfEvent) { event ->
                EventCardItem(event = event) { selectedEvent ->
                    navController.navigate(MainRouter.EVENT_DETAIL.plus(MainArg.setArg(selectedEvent.id)))
                }
            }
        }
    }
}

