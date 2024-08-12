package com.example.controlset

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlin.math.ceil
import kotlin.math.floor

@SuppressLint("MutableCollectionMutableState", "RememberReturnType")
@Composable
fun ComposeStatisticChartView(data: SnapshotStateList<ArrayList<Int>>) {
    data.sortBy { it[0] }
    val dialogState = remember { mutableStateListOf(false, false) }
    val lableName = remember { mutableStateListOf("x轴", "y轴") }

    if (dialogState[0]) TextDialog(lable = lableName, state = dialogState, key = 0)
    if (dialogState[1]) TextDialog(lable = lableName, state = dialogState, key = 1)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = lableName[1], modifier = Modifier
                .padding(40.dp)
                .clickable { dialogState[1] = true })
            DataChart(data, Offset(500F, 300F))
        }
        Text(text = lableName[0], modifier = Modifier
            .clickable { dialogState[0] = true })
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DataChart(data:SnapshotStateList<ArrayList<Int>>, canvasOffset:Offset) {
    val yRange = mutableStateListOf(
        floor(data.minOf { it[1].toDouble() } / 10).toInt() * 10,
        ceil(data.maxOf { it[1].toDouble() } / 10).toInt() * 10
    )
    val xRange = mutableStateListOf(
        floor(data[0][0].toDouble() / 10).toInt() * 10,
        ceil(data[data.size - 1][0].toDouble() / 10).toInt() * 10
    )
    val xTransition = updateTransition(targetState = xRange, label = "xTransition")
    val yTransition = updateTransition(targetState = yRange, label = "yTransition")
    val dataTransition = updateTransition(targetState = data, label = "dataTransition")

    ConstraintLayout {
        var offsets = List(data.size) { index ->
            dataTransition.animateOffset(
                label = "",
                transitionSpec = { tween(durationMillis = 1000) }) {
                Offset(
                    ((it[index][0].toDouble() - xRange[0]) / (xRange[1] - xRange[0] + 0.1)).toFloat(),
                    ((yRange[1] - it[index][1].toDouble()) / (yRange[1] - yRange[0] + 0.1)).toFloat()
                )
            }.value
        }
        val (column, canvas, row) = createRefs()
        Canvas(
            modifier = Modifier
                .width(canvasOffset.x.dp)
                .height(canvasOffset.y.dp)
                .constrainAs(canvas) {}
        ) {
            this.drawIntoCanvas { canvas ->
                offsets = offsets.map { Offset(it.x * this.size.width, it.y * this.size.height) }
                for (i in offsets.withIndex()) {
                    canvas.nativeCanvas.drawText(
                        "(${data[i.index][0]},${data[i.index][1]})",
                        i.value.x - 60,
                        i.value.y - 30,
                        android.graphics.Paint().apply { textSize = 50F })
                    canvas.nativeCanvas.drawPoint(i.value.x, i.value.y,
                        android.graphics.Paint().apply {
                            strokeWidth = 15F
                            strokeCap = android.graphics.Paint.Cap.ROUND
                        })
                }
                canvas.drawPoints(
                    PointMode.Polygon,
                    offsets,
                    Paint().apply { strokeWidth = 5F })
                for (i in 0..5) {
                    canvas.nativeCanvas.drawLines(
                        floatArrayOf(
                            i * this.size.width / 5, this.size.height, i * this.size.width / 5, 0F,
                            this.size.width, i * this.size.height / 5, 0F, i * this.size.height / 5,
                        ), android.graphics.Paint()
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(canvasOffset.y.dp)
                .constrainAs(column) {
                    end.linkTo(canvas.start)
                }) {
            for (i in 0..5) {
                Text(text = yTransition.animateInt(label = "") {
                    ((5 - i) * it[1] + i * it[0]) / 5
                }.value.toString())
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(canvasOffset.x.dp)
                .constrainAs(row) {
                    top.linkTo(canvas.bottom)
                }) {
            for (i in 0..5) {
                Text(text = xTransition.animateInt(label = "") {
                    ((5 - i) * it[0] + i * it[1]) / 5
                }.value.toString())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDialog(lable: SnapshotStateList<String>, state: SnapshotStateList<Boolean>, key: Int) {
    val temLable = remember { mutableStateOf(lable[key]) }
    AlertDialog(
        onDismissRequest = { state[key] = false },
        confirmButton = {
            Button(onClick = {
                if (temLable.value != "") {
                    lable[key] = temLable.value
                } else {
                    temLable.value = lable[key]
                }
                state[key] = false
            }) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(onClick = {
                state[key] = false
            }) {
                Text(text = "取消")
            }
        },
        text = {
            TextField(
                value = temLable.value,
                onValueChange = { temLable.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        })
}
