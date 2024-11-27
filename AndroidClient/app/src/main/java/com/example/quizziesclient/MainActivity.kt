package com.example.quizziesclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizziesclient.ui.theme.QuizziesClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizziesClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        TopLogo()
        AppTitle()
        GameContent()
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
            modifier = modifier.fillMaxWidth()
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
fun GameContent(modifier: Modifier = Modifier) {
    Column()
    {
        RondaInfo(0, modifier)
        GuessedQuestions(modifier)
        QuestionBox(modifier)
    }
}

@Composable
fun RondaInfo(rondaNumber: Int, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Ronda: $rondaNumber"
        )
    }
}

@Composable
fun GuessedQuestions(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    )
    {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text("Preguntas Acertadas", modifier)
            RowOneGuessedQuestion()
            RowTwoGuessedQuestion()
        }
    }
}

@Composable
fun RowOneGuessedQuestion(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(modifier)
        {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                TextBoxWithBgColor("Geología", Color.Blue)
                TextBoxWithBgColor("Deportes", Color.Yellow)
                TextBoxWithBgColor("Ciencias", Color.Green)

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

        }
    }
}

@Composable
fun TextBoxWithBgColor(text: String, bgColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .width(100.dp)
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


@Composable
fun RowTwoGuessedQuestion(modifier: Modifier = Modifier) {
    Row()
    {
    }
}

@Composable
fun QuestionBox(modifier: Modifier = Modifier) {
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
        Greeting("Android")
    }
}