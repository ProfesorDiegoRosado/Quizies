package com.example.quizziesclient.model

import androidx.compose.ui.graphics.Color


data class GameCategory(
    val name: String,
    val description: String,
    val colorHexString: String,
) {
    fun getColor(): Color {
        return Color(android.graphics.Color.parseColor(colorHexString))
    }
}
