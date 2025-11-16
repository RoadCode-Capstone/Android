package com.example.roadcode.view.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor

@Composable
fun CustomAlertDialog(text: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = text,
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PointColor)
            ) {
                Text(
                    text = "확인",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text(
                    text = "취소",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    )
}