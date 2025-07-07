package com.example.roadcode.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roadcode.R
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PrimaryColor

/* 알고리즘 아이템 버튼 */
@Composable
fun algorithmItem(name: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(300.dp)
            .height(50.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.5.dp, PrimaryColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGrayColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}