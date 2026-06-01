package com.example.soundscape.screen.stats

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soundscape.domain.model.Artist
import com.example.soundscape.viewmodel.StatsViewModel
import kotlinx.coroutines.launch

// ─── Culori pentru grafice ────────────────────────────────────────────────────
private val ChartColors = listOf(
    Color(0xFF6650A4),
    Color(0xFFE91E63),
    Color(0xFF2196F3),
    Color(0xFF4CAF50),
    Color(0xFFFF9800)
)

private fun formatNumber(value: String): String {
    val n = value.toLongOrNull() ?: return value
    return when {
        n >= 1_000_000_000 -> "%.1fB".format(n / 1_000_000_000.0)
        n >= 1_000_000     -> "%.1fM".format(n / 1_000_000.0)
        n >= 1_000         -> "%.1fK".format(n / 1_000.0)
        else               -> n.toString()
    }
}

// ─── Screen principal ─────────────────────────────────────────────────────────
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ── Header ──────────────────────────────────────────
        item {
            Column {
                Text(
                    text = "Your Statistics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Insights about your music taste",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ── Stat cards ───────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatHeroCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Favorite,
                    label = "Favorites",
                    value = uiState.totalFavorites.toString(),
                    accentColor = Color(0xFFE91E63)
                )
                StatHeroCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.TrendingUp,
                    label = "Total Plays",
                    value = formatNumber(uiState.totalPlaycount.toString()),
                    accentColor = Color(0xFF6650A4)
                )
                StatHeroCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star,
                    label = "Avg Plays",
                    value = formatNumber(uiState.avgPlaycount.toString()),
                    accentColor = Color(0xFF2196F3)
                )
            }
        }

        // ── Bar chart: Top 5 by Playcount ───────────────────
        if (uiState.topByPlaycount.isNotEmpty()) {
            item {
                SectionCard(title = "Top Artists by Playcount") {
                    BarChart(artists = uiState.topByPlaycount, valueSelector = { it.playcount })
                }
            }
        }

        // ── Bar chart: Top 5 by Listeners ───────────────────
        if (uiState.topByListeners.isNotEmpty()) {
            item {
                SectionCard(title = "Top Artists by Listeners") {
                    BarChart(artists = uiState.topByListeners, valueSelector = { it.listeners })
                }
            }
        }

        // ── Donut chart ─────────────────────────────────────
        if (uiState.topByPlaycount.size >= 2) {
            item {
                SectionCard(title = "Playcount Distribution") {
                    DonutChart(artists = uiState.topByPlaycount)
                }
            }
        }

        // ── Ranking list ────────────────────────────────────
        if (uiState.allFavorites.isNotEmpty()) {
            item {
                SectionCard(title = "All Favorites Ranked") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val sorted = uiState.allFavorites
                            .sortedByDescending { it.playcount.toLongOrNull() ?: 0L }
                        sorted.forEachIndexed { index, artist ->
                            RankRow(rank = index + 1, artist = artist)
                        }
                    }
                }
            }
        }

        // ── Empty state ──────────────────────────────────────
        if (uiState.totalFavorites == 0) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💿", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No favorites yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Add artists to your favorites to see stats here",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ─── Componente ───────────────────────────────────────────────────────────────

@Composable
private fun StatHeroCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            content()
        }
    }
}

// ─── Bar Chart ────────────────────────────────────────────────────────────────
@Composable
private fun BarChart(
    artists: List<Artist>,
    valueSelector: (Artist) -> String
) {
    val maxValue = artists.maxOfOrNull { valueSelector(it).toLongOrNull() ?: 0L } ?: 1L

    // Animatie de intrare
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(artists) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(900, easing = LinearOutSlowInEasing))
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        artists.forEachIndexed { index, artist ->
            val value = valueSelector(artist).toLongOrNull() ?: 0L
            val fraction = if (maxValue > 0) (value.toFloat() / maxValue) * animProgress.value else 0f
            val color = ChartColors[index % ChartColors.size]

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Rank badge
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                // Bar + label
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = artist.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = formatNumber(valueSelector(artist)),
                            style = MaterialTheme.typography.labelSmall,
                            color = color,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(color, color.copy(alpha = 0.7f))
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

// ─── Donut Chart ──────────────────────────────────────────────────────────────
@Composable
private fun DonutChart(artists: List<Artist>) {
    val total = artists.sumOf { it.playcount.toLongOrNull() ?: 0L }.toFloat()
    if (total == 0f) return

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(artists) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(1000, easing = LinearOutSlowInEasing))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Canvas donut
        Canvas(
            modifier = Modifier
                .size(140.dp)
                .padding(8.dp)
        ) {
            var startAngle = -90f
            val strokeWidth = size.minDimension * 0.18f
            val radius = (size.minDimension - strokeWidth) / 2f
            val topLeft = Offset(
                (size.width - radius * 2) / 2f,
                (size.height - radius * 2) / 2f
            )
            val arcSize = Size(radius * 2f, radius * 2f)

            artists.forEachIndexed { index, artist ->
                val value = artist.playcount.toLongOrNull() ?: 0L
                val sweep = (value / total) * 360f * animProgress.value
                val color = ChartColors[index % ChartColors.size]
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep - 2f, // gap between segments
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                startAngle += sweep
            }
        }

        // Legend
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            artists.forEachIndexed { index, artist ->
                val value = artist.playcount.toLongOrNull() ?: 0L
                val pct = if (total > 0) (value / total * 100).toInt() else 0
                val color = ChartColors[index % ChartColors.size]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Text(
                        text = artist.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 100.dp)
                    )
                    Text(
                        text = "$pct%",
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─── Rank Row ─────────────────────────────────────────────────────────────────
@Composable
private fun RankRow(rank: Int, artist: Artist) {
    val accentColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Rank badge
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = if (rank <= 3) 0.2f else 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (rank <= 3) listOf("🥇", "🥈", "🥉")[rank - 1] else "#$rank",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }

        // Artist info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${formatNumber(artist.listeners)} listeners",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Playcount badge
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        ) {
            Text(
                text = formatNumber(artist.playcount),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}