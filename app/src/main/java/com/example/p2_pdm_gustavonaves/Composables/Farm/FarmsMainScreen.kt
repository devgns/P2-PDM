package com.example.p2_pdm_gustavonaves.Composables.Farm

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.p2_pdm_gustavonaves.R
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmsMainScreen(navController: NavController, farmsViewModel: FarmsViewModel) {
    val context = LocalContext.current

    val farmsState = farmsViewModel.farms.collectAsState(initial = mutableListOf())
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            farmsViewModel.getFarms()
        }
    }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Fazendas") }, actions = {
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text(text = "Baixar fazendas cadastradas") },
                        onClick = {
                            farmsViewModel.saveFarmsToFile(context) { success, msg ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Fazendas salvas em arquivo externo com sucesso (/Downloads)",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context, msg, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            };
                            showMenu = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Exibir valor médio das propriedades") },
                        onClick = {
                            farmsViewModel.getPropertyValueAverage { average ->
                                val formattedAverage = String.format(
                                    "%.2f",
                                    average
                                ) // Formata o valor médio com 2 casas decimais
                                Toast.makeText(
                                    context,
                                    "Valor médio das propriedades:  $formattedAverage reais",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            showMenu = false
                        }
                    )
                }
            }

        })
    }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(R.drawable.wheat_gb05a9f908_1920), // Recurso da imagem de fundo
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds // Escala da imagem para preencher todo o espaço
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                //mostrar a media das fazendas aqui
                FarmFilterForm(farmsViewModel)
                FarmsListing(farmsState.value, farmsViewModel, navController)
            }
        }

    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                farmsViewModel.setSelectedId(null);
                navController.navigate("farm-form")
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                Text("Adicionar")
            }
        }
    })


}