package id.gdev.regist.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBuilder(
    showTimePicker: Boolean,
    onDismiss: () -> Unit,
    result: (Calendar) -> Unit
) {
    val state = rememberTimePickerState()
    var showingPicker by remember { mutableStateOf(true) }
    val configuration = LocalConfiguration.current

    if (showTimePicker) {
        TimePickerDialog(
            title = if (showingPicker) {
                "Select Time "
            } else {
                "Enter Time"
            },
            onCancel = { onDismiss.invoke() },
            onConfirm = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                result.invoke(cal)
                onDismiss.invoke()
            },
            toggle = {
                IconButton(onClick = { showingPicker = !showingPicker }) {
                    val icon = if (showingPicker) Icons.Rounded.Keyboard else Icons.Rounded.Schedule
                    Icon(
                        icon,
                        contentDescription = if (showingPicker) "Switch to Text Input" else "Switch to Touch Input"
                    )
                }
            }
        ) {
            if (showingPicker && configuration.screenHeightDp > 400) {
                TimePicker(state = state)
            } else {
                TimeInput(state = state)
            }
        }
    }
}