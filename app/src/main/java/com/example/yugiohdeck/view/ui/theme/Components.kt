package com.example.yugiohdeck.view.ui.theme

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yugiohdeck.R
import com.example.yugiohdeck.view.FavoritesActivity

class Components {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleTopBar(title: String,align: TextAlign = TextAlign.Center, fontWeight: FontWeight = FontWeight.Bold, color: Color = Color.Black){
        TopAppBar(title = {
            Text(text = title,
                textAlign = align,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = fontWeight,
                color = color
            )
        }, modifier = Modifier.fillMaxWidth())
    }

    @Composable
    fun FloatingButton(
        icon: ImageVector,
        text: String = "",
        contentDescription: String? = null,
        onClick: () -> Unit
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = { Icon(imageVector = icon, contentDescription = contentDescription) },
            text = { Text(text) }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarContent(showOptions: Boolean = false, context: Context) {
        // Estado para controlar la visibilidad del menú flotante
        var expanded by remember { mutableStateOf(false) }
        val title: String = if (showOptions)
            stringResource(id = R.string.title_topbar_main)
        else
            stringResource(id = R.string.title_topbar_favorites)
        TopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
                },
                actions = {
                    if(showOptions){
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            DropdownMenuItem(text = {
                                Text(text = "Favoritos")
                            }, onClick = {
                                expanded = false
                                val intent = Intent(context, FavoritesActivity::class.java)
                                context.startActivity(intent)
                            })

                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

}