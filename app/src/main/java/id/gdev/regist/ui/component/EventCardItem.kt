package id.gdev.regist.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import id.gdev.regist.domain.model.Event
import id.gdev.regist.ui.theme.RegistrationAppTheme
import id.gdev.regist.utils.TimeUtils.simple
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCardItem(
    modifier: Modifier = Modifier,
    event: Event,
    onClick: (Event) -> Unit
){
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick.invoke(event) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = event.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = event.location)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.time.simple,
                fontWeight = FontWeight.Bold
            )
            Text(text = "${event.totalParticipant} Registered | ${event.checkInParticipant} Check-In")
        }
    }
}

@Preview
@Composable
private fun PreviewEventCardItem(){
    RegistrationAppTheme {
        val calendar = Calendar.getInstance().apply {
            this.set(Calendar.YEAR, 2023)
            this.set(Calendar.MONTH, 7)
            this.set(Calendar.DAY_OF_MONTH, 27)
            this.set(Calendar.HOUR_OF_DAY, 9)
            this.set(Calendar.MINUTE, 0)
        }
        EventCardItem(
            event = Event(
                "123082130",
                "Keras Community Day",
                "Google Indonesia - SCBD, Pacific Century Place Tower Level 43",
                Timestamp(calendar.time),
                0,
                0,
            )
        ){

        }
    }
}