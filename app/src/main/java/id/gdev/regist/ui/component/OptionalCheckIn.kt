package id.gdev.regist.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.gdev.regist.domain.model.OptionalCheckIn
import id.gdev.regist.ui.theme.RegistrationAppTheme

@Composable
fun OptionalCheckIn(
    optCheckIn: OptionalCheckIn,
    onChange: (index: Int, label: String) -> Unit,
    onClose: (OptionalCheckIn) -> Unit
) {

    var label by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(optCheckIn.label))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = label,
            label = { Text(text = "CheckIn Label") },
            onValueChange = { value ->
                onChange.invoke(optCheckIn.id ?: -1, value.text)
                label = value
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { onClose.invoke(optCheckIn) }
        ) {
            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Delete Option")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOptionalCheckIn() {
    RegistrationAppTheme {
        OptionalCheckIn(OptionalCheckIn(1, "", false), { index, label ->
        }) {
        }
    }
}