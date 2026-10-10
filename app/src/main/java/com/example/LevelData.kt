package com.example

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.StormAcidGreen
import com.example.ui.theme.StormBoostYellow
import com.example.ui.theme.StormBrakeRed
import com.example.ui.theme.StormCyan

data class RallyLevel(
    val id: Int,
    val stageNumber: String,
    val name: String,
    val tag: String,
    val laps: String,
    val weather: String,
    val surface: String,
    val grip: String,
    val timeLimit: String,
    val description: String,
    val difficulty: String,
    val accentColor: Color
)

object RallyLevels {
    val allLevels = listOf(
        RallyLevel(
            id = 0,
            stageNumber = "STAGE 01",
            name = "VALLEY CIRCUIT",
            tag = "HIGHLAND SQUALL",
            laps = "2 LAPS",
            weather = "Torrential Rain & Snow Summit",
            surface = "Wet Asphalt & Mud/Snow",
            grip = "Aquaplaning (0.75)",
            timeLimit = "138s",
            description = "The classic Highland pass. Dynamic squall transitions into snow summits with unpredictable grip.",
            difficulty = "NORMAL",
            accentColor = StormCyan
        ),
        RallyLevel(
            id = 1,
            stageNumber = "STAGE 02",
            name = "DESERT TEMPEST",
            tag = "RED ROCK CANYON",
            laps = "3 LAPS",
            weather = "Extreme Heat & Sand Squalls",
            surface = "Loose Gravel & Hardpack Dirt",
            grip = "High Drift Traction (0.82)",
            timeLimit = "160s",
            description = "Sun-baked canyon trails. High speed gravel drifts and blinding dust squalls through rocky gorges.",
            difficulty = "MEDIUM",
            accentColor = StormBoostYellow
        ),
        RallyLevel(
            id = 2,
            stageNumber = "STAGE 03",
            name = "ARCTIC BLIZZARD",
            tag = "FROZEN GLACIER",
            laps = "2 LAPS",
            weather = "Sub-Zero Blizzard & Ice",
            surface = "Packed Snow & Black Ice",
            grip = "Ultra Low Grip (0.42)",
            timeLimit = "150s",
            description = "Glacial mountain passes with treacherous black ice sheets. Counter-steer through blinding whiteouts.",
            difficulty = "HARD",
            accentColor = Color(0xFFA8DADC)
        ),
        RallyLevel(
            id = 3,
            stageNumber = "STAGE 04",
            name = "COASTAL TYPHOON",
            tag = "PACIFIC SQUALL",
            laps = "2 LAPS",
            weather = "Category 4 Coastal Hurricane",
            surface = "Deep Standing Puddles",
            grip = "Severe Aquaplaning (0.35)",
            timeLimit = "145s",
            description = "Gale-force ocean winds and torrential rain. Read every puddle to survive deep water hydroplaning.",
            difficulty = "EXPERT",
            accentColor = StormBrakeRed
        ),
        RallyLevel(
            id = 4,
            stageNumber = "STAGE 05",
            name = "NIGHTFALL SPRINT",
            tag = "NEON HIGHWAY",
            laps = "3 LAPS",
            weather = "Midnight Drizzle & Mist",
            surface = "Slick Tarmac & Clean Line",
            grip = "High Speed Grip (1.05)",
            timeLimit = "130s",
            description = "High speed midnight expressway. Carve dry racing lines and chain turbo boost combos at 300+ km/h.",
            difficulty = "PRO",
            accentColor = StormAcidGreen
        )
    )
}
