package ir.ayeh.taamal.presentation.journal

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.JournalNote
import ir.ayeh.taamal.ui.components.EmptyState
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class JournalViewModel @Inject constructor(private val repo: ContentRepository) : ViewModel() {
    val notes = repo.observeJournal().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(title: String, content: String, decision: String, result: String) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repo.saveJournal(
                JournalNote(
                    ayahId = null,
                    title = title,
                    content = content,
                    decision = decision,
                    result = result,
                    tags = "تدبر",
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repo.deleteJournal(id) }
    }
}

@Composable
fun JournalScreen(viewModel: JournalViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "یادداشت جدید")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            EmptyState("هنوز یادداشتی ثبت نشده است.", "افزودن یادداشت") { showDialog = true }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { SectionTitle("دفتر تدبر", "برداشت‌ها، تصمیم‌ها و نتایج") }
                items(notes, key = { it.id }) { note ->
                    SoftCard {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(note.title, style = MaterialTheme.typography.titleLarge)
                            IconButton(onClick = { viewModel.delete(note.id) }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "حذف")
                            }
                        }
                        Text(note.content)
                        if (note.decision.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text("تصمیم: ${note.decision}", color = MaterialTheme.colorScheme.primary)
                        }
                        if (note.result.isNotBlank()) {
                            Text("نتیجه: ${note.result}")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        var decision by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("یادداشت جدید") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(title, { title = it }, label = { Text("عنوان") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(content, { content = it }, label = { Text("برداشت") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                    OutlinedTextField(decision, { decision = it }, label = { Text("تصمیم") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(result, { result = it }, label = { Text("نتیجه") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        viewModel.save(title, content, decision, result)
                        showDialog = false
                    }
                }) { Text("ذخیره") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("انصراف") }
            }
        )
    }
}
