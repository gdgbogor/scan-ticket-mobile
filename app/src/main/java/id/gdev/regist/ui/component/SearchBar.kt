import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.ui.component.ParticipantCardItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    searchResults: List<Participant>,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        modifier = modifier,
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { active = false },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = { Text("Search search text") },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        trailingIcon = {
            if (searchResults.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = searchResults.size,
                itemContent = { index ->
                    ParticipantCardItem(participant = searchResults[index])
                }
            )
        }
    }
}