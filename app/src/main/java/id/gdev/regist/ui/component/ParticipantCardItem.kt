package id.gdev.regist.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.ui.theme.RegistrationAppTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantCardItem(
    modifier: Modifier = Modifier,
    participant: Participant,
    onClick: ((Participant) -> Unit)? = null
) {
    if (onClick != null) Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onClick.invoke(participant)
        }
    ) {
        ContentParticipantCard(participant = participant)
    } else Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        ContentParticipantCard(participant = participant)
    }
}

@Composable
private fun ContentParticipantCard(participant: Participant) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = participant.header.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = participant.title,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = participant.subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (participant.checkIn == true) Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp),
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = "Icon Check",
            tint = Color(0XFF0DAC55)
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewParticipantCardItem() {
    RegistrationAppTheme {
        ParticipantCardItem(
            participant = Participant(
                "",
                "Abdillah Faiz",
                "GOOGE23693483",
                "Workshop Ticket",
                mapOf(),
                true
            )
        ) {

        }
    }
}