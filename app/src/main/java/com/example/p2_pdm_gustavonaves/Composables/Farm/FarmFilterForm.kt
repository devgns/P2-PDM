package com.example.p2_pdm_gustavonaves.Composables.Farm


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.p2_pdm_gustavonaves.R
import com.example.p2_pdm_gustavonaves.ViewModels.FarmsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmFilterForm(farmsViewModel: FarmsViewModel) {
    val codeState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)
        ) {
            OutlinedTextField(
                value = codeState.value,
                onValueChange = { value -> codeState.value = value },
                label = { Text("Código") },
                shape = RoundedCornerShape(12.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(3f)
                .padding(end = 8.dp)
        ) {
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { value -> nameState.value = value },
                label = { Text("Nome") },
                shape = RoundedCornerShape(12.dp),
            )
        }

        IconButton(
            onClick = {
                codeState.value = "";
                nameState.value = "";
                farmsViewModel.getFarms()
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_cleaning_services_24),
                contentDescription = "clean filters",
            )
        }
        IconButton(
            onClick = {
                farmsViewModel.getFarms(codeState.value, nameState.value)
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "filter"
            )
        }
    }
}