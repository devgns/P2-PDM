package com.example.p2_pdm_gustavonaves.ViewModels

import android.util.Log
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class FarmsViewModel {
    private var db = FirebaseFirestore.getInstance()

    private var selectedCode: String? = null;
    private val _farms = MutableStateFlow<MutableList<Farm>>(mutableListOf())
    val farms: StateFlow<MutableList<Farm>> = _farms;

    fun setSelectedCode(id: String?) {
        this.selectedCode = id
    }

    fun getSelectedFarm(): Farm?{
        return farms.value.find { it.code === selectedCode }
    }

    suspend fun getFarms(): Flow<MutableList<Farm>>{
        val farmList: MutableList<Farm> = mutableListOf()
        db.collection("farm").get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val farm = document.data;

                    farmList.add(Farm(
                        code= farm["code"] as String,
                        name= farm["name"] as String,
                        propertyValue = farm["propertyValue"].toString().toDouble(),
                        employeesNumber = farm["employeesNumber"].toString().toInt()
                    ))
                }
                _farms.value = farmList;
            }
        return _farms
    }

    fun createOrEdit(farm: Farm) {
        val clientMap = hashMapOf(
            "code" to farm.code,
            "name" to farm.name,
            "propertyValue" to farm.propertyValue,
            "employeesNumber" to farm.employeesNumber,

        )

        db.collection("farm").document(farm.code).set(farm)
            .addOnCompleteListener {
                Log.i("sucess", "Sucess on creating/editing farm")

            }.addOnFailureListener { e ->
                Log.i("error", "Error on creating/editing farm: $e")
            }
    }

    fun delete(id: String) {
        db.collection("servico")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.i("sucess", "Servico deletado com sucesso")
            }
            .addOnFailureListener { e ->
                Log.i("erro", "Erro ao deletar o servico: $e")
            }
    }

}