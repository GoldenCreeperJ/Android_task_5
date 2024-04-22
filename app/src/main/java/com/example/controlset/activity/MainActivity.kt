package com.example.controlset.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.example.controlset.ComposeStatisticChartView
import com.example.controlset.ui.theme.ControlSetTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a= mutableStateListOf(arrayListOf(1,2),arrayListOf(3,4))
        setContent {
            ControlSetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row{
                        Column {
                            Button(
                                onClick = { startActivity(Intent(this@MainActivity, OtherActivity::class.java)) }
                            ) {
                                Text(text = "RecycleViews")
                            }
                            Button(onClick = {
                                a.add(arrayListOf(
                                Random.nextInt(0,100),
                                Random.nextInt(0,100))) }
                            ) {
                                Text(text = "Random Add")
                            }
                            Button(onClick = {
                                if (a.size>1)
                                    a.removeAt(Random.nextInt(0,a.size)) }
                            ) {
                                Text(text = "Random Del")
                            }
                        }
                        ComposeStatisticChartView(a)
                    }
                }
            }
        }
    }
}
