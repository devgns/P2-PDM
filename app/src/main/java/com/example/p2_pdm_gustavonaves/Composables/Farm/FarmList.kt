package com.example.p2_pdm_gustavonaves.Composables.Farm

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel

@Composable
fun FarmsListing(farms: MutableList<Farm>, farmsViewModel: FarmsViewModel, navController: NavController) {
    if (farms.isEmpty()) {
        Text(text = "Não há fazendas cadastradas até o momento")
    } else {
        LazyColumn {
            itemsIndexed(farms) { index, farm ->
                FarmCard(farm = farm, onEditClicked = {
                    farmsViewModel.setSelectedCode(farm.code)
                    navController.navigate("farm-form")
                    Log.i("add", "add farm")
                },  onDeleteClicked = {
                    farmsViewModel.delete(farm.code)
                    Log.i("delete", "delete farm")
                })
            }
        }
    }
}