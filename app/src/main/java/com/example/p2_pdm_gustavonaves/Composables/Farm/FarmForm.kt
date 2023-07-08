package com.example.p2_pdm_gustavonaves.Composables.Farm

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCliente(navController: NavController, farmsViewModel: FarmsViewModel) {
    val selectedFarm = farmsViewModel.getSelectedFarm();
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val idState = remember { mutableStateOf("") }
    val codeState = remember { mutableStateOf<String>("") }
    val nameState = remember { mutableStateOf<String>("") }
    val propertyValueState = remember { mutableStateOf<Double?>(null) }
    val employeesNumberState = remember { mutableStateOf<Int?>(null) }

    if (selectedFarm != null) {
        idState.value = selectedFarm.id as String;
        codeState.value = selectedFarm.code;
        nameState.value = selectedFarm.name;
        propertyValueState.value = selectedFarm.propertyValue;
        employeesNumberState.value = selectedFarm.employeesNumber;
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = if (selectedFarm != null) "Atualizar fazenda" else "Cadastrar fazenda") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("farm") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            })
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
        ) {
            Text("Cadastro de fazenda", style = TextStyle(fontSize = 20.sp))
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = codeState.value.toString(),
                onValueChange = { value -> codeState.value = value },
                label = { Text("Código") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nameState.value.toString(),
                onValueChange = { value -> nameState.value = value },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = propertyValueState.value?.toString() ?: "",
                onValueChange = { value ->
                    propertyValueState.value = value.toDoubleOrNull()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Valor da propriedade (R$)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = employeesNumberState.value?.toString() ?: "",
                onValueChange = { value ->
                    employeesNumberState.value = value.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Quantidade de funcionários") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    val farm = Farm(
                        id = if(selectedFarm != null) selectedFarm.id else null,
                        code = codeState.value,
                        name = nameState.value,
                        propertyValue = propertyValueState.value as Double,
                        employeesNumber = employeesNumberState.value as Int
                    )
                    scope.launch(Dispatchers.IO) {
                        farmsViewModel.createOrEdit(farm){ success, error ->
                            if (success) {
                                val createOrEdit = if(selectedFarm == null) "criada" else "editada"
                                Toast.makeText(context, "Fazenda $createOrEdit com sucesso", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        };
                    }
                },
                enabled = codeState.value.isNotBlank() && nameState.value.isNotBlank() && propertyValueState.value != null && employeesNumberState.value != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (selectedFarm != null) "Atualizar" else "Cadastrar")
            }
        }
    })
}
