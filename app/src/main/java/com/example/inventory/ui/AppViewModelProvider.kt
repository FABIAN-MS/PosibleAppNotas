package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.item.NoteEntryViewModel
import com.example.inventory.ui.item.NoteEditViewModel
import com.example.inventory.ui.notes.NoteDetailsViewModel
import com.example.inventory.ui.notes.NotesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Inicializar NoteEntryViewModel
        initializer {
            NoteEntryViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }

        // Inicializar NoteEditViewModel
        initializer {
            NoteEditViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }
        initializer {
            NotesViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }
        // Inicializar NoteDetailsViewModel con SavedStateHandle
        initializer {
            val savedStateHandle = this.createSavedStateHandle()  // Obtiene el SavedStateHandle
            NoteDetailsViewModel(
                notesRepository = inventoryApplication().container.notesRepository,
                savedStateHandle = savedStateHandle
            )
        }
    }
}

fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
