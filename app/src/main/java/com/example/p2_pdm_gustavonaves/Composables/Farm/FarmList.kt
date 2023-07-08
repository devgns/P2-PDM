package com.example.p2_pdm_gustavonaves.Composables.Farm

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel

@Composable
fun FarmsListing(
    farms: MutableList<Farm>,
    farmsViewModel: FarmsViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    if (farms.isEmpty()) {
        Text(text = "Não há fazendas cadastradas até o momento")
    } else {
        LazyColumn {
            itemsIndexed(farms) { index, farm ->
                FarmCard(farm = farm, onEditClicked = {
                    farmsViewModel.setSelectedId(farm.id)
                    navController.navigate("farm-form")
                    Log.i("add", "add farm")
                }, onDeleteClicked = {
                    farmsViewModel.delete(farm.id as String) { success, msg ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Fazenda deletada com sucesso",
                                Toast.LENGTH_SHORT
                            ).show()
                            farmsViewModel.getFarms()
                        } else {
                            Toast.makeText(
                                context,
                                "Erro ao deletar fazenda: $msg",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        }
    }
}