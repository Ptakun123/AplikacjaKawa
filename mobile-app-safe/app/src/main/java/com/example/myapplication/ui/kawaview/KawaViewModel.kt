package com.example.myapplication.ui.kawaview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.data.roomdatabase.Kawa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KawaViewModel(private val db: AppDatabase) : ViewModel(){
    val kawaList = db.kawaDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var nazwa by  mutableStateOf("")
    var kraj by mutableStateOf("")
    var palenie by mutableStateOf("")
    var gatunek by mutableStateOf("")
    var opis by mutableStateOf("")

    private val eventChannel = Channel<UiEventKawa>()
    val uiEvents = eventChannel.receiveAsFlow()

    fun zapiszKawe(){
        viewModelScope.launch(Dispatchers.IO) {
            val kawaDao = db.kawaDao()
            if(nazwa == ""){
                eventChannel.send(UiEventKawa.ShowSnackbar("Wpisz nazwę kawy"))
            }
            else if(kawaDao.countNazwa(nazwa) > 0){
                eventChannel.send(UiEventKawa.ShowSnackbar("Kawa o nazwie '$nazwa' już istnieje"))
            }
            else {
                val kawaObj = Kawa(0, kraj, nazwa, palenie, gatunek, opis)
                kawaDao.insertAll(listOf(kawaObj))
                eventChannel.send(UiEventKawa.ShowSnackbar("Dodano kawę $nazwa"))
                nazwa = ""
                kraj = ""
                palenie = ""
                gatunek = ""
                opis  = ""
            }
        }
    }
}

class KawaViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KawaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KawaViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface UiEventKawa { //Snackbar
    data class ShowSnackbar(val message: String) : UiEventKawa
    object NavigateBack : UiEventKawa // Na przyszłość, np. żeby wyjść z ekranu po zapisie
}