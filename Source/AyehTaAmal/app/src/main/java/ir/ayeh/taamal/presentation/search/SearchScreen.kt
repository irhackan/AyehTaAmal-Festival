package ir.ayeh.taamal.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.Ayah
import ir.ayeh.taamal.ui.components.ArabicAyahText
import ir.ayeh.taamal.ui.components.EmptyState
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: ContentRepository) : ViewModel() {
    var results by mutableStateOf<List<Ayah>>(emptyList())
        private set

    fun search(query: String) {
        viewModelScope.launch {
            results = if (query.isBlank()) emptyList() else repo.search(query)
        }
    }
}

@Composable
fun SearchScreen(
    onOpenAyah: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            SoftCard {
                SectionTitle("جست‌وجو", "عربی، موضوع یا کلیدواژه")
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.search(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("مثلاً صبر، آرامش، غیبت…") },
                    singleLine = true
                )
            }
        }
        if (viewModel.results.isEmpty()) {
            EmptyState(if (query.isBlank()) "عبارت جست‌وجو را وارد کنید." else "نتیجه‌ای یافت نشد.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.results, key = { it.id }) { ayah ->
                    SoftCard(modifier = Modifier.clickable { onOpenAyah(ayah.id) }) {
                        Text("سوره ${ayah.surahNumber} آیه ${ayah.ayahNumber}")
                        Spacer(Modifier.height(6.dp))
                        ArabicAyahText(ayah.arabicText)
                        Spacer(Modifier.height(4.dp))
                        Text(ayah.topic)
                    }
                }
            }
        }
    }
}
