package com.example.a23012011147_practicalexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // basic Material3 theme
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF3F4FF) // light purple / grey background
                ) {
                    TechConferenceScreen()
                }
            }
        }
    }
}

// ----------------- DATA MODELS -----------------

data class Speaker(
    val name: String,
    val title: String
)

enum class SessionType(val label: String) {
    ALL("ALL"),
    KEYNOTE("KEYNOTE"),
    WORKSHOP("WORKSHOP"),
    NETWORKING("NETWORKING")
}

data class Session(
    val time: String,
    val title: String,
    val type: SessionType,
    val description: String,
    val room: String
)

data class Review(
    val name: String,
    val stars: Int,
    val comment: String
)

// sample static data (like in video)
private val speakers = listOf(
    Speaker("Dr. Emily Chen", "AI Researcher"),
    Speaker("Jake Wharton", "Android GDE"),
    Speaker("Sarah Connor", "Security Expert"),

    // 🔽 extra featured speakers added
    Speaker("Alice Johnson", "Cloud Architect"),
    Speaker("Bob Smith", "Kotlin Developer"),
    Speaker("Charlie Davis", "UI/UX Designer")
)

private val sessions = listOf(
    Session(
        "9:00 AM",
        "Opening Ceremony",
        SessionType.ALL,
        "Kick-off session with conference overview and welcome note.",
        "Main Hall"
    ),
    Session(
        "10:00 AM",
        "The Future of AI",
        SessionType.KEYNOTE,
        "Keynote talk about the latest trends in Artificial Intelligence.",
        "Room 201 - North Wing"
    ),
    Session(
        "11:30 AM",
        "Kotlin Multiplatform",
        SessionType.WORKSHOP,
        "Hands-on workshop for mobile developers sharing Kotlin code.",
        "Room 304 - East Wing"
    ),
    Session(
        "1:00 PM",
        "Lunch & Connect",
        SessionType.NETWORKING,
        "Gourmet lunch and networking opportunity with speakers.",
        "Cafeteria"
    ),
    Session(
        "2:30 PM",
        "Cloud Scalability",
        SessionType.WORKSHOP,
        "Deep dive into scalable architectures on the cloud.",
        "Room 305 - East Wing"
    )
)

private val reviews = listOf(
    Review("Alice Johnson", 5, "Great event! Well-organized."),
    Review("Bob Smith", 4, "Loved the keynote!"),
    Review("Charlie Davis", 3, "Workshops were crowded.")
)

// ----------------- MAIN SCREEN -----------------

@Composable
fun TechConferenceScreen() {
    var selectedFilter by remember { mutableStateOf(SessionType.ALL) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.conference_banner),
            // if your file name is different, change here ↑
            contentDescription = "Conference photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        HeaderCard()

        Spacer(Modifier.height(16.dp))

        SpeakersSection()

        Spacer(Modifier.height(16.dp))

        ScheduleFilterRow(
            selected = selectedFilter,
            onSelectedChange = { selectedFilter = it }
        )

        Spacer(Modifier.height(8.dp))

        val filteredSessions = if (selectedFilter == SessionType.ALL) {
            sessions
        } else {
            sessions.filter { it.type == selectedFilter }
        }

        filteredSessions.forEach { session ->
            SessionCard(session = session)
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(16.dp))

        ReviewsSection()

        Spacer(Modifier.height(24.dp))

        BottomButtons()
    }
}

// ----------------- HEADER -----------------

@Composable
fun HeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827) // dark background like video
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Tech Conference 2025",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Mehsana, Gujarat | 2.5 km away",
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Join us for a deep dive into the future of technology.",
                color = Color(0xFFE5E7EB),
                fontSize = 13.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Live Updates Active",
                    color = Color(0xFFBBF7D0),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ----------------- SPEAKERS -----------------

@Composable
fun SpeakersSection() {
    Column {
        Text(
            text = "Featured Speakers",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(8.dp))

        // 🔽 horizontally scrollable row of speakers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            speakers.forEach { speaker ->
                SpeakerCard(speaker)
            }
        }
    }
}

@Composable
fun SpeakerCard(speaker: Speaker) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE082)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = speaker.name.first().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = speaker.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = speaker.title,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ----------------- SCHEDULE FILTER -----------------

@Composable
fun ScheduleFilterRow(
    selected: SessionType,
    onSelectedChange: (SessionType) -> Unit
) {
    Column {
        Text(
            text = "Schedule",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SessionType.values().forEach { type ->
                val isSelected = type == selected
                FilterChip(
                    label = type.label,
                    selected = isSelected,
                    onClick = { onSelectedChange(type) }
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0xFF4F46E5) else Color.Transparent
    val textColor = if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF4F46E5) else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .background(bg, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 12.sp, color = textColor)
    }
}

// ----------------- SESSION CARD -----------------

@Composable
fun SessionCard(session: Session) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = session.time,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF4F46E5)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = session.type.label,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = session.description,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Room: ${session.room}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ----------------- REVIEWS -----------------

@Composable
fun ReviewsSection() {
    Column {
        Text(
            text = "Reviews",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(8.dp))

        reviews.forEach { review ->
            ReviewCard(review)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE082)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.name.first().toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = review.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = "★".repeat(review.stars),
                    fontSize = 13.sp,
                    color = Color(0xFFF59E0B)
                )
                Text(
                    text = review.comment,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ----------------- BOTTOM BUTTONS -----------------

@Composable
fun BottomButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { /* TODO: open ticket link */ },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White
            )
        ) {
            Text(text = "Buy Tickets", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = { /* TODO: open calendar */ },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Calendar", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TechConferencePreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF3F4FF)
        ) {
            TechConferenceScreen()
        }
    }
}