package com.example.yugiohdeck.view.ui.theme

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.yugiohdeck.view.FavoritesActivity

class TopBarUtils {
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TopAppBarContent(showOptions: Boolean, context: Context) {
            // Estado para controlar la visibilidad del menú flotante
            var expanded by remember { mutableStateOf(false) }
            val title: String = if (showOptions)
                "Yugi Oh! All Cards"
            else
                "Yugi Oh! Favorites Cards"
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
}