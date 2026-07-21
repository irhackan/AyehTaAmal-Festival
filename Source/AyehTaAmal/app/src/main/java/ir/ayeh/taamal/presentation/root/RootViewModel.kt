package ir.ayeh.taamal.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.seed.ContentSeeder
import ir.ayeh.taamal.domain.model.UserSettings
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RootViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val seeder: ContentSeeder
) : ViewModel() {

    val settings: StateFlow<UserSettings> = prefs.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UserSettings()
    )

    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { seeder.seedIfNeeded() }
            _ready.value = true
        }
    }

    fun updateSettings(transform: (UserSettings) -> UserSettings) {
        viewModelScope.launch { prefs.update(transform) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            prefs.update { it.copy(onboardingDone = true) }
        }
    }
}
