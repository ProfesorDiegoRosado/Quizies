package com.example.quizziesclient.model

data class StartGameInputMessage(
    val type: String,
    val categories: List<QuizzCategory>,
    val question: Question
)

data class Question(
    val category: QuizzCategory,
    val question: String,
    val answers: List<String>,
    val rightAnswer: Int
)


data class QuizzCategory(
    val name: String,
    val description: String,
    val color: QuizzColor
) {
    fun getGameCategory(): GameCategory {
        val gameCategory: GameCategory = GameCategory (
            name = name,
            description = description,
            colorHexString = color.hexString
        )
        return gameCategory
    }
}

data class QuizzColor(
    val red: Int,
    val green: Int,
    val blue: Int,
    val hexString: String
)