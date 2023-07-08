package com.example.p2_pdm_gustavonaves.Composables.Farm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmsMainScreen(navController: NavController, farmsViewModel: FarmsViewModel) {

    val farmsState = farmsViewModel.farms.collectAsState(initial = mutableListOf())
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            farmsViewModel.getFarms()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Fazendas") },
//                navigationIcon = {
//                    IconButton(onClick = {navController.navigate("home") }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
//                    }
//                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                FarmFilterForm(farmsViewModel)

                FarmsListing(farmsState.value, farmsViewModel, navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    farmsViewModel.setSelectedId(null);
                    navController.navigate("farm-form") },
                modifier = Modifier
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                    Text("Adicionar")
                }
            }
        }
    )
}