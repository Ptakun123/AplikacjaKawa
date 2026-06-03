package com.example.myapplication.ui.wypicieview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.data.roomdatabase.Wypicie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WypicieViewModel(private val db: AppDatabase) : ViewModel() {

    // Lista z bazy danych
    val kawaList = db.kawaDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val wypicieList = db.wypicieDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var nazwa by  mutableStateOf("")
    var gramy by mutableStateOf("")
    val formatter =  SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply{
        isLenient = false
    }
    var dataValue by  mutableStateOf(formatter.format(Date()))
    var mlynek by mutableStateOf("")
    var temp by mutableStateOf("")
    var cisn by mutableStateOf("")
    var czas by mutableStateOf("")
    var ocena by mutableStateOf(0)

    // Narzędzia do walidacji (zainicjowane raz)
    private val regexZmiennoprzecinkowa = Regex("^\\d+([.,]\\d+)?$")
    private val regexCalkowita = Regex("^\\d+$")
    private val dataRegex =  Regex("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\$")

    // Walidacja (odświeża się sama)
    val nazwaReady: Boolean get() = kawaList.value.any { it.Nazwa.equals(nazwa, ignoreCase = false) }
    val gramyReady: Boolean get() = regexZmiennoprzecinkowa.matches(gramy)
    val dataReady: Boolean get() = dataRegex.matches(dataValue)
    val mlynekReady: Boolean get() = regexCalkowita.matches(mlynek)
    val tempReady: Boolean get() = regexCalkowita.matches(temp)
    val cisnReady: Boolean get() = regexZmiennoprzecinkowa.matches(cisn) || cisn.isEmpty()
    val czasReady: Boolean get() = regexCalkowita.matches(czas) || czas.isEmpty()

    val ready: Boolean get() = nazwaReady && gramyReady && dataReady && mlynekReady && tempReady && cisnReady && czasReady

    private val eventChannel = Channel<UiEventWypicie>()
    val uiEvents = eventChannel.receiveAsFlow()

    // Funkcja do zapisu
    fun zapiszWypicie() {
        if(!ready) return
        viewModelScope.launch(Dispatchers.IO) {
            val wypicieDao = db.wypicieDao()
            val kawaId = wypicieDao.getKawaId(nazwa)
            if(kawaId <= 0) eventChannel.send(UiEventWypicie.ShowSnackbar("Kawa o nazwie $nazwa nie istnieje"))
            else {
                try {
                    val wypicieObj = Wypicie(
                        0,
                        dataValue,
                        gramy.replace(',', '.').toDoubleOrNull(),
                        mlynek.toInt(),
                        temp.toIntOrNull(),
                        cisn.replace(',', '.').toDoubleOrNull(),
                        czas.toIntOrNull(),
                        ocena,
                        kawaId
                    )
                    wypicieDao.insertAll(listOf(wypicieObj))
                    eventChannel.send(UiEventWypicie.ShowSnackbar("Pomyślnie dodano wypicie"))
                    nazwa = ""
                    gramy = ""
                    mlynek = ""
                    temp = ""
                    cisn = ""
                    czas = ""
                    ocena = 0


                } catch (e: Exception){
                    eventChannel.send(UiEventWypicie.ShowSnackbar("Błąd podczas zapisu ${e.message}"))
                }
            }
        }

    }
}

// Fabryka, żeby Android umiał stworzyć ViewModel z bazą danych
class WypicieViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WypicieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WypicieViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
sealed interface UiEventWypicie {
    data class ShowSnackbar(val message: String) : UiEventWypicie
    object NavigateBack : UiEventWypicie // Na przyszłość, np. żeby wyjść z ekranu po zapisie
}