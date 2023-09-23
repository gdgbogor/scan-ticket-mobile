package id.gdev.regist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import id.gdev.regist.ui.theme.RegistrationAppTheme

@Composable
fun LoadingDialog(
    title: String? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = { onDismissRequest?.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = title ?: "Loading ...")
            }
        }
    }
}


@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun PreviewLoadingDialog() {
    RegistrationAppTheme {
        val openAlertDialog = remember { mutableStateOf(false) }
        if (openAlertDialog.value) {
            LoadingDialog { openAlertDialog.value = false }
        }
        Button(onClick = { openAlertDialog.value = !openAlertDialog.value }) {
            Text(text = if (openAlertDialog.value) "Close Dialog" else "Open Dialog")
        }
    }
}
