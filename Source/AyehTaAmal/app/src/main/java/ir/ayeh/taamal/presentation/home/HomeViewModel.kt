package ir.ayeh.taamal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.HomeSnapshot
import ir.ayeh.taamal.domain.model.UserSettings
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ContentRepository,
    prefs: UserPreferencesRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings> = prefs.settings.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings()
    )

    private val _snapshot = MutableStateFlow(HomeSnapshot(null, "", "", null, null, null, ir.ayeh.taamal.domain.model.WeeklyStats()))
    val snapshot: StateFlow<HomeSnapshot> = _snapshot.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _snapshot.value = repo.homeSnapshot()
        }
    }

    fun markPracticeDone() {
        viewModelScope.launch {
            repo.bumpProgress("practices_done")
            refresh()
        }
    }
}
