package com.example.myapplication.ui.kawaview

import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.myapplication.data.roomdatabase.Kawa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KrajTextField(value: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val kraje = remember { listOf("Etiopia", "Brazylia", "Kolumbia", "Indie", "Wietnam") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Używamy 'it', aby system sam decydował o stanie
    ) {
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Kraj") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // menuAnchor musi być tutaj, aby TextField wiedział, że ma menu
            modifier = Modifier.menuAnchor()
        )

        // ExposedDropdownMenu automatycznie dopasowuje szerokość i zachowanie do TextField
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            kraje.forEach { opcja ->
                DropdownMenuItem(
                    text = { Text(opcja) },
                    onClick = {
                        onValueChange(opcja)
                        expanded = false // Zamykamy po wyborze
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
@Composable
fun NazwaTextField(value: String, onValueChange: (String) -> Unit){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text("Nazwa Kawy")},
        placeholder = {Text("np. 123")}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalenieTextField(value: String, onValueChange: (String) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val palenia = remember { listOf("Light Roast", "Medium Roast", "Dark Roast") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Używamy 'it', aby system sam decydował o stanie
    ) {
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Rodzaj Palenia") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // menuAnchor musi być tutaj, aby TextField wiedział, że ma menu
            modifier = Modifier.menuAnchor()
        )

        // ExposedDropdownMenu automatycznie dopasowuje szerokość i zachowanie do TextField
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            palenia.forEach { opcja ->
                DropdownMenuItem(
                    text = { Text(opcja) },
                    onClick = {
                        onValueChange(opcja)
                        expanded = false // Zamykamy po wyborze
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GatunekTextField(value: String, onValueChange: (String) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val kraje = remember { listOf("100% Arabica", "Mieszanka (Blend)", "Robusta") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Używamy 'it', aby system sam decydował o stanie
    ) {
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Gatunek") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // menuAnchor musi być tutaj, aby TextField wiedział, że ma menu
            modifier = Modifier.menuAnchor()
        )

        // ExposedDropdownMenu automatycznie dopasowuje szerokość i zachowanie do TextField
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            kraje.forEach { opcja ->
                DropdownMenuItem(
                    text = { Text(opcja) },
                    onClick = {
                        onValueChange(opcja)
                        expanded = false // Zamykamy po wyborze
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
@Composable
fun OpisTextField(value: String, onValueChange: (String) -> Unit){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text("Dodatkowy opis")},
        placeholder = {Text("Dodaj opis")}
    )
}
@Composable
fun DodajKaweButton(
    onClick: () -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            keyboardController?.hide()
            onClick()
        }
    ){
        Text("Dodaj Kawę")
    }
}
@Composable
fun ListaKawText(kawaList: List<Kawa>){
    val text: String
    if(kawaList.isEmpty()) text = "Brak Kaw w bazie"
    else text =
        kawaList.joinToString() {
                kawa -> "Nazwa: ${kawa.Nazwa}, Kraj:${kawa.Kraj}, Palenie: ${kawa.Palenie}, Gatunek: ${kawa.Gatunek}, Opis: ${kawa.Opis} \n"
        }
    Text(text)
}
@Composable
fun GoToWypicieButton(onNavigateToWypicie: () -> Unit){
    Button(onClick =
        {onNavigateToWypicie()}){
        Text("Dodaj Wypicie")
    }
}


