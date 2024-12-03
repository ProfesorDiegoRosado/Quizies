package com.example.quizziesclient.model

import androidx.compose.ui.graphics.Color


data class GameCategory(
    val name: String,
    val description: String,
    val colorHexString: String,
    val rightCount: Int = 0
) {
    fun getColor(): Color {
        return Color(android.graphics.Color.parseColor(colorHexString))
    }
}

/*
data class GameQuestion(
    val question: String,
    val answers: List<String>,
    val rightAnswer: Int
)
 */

enum class AnswerState {
    NOT_ANSWERED, RIGHT, WRONG
}

