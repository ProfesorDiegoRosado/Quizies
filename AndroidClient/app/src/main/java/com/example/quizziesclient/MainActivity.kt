package com.example.quizziesclient

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizziesclient.model.GameCategory
import com.example.quizziesclient.model.GameViewModel
import com.example.quizziesclient.model.Question
import com.example.quizziesclient.ui.GameUiState
import com.example.quizziesclient.ui.theme.QuizziesClientTheme
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.system.exitProcess


class MainActivity : ComponentActivity() {


    //private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Info:", "Starting Quizzies app ...")

        val gameViewModel: GameViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            QuizziesClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        gameViewModel = gameViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Send GameStart event to get categories and colors
        gameViewModel.sendStartEvent()

    }

    private fun applySchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

}


@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameUiState: GameUiState by gameViewModel.uiState.collectAsState()

    if (gameUiState.gameDone) {
        GameDoneDialog()
    }

    Column(
        modifier = modifier
    ) {
        TopLogo()
        AppTitle()
        GameContent(gameUiState, gameViewModel)
        Copyright()
    }
}

@Composable
fun TopLogo(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val originalWidth = 860
        val originalHeight = 646
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.size((originalWidth * 0.1f).dp, (originalHeight * 0.1f).dp)
        )
    }

}

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = "Quizzies"
            )
        }
    }
}

@Composable
fun GameContent(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column()
    {
        val categoryNameList = gameUiState.gameCategories.map { it.name }
        val categoryGuessedCountMap: SnapshotStateMap<String, Int> = remember {
            mutableStateMapOf(*categoryNameList.map { it to 0}.toTypedArray())
        }

        RondaInfo(roundNumber = gameUiState.round, modifier = modifier)
        GuessedQuestions(gameUiState = gameUiState, categoryGuessedCountMap, modifier = modifier)
        QuestionBox(
            gameUiState = gameUiState,
            categoryGuessedCountMap = categoryGuessedCountMap,
            gameViewModel = gameViewModel,
            modifier = modifier)
    }
}

@Composable
fun RondaInfo(roundNumber: Int, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Ronda: $roundNumber"
        )
    }
}

@Composable
fun GuessedQuestions(gameUiState: GameUiState, categoryGuessedCountMap: SnapshotStateMap<String, Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text("Preguntas Acertadas", modifier)
            if (gameUiState.gameCategories.size==6) {
                RowOneGuessedQuestion(gameUiState.gameCategories.subList(0,3), categoryGuessedCountMap)
                RowOneGuessedQuestion(gameUiState.gameCategories.subList(3,6), categoryGuessedCountMap)
            }
        }
    }
}

@Composable
fun RowOneGuessedQuestion(categories: List<GameCategory>, categoryGuessedCountMap: SnapshotStateMap<String, Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            GuessedCategoryColumn(category = categories[0], categoryGuessedCountMap = categoryGuessedCountMap, modifier = modifier)
            GuessedCategoryColumn(category = categories[1], categoryGuessedCountMap = categoryGuessedCountMap, modifier = modifier)
            GuessedCategoryColumn(category = categories[2], categoryGuessedCountMap = categoryGuessedCountMap, modifier = modifier)
        }
    }
}

@Composable
fun GuessedCategoryColumn(
    category: GameCategory,
    categoryGuessedCountMap: SnapshotStateMap<String, Int>,
    modifier: Modifier
) {
    val rightCount = categoryGuessedCountMap.toMap().getOrDefault(category.name, 0)
    val bgColor: Color = if (rightCount<3) Color.White else Color.DarkGray
    Column(
        modifier
            .padding(2.dp)
            .background(bgColor, shape = RoundedCornerShape(2.dp)),
        verticalArrangement = Arrangement.SpaceBetween,
    )
    {
        TextBoxWithBgColor(category.name, category.getColor())
        CategoryGuessedQuestionIndicator(category, categoryGuessedCountMap, 0)
        CategoryGuessedQuestionIndicator(category, categoryGuessedCountMap, 1)
        CategoryGuessedQuestionIndicator(category, categoryGuessedCountMap, 2)
    }
}

@Composable
fun CategoryGuessedQuestionIndicator(category: GameCategory, categoryGuessedCountMap: SnapshotStateMap<String, Int>, position: Int) {
    if (categoryGuessedCountMap.getOrElse(category.name, { 0 }) > position) {
        TextBoxWithBgColor("", category.getColor())
    } else {
        TextBoxWithBgColor("", Color.LightGray)
    }
}

@Composable
fun TextBoxWithBgColor(text: String, bgColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .width(126.dp)
            .padding(2.dp)
    )
    {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}


@Composable
fun QuestionBox(
    gameUiState: GameUiState,
    categoryGuessedCountMap: SnapshotStateMap<String, Int>,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    )
    {
        val question = gameUiState.currentQuestion
        if (question!=null) {
            val questionString = question.question
            val answersString = question.answers

            Row(
                modifier = modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(
                    "Pregunta",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                TextBoxWithBgColor(
                    text = question.category.name,
                    bgColor = question.category.getGameCategory().getColor(),
                    modifier = modifier
                )
            }

            Text(
                questionString,
                modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            // Answer options
            for (i in 0..3) {
                ClickableAnswer(
                    answerString = answersString[i],
                    modifier = modifier,
                    gameViewModel = gameViewModel,
                    question = question,
                    categoryGuessedCountMap = categoryGuessedCountMap,
                    answerPosition = i
                )
            }

        }
    }
}

@Composable
fun ClickableAnswer(
    answerString: String,
    modifier: Modifier,
    gameViewModel: GameViewModel,
    question: Question,
    categoryGuessedCountMap: SnapshotStateMap<String, Int>,
    answerPosition: Int
) {
    val context: Context = LocalContext.current
    Text(
        answerString,
        modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable (
                onClick = {
                    onAnswerClick(
                        gameViewModel = gameViewModel,
                        question = question,
                        categoryGuessedCountMap = categoryGuessedCountMap,
                        answerPosition = answerPosition,
                        context = context
                    )
                }
            ),
        textAlign = TextAlign.Center
    )
}

fun onAnswerClick(
    gameViewModel: GameViewModel,
    question: Question,
    categoryGuessedCountMap: SnapshotStateMap<String, Int>,
    answerPosition: Int,
    context: Context
) {
    val rightAnswer = question.rightAnswer
    val categoryName = question.category.name
    val categoryRightAnswersCount: Int = categoryGuessedCountMap.getOrElse(categoryName, {-1})

    // Update guessed answer by category
    if (rightAnswer==answerPosition) {
        categoryGuessedCountMap[categoryName] = categoryRightAnswersCount + 1
    }

    // Inc round
    gameViewModel.incRound()

    // Toast Right/Wrong answer choice message and category done
    val categoryDone = categoryGuessedCountMap.toMap().filter { (_, v) -> v >= 3 }.keys.toList().contains(categoryName)
    toastRightWrong(
        right = rightAnswer==answerPosition,
        categoryDoneName = if (categoryDone) categoryName else "",
        context
    )

    // Check end game
    val filteredCategoryNames = categoryGuessedCountMap.toMap().filter { (_, v) -> v < 3 }.keys.toList()
    if (filteredCategoryNames.isEmpty()) {
        // Game Done
        gameViewModel.gameDone()
    } else {
        // Load next question
        gameViewModel.loadNextQuestionEvent(filteredCategoryNames)
    }

}

private fun toastRightWrong(
    right: Boolean,
    categoryDoneName: String,
    context: Context
) {
    val rightWrongMessage: String = if (right) "Correcto" else "Incorrecto"
    val message = if (categoryDoneName.equals("")) {
        rightWrongMessage
    } else {
        "$rightWrongMessage\nCategoria $categoryDoneName completa"
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun GameDoneDialog() {
    Dialog(
        onDismissRequest = { exitProcess(status = -1) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "¡¡¡ Enhorabuena !!!\nHas terminado el juego",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
           TextButton(
               onClick = { exitProcess(status = -1) },
               modifier = Modifier.padding(8.dp),
           ) {
               Text("Cerrar")
           }
        }

    }
}



@Composable
fun Copyright(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "© 2024 Quizzies",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Center

        )
        Text(
            text = "Autor: Diego Rosado",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Logo Designed by Freepik",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizziesClientTheme {
        GameScreen(gameViewModel = GameViewModel())
    }
}

/*
@Composable
fun SVGImage(context: Context, svgResId: Int) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Parse the SVG file
        val svg = SVG.getFromResource(context, svgResId)
        val canvas = drawContext.canvas.nativeCanvas

        // Set the viewBox size for the SVG
        val width = size.width
        val height = size.height
        svg.setDocumentWidth(width.toString())
        svg.setDocumentHeight(height.toString())

        // Render the SVG on the native canvas
        svg.renderToCanvas(canvas)
    }
}
 */