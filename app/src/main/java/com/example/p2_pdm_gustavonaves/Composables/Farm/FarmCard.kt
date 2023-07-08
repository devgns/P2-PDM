package com.example.p2_pdm_gustavonaves.Composables.Farm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p2_pdm_gustavonaves.DataClasses.Farm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmCard(farm: Farm, onEditClicked: () -> Unit,onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = farm.name,
                    fontSize = 20.sp,
                    color = Color.Blue,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDeleteClicked,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete farm",
                        tint = Color.Red
                    )
                }
                IconButton(
                    onClick = onEditClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit farm"
                    )
                }
            }
            Text(text = "Código: ${farm.code}")
            Text(text = "Nome: ${farm.name}")
            Text(text = "Valor da propriedade: ${farm.propertyValue}")
            Text(text = "Quantidade de funcionários: ${farm.employeesNumber}")
        }
    }
}

@Preview
@Composable
fun farmCardPreviem(){
    val farm = Farm( "1", "Fazenda Boi Gordo", 20.5, 10)
    FarmCard(farm, {}, {})
}