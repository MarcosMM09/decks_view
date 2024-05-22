package com.example.yugiohdeck.view.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class Styles {
    object ParamsStyle{
        val paramsWhite = SpanStyle(
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }

    object DataStyle{
        val dataWhite = SpanStyle(
            color = Color.White,
            fontSize = 15.sp,
        )
    }
}