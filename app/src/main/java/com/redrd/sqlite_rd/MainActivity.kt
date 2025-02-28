package com.redrd.sqlite_rd

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redrd.sqlite_rd.database_sqlite.AppDatabase
import com.redrd.sqlite_rd.database_sqlite.entitie.Contactos
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(applicationContext)

        setContent {
            ContactApp(db)
        }
    }
}

@Composable
fun ContactApp(db: AppDatabase) {
    var nombre = remember { mutableStateOf("") }
    var numeroCelular = remember { mutableStateOf("") }
    var contactos = remember { mutableStateOf<List<Contactos>>(emptyList()) }
    var selectedContact = remember { mutableStateOf<Contactos?>(null) }

    // Corutinas
    val corutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para cargar todos los contactos
    fun loadContacts() {
        corutineScope.launch {
            contactos.value = db.contactDao().getAllContacts()
        }
    }

    // Cargar los contactos al iniciar la UI
    LaunchedEffect(Unit) {
        loadContacts()
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        // Campo de texto para el nombre
        OutlinedTextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para el número
        OutlinedTextField(
            value = numeroCelular.value,
            onValueChange = { numeroCelular.value = it },
            label = { Text("Número Telefónico") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para añadir o editar
        /*Button(
            onClick = {
                if (selectedContact.value != null) {
                    // Editar el contacto existente
                    val updatedContact = selectedContact.value!!.copy(
                        nombre = nombre.value,
                        numero = numeroCelular.value
                    )
                    corutineScope.launch {
                        db.contactDao().update(updatedContact)
                        loadContacts() // Actualizar la lista de contactos
                        Toast.makeText(context, "Contacto actualizado: ${nombre.value}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Añadir nuevo contacto
                    val contacto = Contactos(nombre = nombre.value, numero = numeroCelular.value)
                    corutineScope.launch {
                        db.contactDao().insert(contacto)
                        loadContacts() // Actualizar la lista de contactos
                        Toast.makeText(context, "Contacto añadido: ${nombre.value}", Toast.LENGTH_SHORT).show()
                    }
                }

                // Limpiar los campos después de añadir o editar
                nombre.value = ""
                numeroCelular.value = ""
                selectedContact.value = null // Resetear el contacto seleccionado
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (selectedContact.value != null) "Editar Contacto" else "Añadir Contacto")
        }*/

        // Botón para añadir o editar
        Button(onClick = {
            if (selectedContact.value != null) {
                // Editar el contacto existente
                val updatedContact = selectedContact.value!!.copy(
                    nombre = nombre.value,
                    numero = numeroCelular.value
                )
                corutineScope.launch {
                    db.contactDao().update(updatedContact)
                    loadContacts() // Actualizar la lista de contactos
                    Toast.makeText(context, "Contacto actualizado: ${nombre.value}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Añadir nuevo contacto
                val contacto = Contactos(nombre = nombre.value, numero = numeroCelular.value)
                corutineScope.launch {
                    db.contactDao().insert(contacto)
                    loadContacts() // Actualizar la lista de contactos
                    Toast.makeText(context, "Contacto añadido: ${nombre.value}", Toast.LENGTH_SHORT).show()
                }
            }

            // Limpiar los campos después de añadir o editar
            nombre.value = ""
            numeroCelular.value = ""
            selectedContact.value = null // Resetear el contacto seleccionado
        }, modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            ) {
            Text(if (selectedContact.value != null) "Editar Contacto" else "Añadir Contacto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de contactos
        Text("Contactos:",style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))


        // Mostramos los contactos en tarjetas
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(contactos.value) { index, contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            nombre.value = contact.nombre
                            numeroCelular.value = contact.numero
                            selectedContact.value = contact
                        },
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Cambié aquí para usar la función correcta
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nombre: ${contact.nombre}", style = MaterialTheme.typography.bodyLarge)
                            Text("Número: ${contact.numero}", style = MaterialTheme.typography.bodyLarge)
                        }

                        // Botones para editar y eliminar
                        Column(verticalArrangement = Arrangement.Center) {
                            // Botón de editar
                            IconButton(onClick = {
                                nombre.value = contact.nombre
                                numeroCelular.value = contact.numero
                                selectedContact.value = contact
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                            }

                            // Botón de eliminar
                            IconButton(onClick = {
                                corutineScope.launch {
                                    db.contactDao().delete(contact) // Elimina el contacto de la base de datos
                                    loadContacts() // Recarga la lista después de eliminar el contacto
                                    Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }

    }
}

/*@Composable
fun ContactApp(db: AppDatabase) {
    var nombre = remember { mutableStateOf("") }
    var numeroCelular = remember { mutableStateOf("") }
    var contactos = remember { mutableStateOf<List<Contactos>>(emptyList()) }
    var selectedContact = remember { mutableStateOf<Contactos?>(null) } // Para seleccionar un contacto a editar

    // Corutinas
    val corutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para cargar todos los contactos
    fun loadContacts() {
        corutineScope.launch {
            contactos.value = db.contactDao().getAllContacts()
        }
    }

    // Cargar los contactos al iniciar la UI
    LaunchedEffect(Unit) {
        loadContacts()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre: ") }
        )

        TextField(
            value = numeroCelular.value,
            onValueChange = { numeroCelular.value = it },
            label = { Text("Numero Telefonico: ") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para añadir o editar
        Button(onClick = {
            if (selectedContact.value != null) {
                // Editar el contacto existente
                val updatedContact = selectedContact.value!!.copy(
                    nombre = nombre.value,
                    numero = numeroCelular.value
                )
                corutineScope.launch {
                    db.contactDao().update(updatedContact)
                    loadContacts() // Actualizar la lista de contactos
                    Toast.makeText(context, "Contacto actualizado: ${nombre.value}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Añadir nuevo contacto
                val contacto = Contactos(nombre = nombre.value, numero = numeroCelular.value)
                corutineScope.launch {
                    db.contactDao().insert(contacto)
                    loadContacts() // Actualizar la lista de contactos
                    Toast.makeText(context, "Contacto añadido: ${nombre.value}", Toast.LENGTH_SHORT).show()
                }
            }

            // Limpiar los campos después de añadir o editar
            nombre.value = ""
            numeroCelular.value = ""
            selectedContact.value = null // Resetear el contacto seleccionado
        }) {
            Text(if (selectedContact.value != null) "Editar Contacto" else "Añadir Contacto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de contactos
        Text("Contactos:")
        contactos.value.forEach { contact ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Nombre: ${contact.nombre}")
                    Text("Numero: ${contact.numero}")
                }

                // Botón de edición para cada contacto
                Button(onClick = {
                    nombre.value = contact.nombre
                    numeroCelular.value = contact.numero
                    selectedContact.value = contact
                }) {
                    Text("Editar")
                }

                // Botón de eliminación para cada contacto
                Button(onClick = {
                    corutineScope.launch {
                        db.contactDao().delete(contact)
                        loadContacts() // Actualizar la lista después de eliminar
                        Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}*/

/*@Composable
fun ContactApp(db : AppDatabase) {
    var nombre = remember { mutableStateOf("") }
    var numeroCelular = remember { mutableStateOf("") }
    var contactos = remember { mutableStateOf<List<Contactos>>(emptyList()) }

    // coruntinas
    var corutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column (modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre: ") }
        )

        TextField(
            value = numeroCelular.value,
            onValueChange = { numeroCelular.value = it },
            label = { Text("Numero Telefonico: ") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val contacto = Contactos(nombre = nombre.value, numero = numeroCelular.value)
            corutineScope.launch {
                db.contactDao().insert(contacto)
                contactos.value = db.contactDao().getAllContacts()
                Toast.makeText(context, "Contacto: ${nombre.toString()}, añadido", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Añadir Contacto")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Contactos:")
        contactos.value.forEach { contact ->
            Text("Nombre: ${contact.nombre}, Numero_Celular: ${contact.numero}")
        }
    }
}*/