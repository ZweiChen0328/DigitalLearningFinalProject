package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var index = remember { mutableIntStateOf(0) }
            val temp = remember { mutableStateOf(words.shuffled()) }

            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) { innerPadding ->
                    if (index.intValue == 0) {
                        Index(modifier = Modifier.padding(innerPadding), index)
                    } else if (index.intValue == 1) {
                        WordList(temp = temp, index)
                    } else if (index.intValue == 2) {
                        Quiz(temp = temp, index)
                    } else if (index.intValue == 3) {
                        Data()
                    }
                }
            }
        }
    }
}

@Composable
fun Index(modifier: Modifier = Modifier, index: MutableIntState) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { index.intValue = 1 }) {
            Text(text = "每日單字")
        }

        Button(onClick = { index.intValue = 3 }) {
            Text(text = "單字分數")
        }
    }
}

@Composable
fun WordList(temp: MutableState<List<Word>>, index: MutableIntState) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                for (i in 0..4) {
                    Text(text = temp.value[i].en + " " + temp.value[i].ch + " " + temp.value[i].sc + "%")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                for (i in 5..9) {
                    Text(text = temp.value[i].en + " " + temp.value[i].ch + " " + temp.value[i].sc + "%")
                }
            }
        }
        Row {
            Button(onClick = {
                temp.value = words.shuffled()
            }) {
                Text(text = "重新產生")
            }
            Button(onClick = { index.intValue = 2 }) {
                Text(text = "測驗")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "測驗紀錄")
            }
        }
    }
}

@Composable
fun Quiz(temp: MutableState<List<Word>>, index: MutableIntState) {

    for (i in 0..9) {
        var wrongAnswerList = temp.value.filter { it.ch != temp.value[i].ch }.toMutableList()
        wrongAnswerList = wrongAnswerList.shuffled().subList(0, 2).toMutableList()

        var answerPosition = Random.nextInt(0,3)

        Column {
            Text(text = "${i + 1}." + temp.value[i].en)

            Row {
                Column {
                }
                Column {

                }
            }
        }
    }
}

@Composable
fun Data() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)
    ) {
        items(words) {
            Text(text = it.en + " " + it.ch + " " + it.sc)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var index = remember {
        mutableIntStateOf(0)
    }
    MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            if (index.value == 0) {
                Index(modifier = Modifier.padding(innerPadding), index)
            } else if (index.value == 1) {
                Index(modifier = Modifier.padding(innerPadding), index)
            }
        }
    }

//    Data()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Data()
}

data class Word(
    var en: String,
    var ch: String,
    var sc: Int,
)

val words: List<Word> = listOf(
    Word(en = "apple", ch = "蘋果", sc = 0),
    Word(en = "apple", ch = "蘋果", sc = 1),
    Word(en = "apple", ch = "蘋果", sc = 2),
    Word(en = "apple", ch = "蘋果", sc = 3),
    Word(en = "apple", ch = "蘋果", sc = 4),
    Word(en = "apple", ch = "蘋果", sc = 5),
    Word(en = "apple", ch = "蘋果", sc = 6),
    Word(en = "apple", ch = "蘋果", sc = 7),
    Word(en = "apple", ch = "蘋果", sc = 8),
    Word(en = "apple", ch = "蘋果", sc = 9),
    Word(en = "apple", ch = "蘋果", sc = 10),
    Word(en = "apple", ch = "蘋果", sc = 11),
    Word(en = "apple", ch = "蘋果", sc = 12),
    Word(en = "apple", ch = "蘋果", sc = 13),
)
