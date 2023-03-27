package de.schroeder.androidconsumer.superheroes.boundary

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.schroeder.androidconsumer.superheroes.control.SuperheroRepository
import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuperheroViewModel(private val repository: SuperheroRepository): ViewModel() {

    var uiState by mutableStateOf( SuperheroUiState() )
        private set

    fun getSuperheroes() {
        CoroutineScope(Dispatchers.IO).launch {
            val heroes = repository.getSuperheroes()
            uiState = uiState.copy(heroes = heroes)
        }
    }

}

data class SuperheroUiState(
    val heroes: List<SuperheroResource> = emptyList()
)