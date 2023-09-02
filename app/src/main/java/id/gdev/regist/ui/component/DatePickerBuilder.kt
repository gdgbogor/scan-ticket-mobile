package id.gdev.regist.ui.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.google.firebase.Timestamp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBuilder(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onResult: (Timestamp) -> Unit
) {
    if (isOpen) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = { onDismiss.invoke() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss.invoke()
                        onResult.invoke(Timestamp(Date(datePickerState.selectedDateMillis?:0L)))
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss.invoke()
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
