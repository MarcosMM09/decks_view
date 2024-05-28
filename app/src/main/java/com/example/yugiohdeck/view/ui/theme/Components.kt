package com.example.yugiohdeck.view.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.example.yugiohdeck.R
import com.example.yugiohdeck.model.ResponseService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String,align: TextAlign = TextAlign.Center, fontWeight: FontWeight = FontWeight.Bold, color: Color = Color.Black,
                 paddingStart: Dp = 3.dp, paddingEnd: Dp = 3.dp){
    TopAppBar(title = {
        Text(text = title,
            textAlign = align,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = fontWeight,
            color = color
        )
    }, modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(start = paddingStart, end = paddingEnd)
        .shadow(elevation = 10.dp, shape = RectangleShape)
    )
}

@Composable
fun FloatingButton(icon: ImageVector, text: String = "", contentDescription: String? = null,color: Color = Color.Black, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(imageVector = icon, contentDescription = contentDescription) },
        text = { Text(text) },
        containerColor = color
    )
}

@Composable
fun CardViewInfo(
    color: Color = BlueDarkColor,
    painter: AsyncImagePainter,
    cardSet: ResponseService,
    showButton: Boolean = false,
    buttonColor: Color = OrangeBorderColor,
    textAfterClickButton: String = stringResource(id = R.string.selected_to_favorites),
    onAddToFavoritesClicked: (ResponseService) -> Unit
) {
    var buttonPressed by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(width = 4.dp, color = OrangeBorderColor, shape = Shapes().medium),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // Separar las columnas un poco
            ) {
                Text(buildAnnotatedString {
                    withStyle(style = Styles.ParamsStyle.paramsWhite) {
                        append(stringResource(id = R.string.card_name))
                    }
                    withStyle(style = Styles.DataStyle.dataWhite) {
                        append(cardSet.set_name)
                    }
                })
                Text(buildAnnotatedString {
                    withStyle(style = Styles.ParamsStyle.paramsWhite) {
                        append(stringResource(id = R.string.card_code))
                    }
                    withStyle(style = Styles.DataStyle.dataWhite) {
                        append(cardSet.set_code)
                    }
                })
                Text(buildAnnotatedString {
                    withStyle(style = Styles.ParamsStyle.paramsWhite) {
                        append(stringResource(id = R.string.card_numbers))
                    }
                    withStyle(style = Styles.DataStyle.dataWhite) {
                        append(cardSet.num_of_cards.toString())
                    }
                })

                if (showButton) {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!buttonPressed) {
                        ButtonToAdd(onClickButton = {
                            buttonPressed = true
                            onAddToFavoritesClicked(cardSet)
                        }, buttonColor = buttonColor)
                    } else {
                        TextFromButtonClick(text = textAfterClickButton)
                    }
                }
            }

            Image(
                painter = painter,
                contentDescription = "Set Image",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .weight(1f),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }
    }
}


@Composable
fun ButtonToAdd(onClickButton: () -> Unit, text: String = stringResource(id = R.string.select_to_favorites), buttonColor: Color = BlueDarkColor){
    Button(
        onClick = onClickButton,
    ) {
        Text(text = text)
    }
}

@Composable
fun TextFromButtonClick(text: String = stringResource(id = R.string.selected_to_favorites)){
    Spacer(modifier = Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = Color.White)
    }
}
