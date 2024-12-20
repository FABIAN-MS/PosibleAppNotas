 package com.ad_coding.noteappcourse.componentes

 import android.net.Uri
 import android.app.Activity
 import android.content.Intent
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.compose.foundation.Image
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.width
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.items
 import androidx.compose.material.*
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.MailOutline

 import androidx.compose.material3.AlertDialog
 import androidx.compose.material3.Button
 import androidx.compose.material3.Icon
 import androidx.compose.material3.Text
 import androidx.compose.runtime.*
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import coil.compose.rememberAsyncImagePainter

 import com.example.inventory.data.Note
 import com.example.inventory.data.NoteDao

 @Composable
 fun MultimediaPicker(
     onMultimediaChange: (List<String>) -> Unit, // Callback para enviar los cambios
 ) {
     val context = LocalContext.current
     var multimediaUris by remember { mutableStateOf<List<String>>(listOf()) }
     var selectedUri by remember { mutableStateOf<Uri?>(null) }
     var showDialogForSelection by remember { mutableStateOf(false) }
     var showDeleteDialog by remember { mutableStateOf(false) }

     // ActivityResultLauncher para seleccionar multimedia
     val multimediaPickerLauncher = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.StartActivityForResult()
     ) { result ->
         if (result.resultCode == Activity.RESULT_OK) {
             val clipData = result.data?.clipData
             val uris = mutableListOf<String>()

             if (clipData != null) {
                 for (i in 0 until clipData.itemCount) {
                     uris.add(clipData.getItemAt(i).uri.toString())
                 }
             } else {
                 result.data?.data?.let { uri ->
                     uris.add(uri.toString())
                 }
             }

             multimediaUris = uris
             onMultimediaChange(uris) // Notificar los cambios
         }
     }

     Column {
         // Botón para abrir el diálogo de selección
         Button(
             onClick = { showDialogForSelection = true },
             modifier = Modifier.padding(16.dp)
         ) {
             Icon(Icons.Filled.MailOutline, contentDescription = "Seleccionar Multimedia")
             Spacer(modifier = Modifier.width(8.dp))
             Text("Añadir Multimedia")
         }

         // Lista de imágenes o videos seleccionados
         LazyColumn(
             modifier = Modifier
                 .height(150.dp)
                 .padding(horizontal = 16.dp)
         ) {
             items(multimediaUris) { uri ->
                 Image(
                     painter = rememberAsyncImagePainter(model = Uri.parse(uri)),
                     contentDescription = null,
                     modifier = Modifier
                         .width(100.dp)
                         .height(150.dp)
                         .clickable {
                             selectedUri = Uri.parse(uri)
                             showDeleteDialog = true
                         }
                         .padding(8.dp)
                 )
             }
         }

         // Diálogo de confirmación para seleccionar multimedia
         if (showDialogForSelection) {
             AlertDialog(
                 onDismissRequest = { showDialogForSelection = false },
                 title = { Text(text = "Seleccionar multimedia") },
                 confirmButton = {
                     Button(onClick = {
                         showDialogForSelection = false
                         val intent = Intent(Intent.ACTION_PICK).apply {
                             type = "image/* video/*"
                             putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                         }
                         multimediaPickerLauncher.launch(intent)
                     }) {
                         Text("Aceptar")
                     }
                 },
                 dismissButton = {
                     Button(onClick = { showDialogForSelection = false }) {
                         Text("Cancelar")
                     }
                 }
             )
         }

         // Diálogo para eliminar un archivo multimedia
         if (showDeleteDialog) {
             AlertDialog(
                 onDismissRequest = { showDeleteDialog = false },
                 title = { Text("Eliminar archivo multimedia") },
                 text = { Text("¿Estás seguro de que deseas eliminar este archivo?") },
                 confirmButton = {
                     Button(onClick = {
                         showDeleteDialog = false
                         selectedUri?.let { uri ->
                             multimediaUris = multimediaUris.filter { it != uri.toString() }
                             onMultimediaChange(multimediaUris) // Notificar cambios tras eliminar
                         }
                     }) {
                         Text("Eliminar")
                     }
                 },
                 dismissButton = {
                     Button(onClick = { showDeleteDialog = false }) {
                         Text("Cancelar")
                     }
                 }
             )
         }
     }
 }
