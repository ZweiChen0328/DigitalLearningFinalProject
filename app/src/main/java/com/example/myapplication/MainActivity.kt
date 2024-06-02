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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val index = remember { mutableIntStateOf(0) }
            val temp = remember { mutableStateOf(words.shuffled()) }

            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) { innerPadding ->
                    when (index.intValue) {
                        0 -> {
                            Index(modifier = Modifier.padding(innerPadding), index)
                        }

                        1 -> {
                            WordList(temp = temp, index)
                        }

                        2 -> {
                            Quiz(temp, pageIndex = index)
                        }

                        3 -> {
                            Data(index)
                        }
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
            Text(text = "單字列表")
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
            Button(onClick = { index.intValue = 3 }) {
                Text(text = "單字列表")
            }
        }
    }
}

@Composable
fun Quiz(temp: MutableState<List<Word>>, pageIndex: MutableIntState) {
    val wrongAnswerList = temp.value.drop(10).take(30)
    val textList = remember { wrongAnswerList.chunked(3) }
    val correctPosition = remember { List(10) { Random.nextInt(4) } }
    val selectList = remember { List(10) { mutableIntStateOf(0) } }
    val sc = remember { mutableIntStateOf(0) }
    val cal = remember { mutableStateOf(false) }

    LazyColumn {
        itemsIndexed(textList) { index, it ->
            val list = it.toMutableList()
            list.add(correctPosition[index], temp.value.take(10)[index])
            Text(text = "${index + 1}. ${temp.value.take(10)[index].en}")
            ButtonGroup(answers = list, selectList[index], cal, correctPosition[index])
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    cal.value = true
                    sc.intValue = calculate(temp, correctPosition, selectList)
                }) {
                    Text(text = "送出")
                }
                Button(onClick = {
                    pageIndex.intValue = 1
                }) {
                    Text(text = "回到單字列表")
                }
                Row {
                    Text(text = "分數:")
                    Text(
                        text = "${sc.intValue}", color = if (cal.value) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    )
                    Text(text = "/10")
                }

            }
        }
    }
}

private fun calculate(
    correctAnswerList: MutableState<List<Word>>,
    correctPosition: List<Int>,
    selectList: List<MutableIntState>
): Int {
    var sc = 0
    for (i in 0..9) {
        if (correctPosition[i] == selectList[i].intValue) {
            sc++
            correctAnswerList.value[i].sc += 20
        } else {
            correctAnswerList.value[i].sc -= 20
            if (correctAnswerList.value[i].sc < 0) {
                correctAnswerList.value[i].sc = 0
            }
        }
    }
    return sc
}

@Composable
fun ButtonGroup(
    answers: MutableList<Word>,
    select: MutableIntState,
    cal: MutableState<Boolean>,
    correctPosition: Int
) {
    Row {
        answers.forEachIndexed { index, it ->
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    if (select.intValue == index) {
                        if (cal.value) {
                            if (correctPosition == select.intValue) {
                                Color.Red
                            } else {
                                Color.Green
                            }
                        } else {
                            Color(0xFF0044BB)
                        }
                    } else {
                        Color(0xFFAAAAAA)
                    }
                ),
                onClick = {
                    select.intValue = index
                }
            ) {
                Text(text = it.ch)
            }
        }
    }
}

@Composable
fun Data(index: MutableIntState) {

    Column {
        LazyVerticalGrid(
            modifier = Modifier.weight(1f), columns = GridCells.Fixed(2)
        ) {
            items(words) {
                Text(text = it.en + " " + it.ch + " " + it.sc + "%")
            }
        }
        Button(modifier = Modifier, onClick = { index.intValue = 0 }) {
            Text(text = "回到主畫面")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val index = remember {
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
}


data class Word(
    var en: String,
    var ch: String,
    var sc: Int,
)

val words: List<Word> = listOf(
    Word(en = "head", ch = "頭", sc = 0),
    Word(en = "hair", ch = "頭髮", sc = 0),
    Word(en = "eye", ch = "眼睛", sc = 0),
    Word(en = "nose", ch = "鼻子", sc = 0),
    Word(en = "mouth", ch = "嘴巴", sc = 0),

    Word(en = "ear", ch = "耳朵", sc = 0),
    Word(en = "hand", ch = "手", sc = 0),
    Word(en = "leg", ch = "腿", sc = 0),
    Word(en = "foot", ch = "腳", sc = 0),
    Word(en = "clothing", ch = "服飾", sc = 0),

    Word(en = "hat", ch = "帽子", sc = 0),
    Word(en = "glasses", ch = "眼鏡", sc = 0),
    Word(en = "shirt", ch = "襯衫", sc = 0),
    Word(en = "shorts", ch = "短褲", sc = 0),
    Word(en = "pants", ch = "長褲", sc = 0),

    Word(en = "skirt", ch = "裙子", sc = 0),
    Word(en = "dress", ch = "洋裝", sc = 0),
    Word(en = "socks", ch = "襪子", sc = 0),
    Word(en = "shoes", ch = "鞋子", sc = 0),
    Word(en = "boots", ch = "靴子", sc = 0),

    Word(en = "bear", ch = "熊", sc = 0),
    Word(en = "dog", ch = "狗", sc = 0),
    Word(en = "cat", ch = "貓", sc = 0),
    Word(en = "bird", ch = "鳥", sc = 0),
    Word(en = "rabbit", ch = "兔", sc = 0),

    Word(en = "frog", ch = "青蛙", sc = 0),
    Word(en = "fish", ch = "魚", sc = 0),
    Word(en = "chicken", ch = "雞", sc = 0),
    Word(en = "turtle", ch = "烏龜", sc = 0),
    Word(en = "lion", ch = "獅子", sc = 0),

    Word(en = "tiger", ch = "虎", sc = 0),
    Word(en = "monkey", ch = "猴子", sc = 0),
    Word(en = "giraffe", ch = "長頸鹿", sc = 0),
    Word(en = "fox", ch = "狐狸", sc = 0),
    Word(en = "zebra", ch = "斑馬", sc = 0),

    Word(en = "pig", ch = "豬", sc = 0),
    Word(en = "elephant", ch = "大象", sc = 0),
    Word(en = "pen", ch = "原子筆", sc = 0),
    Word(en = "pencil", ch = "鉛筆", sc = 0),
    Word(en = "marker", ch = "麥克筆", sc = 0),
)

