package com.example.myapplication.wypicieview

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.Kawa
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.myapplication.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NazwaKawyTextField(value: String, onValueChange: (String) -> Unit, kawaList: List<Kawa>) {
    val nazwaKawyList = remember(kawaList) {
        kawaList.map { it.Nazwa }
    }

    var expanded by remember { mutableStateOf(false) }

    val filteredOptions = remember(value, nazwaKawyList) {
        nazwaKawyList.filter { it.contains(value, ignoreCase = true) }
    }

    val defaultIndicatorColor = MaterialTheme.colorScheme.primary // Domyślny kolor linii dla focusu

    val currentIndicatorColor = when {
        value.isEmpty() -> defaultIndicatorColor
        nazwaKawyList.contains(value) -> Color.Green
        else -> Color.Red
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = { Text("Nazwa Kawy") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
             focusedIndicatorColor = currentIndicatorColor,
                unfocusedIndicatorColor = currentIndicatorColor

            ),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable)
        )

        if (expanded && filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { opcja ->
                    DropdownMenuItem(
                        text = { Text(opcja) },
                        onClick = {
                            onValueChange(opcja)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun GramyTextField(value: String, onValueChange: (String) -> Unit) {
    // Walidacja: ciąg cyfr, opcjonalnie przecinek lub kropka i kolejne cyfry
    val regex = remember { Regex("^\\d+([.,]\\d+)?\$") }
    val isValid = regex.matches(value)

    // Pobranie domyślnych kolorów motywu dla stanu pustego, aby uniknąć problemów z fokusem
    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant

    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Waga ziaren") },
        placeholder = { Text("np. 11,8") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun DataTextField(value: String, onValueChange: (String) -> Unit) {
    // Walidacja: ścisły format DD.MM.YYYY
    val regex = remember { Regex("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\$") }
    val isValid = regex.matches(value)

    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant

    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Data wypicia") },
        placeholder = { Text("DD.MM.YYYY") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun MlynekTextField(value: String, onValueChange: (String) -> Unit) {
    val regex = remember{Regex("^\\d+\$")}
    val isValid = regex.matches(value)
    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant
    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Rozmiar młynka") },
        placeholder = { Text("np. 20") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun RatingStars(
    ratingOcena: Int, // Wartość 10..50
    onRatingChange: (Int) -> Unit,
    starSize: Dp = 48.dp,
    activeColor: Color = Color(0xFFFFC107) // Amber Gold
) {
    // Przepływ jednokierunkowy (UDF): nadrzędny komponent zarządza stanem ratingOcena.
    // Konwersja z formatu bazowego (10..50) na format UI (1.0..5.0) dla wyświetlania.
    val displayRating = ratingOcena.toDouble() / 10.0

    var rowWidth by remember { mutableStateOf(0) }

    // Obliczanie oceny na podstawie pozycji dotyku/przeciągnięcia
    fun calculateRating(offset: Offset, width: Int): Int {
        if (width <= 0) return ratingOcena
        val relativeX = offset.x
        // Przesunięcie 0.0-1.0
        val fraction = (relativeX / width).coerceIn(0f, 1f)
        // Skala 0-10 pozycje (0 to 0.5 gwiazdki, 9 to 5 gwiazdek)
        val position = (fraction * 10).toInt().coerceIn(0, 9)
        // Wynik w skali 5..50 (skok co 5, czyli co pół gwiazdki)
        return (position + 1) * 5
    }

    Row(
        modifier = Modifier
            .onGloballyPositioned { rowWidth = it.size.width }
            .pointerInput(Unit) {
                // Obsługa pojedynczego tapnięcia
                detectTapGestures { offset ->
                    onRatingChange(calculateRating(offset, rowWidth))
                }
            }
            .pointerInput(Unit) {
                // Obsługa płynnego przeciągania
                detectHorizontalDragGestures { change, _ ->
                    onRatingChange(calculateRating(change.position, rowWidth))
                }
            }
    ) {
        repeat(5) { index ->
            // Wybór odpowiedniej ikony (pełna, połówka, pusta)
            val starIndex = index + 1
            val icon = when {
                displayRating >= starIndex -> Icons.Filled.Star
                displayRating >= starIndex - 0.5 -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = "Gwiazdka $starIndex",
                tint = if (displayRating >= starIndex - 0.5) activeColor else Color.Gray,
                modifier = Modifier.size(starSize)
            )
        }
    }

}

@Composable
fun TempTextField(value: String, onValueChange: (String) -> Unit) {
    val regex = remember{Regex("^\\d+\$")}
    val isValid = regex.matches(value)
    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant
    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Temperatura") },
        placeholder = { Text("np. 3") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun CisnTextField(value: String, onValueChange: (String) -> Unit) {
    // Walidacja: ciąg cyfr, opcjonalnie przecinek lub kropka i kolejne cyfry
    val regex = remember { Regex("^\\d+([.,]\\d+)?\$") }
    val isValid = regex.matches(value)

    // Pobranie domyślnych kolorów motywu dla stanu pustego, aby uniknąć problemów z fokusem
    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant

    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Ciśnienie (opcjonalne)") },
        placeholder = { Text("np. 9,5") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun CzasTextField(value: String, onValueChange: (String) -> Unit) {
    val regex = remember{Regex("^\\d+\$")}
    val isValid = regex.matches(value)
    val defaultFocused = MaterialTheme.colorScheme.primary
    val defaultUnfocused = MaterialTheme.colorScheme.onSurfaceVariant
    val (focusedLine, unfocusedLine) = when {
        value.isEmpty() -> defaultFocused to defaultUnfocused
        isValid -> Color.Green to Color.Green
        else -> Color.Red to Color.Red
    }
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Czas w sekundach (opcjonalne)") },
        placeholder = { Text("np. 30") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedLine,
            unfocusedIndicatorColor = unfocusedLine
        )
    )
}
@Composable
fun DodajWypicieButton(
    nazwa: String,
    dataValue: String,
    gramy: String,
    mlynek: String,
    temp: String,
    cisn: String,
    czas: String,
    ocena: Int,
    db: AppDatabase,
    snackBarHostState: SnackbarHostState
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            keyboardController?.hide()
            scope.launch(Dispatchers.IO) {
                val wypicieDao = db.wypicieDao()
                if(wypicieDao.getKawaId(nazwa) > 0){
                    snackBarHostState.showSnackbar(
                        message = "Kawa o nazwie '$nazwa' nie istnieje",
                        duration = SnackbarDuration.Short
                    )
                }
                else {
                    val wypicieObj = Wypicie(0, dataValue,gramy,mlynek, ocena, )
                    kawaDao.insertAll(listOf(kawaObj))
                    snackBarHostState.showSnackbar(
                        message = "Dodano kawę $nazwa",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    ){
        Text("Dodaj Kawę")
    }
}