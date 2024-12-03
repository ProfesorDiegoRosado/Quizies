package com.example.quizziesclient

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizziesclient.model.GameCategory
import com.example.quizziesclient.model.GameViewModel
import com.example.quizziesclient.model.Question
import com.example.quizziesclient.model.StartGameInputMessage
import com.example.quizziesclient.ui.GameUiState
import com.example.quizziesclient.ui.theme.QuizziesClientTheme
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage




class MainActivity : ComponentActivity() {


    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Info:", "Starting Quizzies app ...")

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


        // ...
        //Thread.sleep(1000)
        gameViewModel.subscribeToMessages()

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
    gameViewModel: GameViewModel = GameViewModel(),
    modifier: Modifier = Modifier
) {
    val gameUiState: GameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
    ) {
        TopLogo()
        AppTitle()
        GameContent(gameUiState)
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
                textAlign = TextAlign.Center,
                text = "Quizzies"
            )
        }
    }
}

@Composable
fun GameContent(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column()
    {
        RondaInfo(roundNumber = gameUiState.round, modifier = modifier)
        GuessedQuestions(gameUiState = gameUiState, modifier = modifier)
        QuestionBox(question = gameUiState.currentQuestion, modifier = modifier)
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
fun GuessedQuestions(gameUiState: GameUiState, modifier: Modifier = Modifier) {
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
                RowOneGuessedQuestion(gameUiState.gameCategories.subList(0,3))
                RowOneGuessedQuestion(gameUiState.gameCategories.subList(3,6))
            }
        }
    }
}

@Composable
fun RowOneGuessedQuestion(categories: List<GameCategory>, modifier: Modifier = Modifier) {
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
        val cat0 = categories[0]
        val cat1 = categories[1]
        val cat2 = categories[2]
        Column(modifier)
        {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                TextBoxWithBgColor(cat0.name, cat0.getColor())
                TextBoxWithBgColor(cat1.name, cat1.getColor())
                TextBoxWithBgColor(cat2.name, cat2.getColor())
            }
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)

            }
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)

            }
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)
                TextBoxWithBgColor("", Color.LightGray)

            }
            /*
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            {
                ImageVector.vectorResource(id = R.drawable.quesito_2)
             */
                /*
                Image(
                    painter = painterResource(id = R.drawable.quesito_2)

                )
                 */
            //Icon(imageVector = ImageVector.vectorResource(R.drawable.quesito_2))
                //SVGImage(LocalContext.current, R.drawable.quesito_2)

            //}
            /*
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                SVGImage(LocalContext.current, R.drawable.quesito_2)
                SVGImage(LocalContext.current, R.drawable.quesito_2)
                SVGImage(LocalContext.current, R.drawable.quesito_2)
            }

             */

        }
    }
}

@Composable
fun TextBoxWithBgColor(text: String, bgColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .width(120.dp)
            //.fillMaxWidth(0.33f)
            //.background(bgColor)
    )
    {
        Text(
            text = text,
            //fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                //.shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp),clip = false)
        )
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

@Composable
fun QuestionBox(question: Question?, modifier: Modifier = Modifier) {
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
        Text(
            "Pregunta",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            "¿Aqui va la pregunta? Pues claro que si",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        // Las cuatro opciones de respuesta
        Text(
            "Opcion 1",
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            "Opcion 2",
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            "Opcion 3",
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            "Opcion 4",
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                ),
            textAlign = TextAlign.Center
        )
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizziesClientTheme {
        GameScreen()
    }
}