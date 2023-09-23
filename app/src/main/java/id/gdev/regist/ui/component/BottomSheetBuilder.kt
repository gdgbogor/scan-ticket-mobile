import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import id.gdev.regist.utils.DottedShape
import id.gdev.regist.utils.FilterSort
import id.gdev.regist.utils.SheetFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    component: @Composable () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        component()
    }
}

@Composable
fun FilterSheet(
    listFilter: List<SheetFilter>,
    onClick: (Int, FilterSort) -> Unit
) {
    val radioOptions = listOf(FilterSort.ASC.toString(), FilterSort.DESC.toString())
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val isValid by remember {
        derivedStateOf {
            selectedIndex != -1 || listFilter.any { it.selected == true }
        }
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(listFilter) { index, filter ->
            Row(
                modifier = Modifier
                    .clickable { selectedIndex = index }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = filter.name
                )
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "",
                    tint = if (selectedIndex != -1) {
                        if (selectedIndex == index) LocalContentColor.current else Color.Transparent
                    } else {
                        if (filter.selected == true) LocalContentColor.current else Color.Transparent
                    }
                )
            }
        }
        item {
            Box(
                Modifier
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(
                        Color.Gray,
                        shape = DottedShape(step = 10.dp)
                    )
            )
        }
        item {
            Row(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    if (selectedIndex != -1) onClick.invoke(
                        selectedIndex,
                        if (selectedOption == FilterSort.ASC.toString()) FilterSort.ASC else FilterSort.DESC
                    )
                },
                enabled = isValid
            ) {
                Text(text = "Apply")
            }
        }
        item {
            Box(modifier = Modifier.height(50.dp))
        }
    }
}

fun getListFilter(listFilter: List<String>) = listFilter.map {
    SheetFilter("Filter by $it")
}
