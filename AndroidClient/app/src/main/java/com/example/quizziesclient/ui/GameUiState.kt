package com.example.quizziesclient.ui

import com.example.quizziesclient.model.GameCategory
import com.example.quizziesclient.model.Question


data class GameUiState (
    val round: Int = 0,
    val gameCategories: List<GameCategory> = listOf(),
    val currentQuestion: Question? = null,
    val gameDone: Boolean = false
) {

}