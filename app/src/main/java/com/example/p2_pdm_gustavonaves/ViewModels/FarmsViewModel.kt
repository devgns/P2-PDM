package com.example.p2_pdm_gustavonaves.ViewModels

import android.util.Log
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class FarmsViewModel : ViewModel() {
    private var db = FirebaseFirestore.getInstance()

    private var selectedId: String? = null;
    private val _farms = MutableStateFlow<MutableList<Farm>>(mutableListOf())
    val farms: StateFlow<MutableList<Farm>> = _farms;
    fun setSelectedId(id: String?) {
        this.selectedId = id
    }

    fun getSelectedFarm(): Farm? {
        return farms.value.find { it.id === selectedId }
    }


    fun getFarms(code: String = "", name: String = "") {
        viewModelScope.launch {
            val farmList: MutableList<Farm> = mutableListOf()
            var query: Query = db.collection("farm")

            if (code.isNotEmpty()) {
                query = query.whereEqualTo("code", code)
            }

            if (name.isNotEmpty()) {
                query = query.whereEqualTo("name", name)
            }

            query = query.orderBy("code")

            query.get().addOnSuccessListener { result ->
                    for (document in result) {
                        val farm = document.data

                        farmList.add(
                            Farm(
                                id = farm["id"] as String?,
                                code = farm["code"] as String,
                                name = farm["name"] as String,
                                propertyValue = farm["propertyValue"].toString().toDouble(),
                                employeesNumber = farm["employeesNumber"].toString().toInt()
                            )
                        )
                    }
                    _farms.value = farmList
                }
        }
    }


//    fun createOrEdit(farm: Farm, callback: (Boolean, String?) -> Unit) {
//
//
//        if (farm.id == null) {
//            val uuid = UUID.randomUUID().toString();
//            farm.id = uuid
//        }
//
//        val clientMap = hashMapOf(
//            "id" to farm.id,
//            "code" to farm.code,
//            "name" to farm.name,
//            "propertyValue" to farm.propertyValue,
//            "employeesNumber" to farm.employeesNumber,
//
//            )
//
//        db.collection("farm").document(farm.id as String).set(farm).addOnCompleteListener {
//                callback(true, null) // Chamada de sucesso
//                Log.i("success", "Sucesso ao criar/editar fazenda")
//            }.addOnFailureListener { e ->
//                callback(false, e.message) // Chamada de erro com a mensagem de erro
//                Log.i("error", "Erro ao criar/editar fazenda: $e")
//            }
//    }

    fun createOrEdit(farm: Farm, callback: (Boolean, String?) -> Unit) {
        if (farm.id == null) {
            val uuid = UUID.randomUUID().toString()
            farm.id = uuid
        }

        val clientMap = hashMapOf(
            "id" to farm.id,
            "code" to farm.code,
            "name" to farm.name,
            "propertyValue" to farm.propertyValue,
            "employeesNumber" to farm.employeesNumber,
        )

        db.collection("farm")
            .whereEqualTo("code", farm.code)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    db.collection("farm")
                        .document(farm.id as String)
                        .set(clientMap)
                        .addOnSuccessListener {
                            callback(true, null) // Chamada de sucesso
                            Log.i("success", "Sucesso ao criar/editar fazenda")
                        }
                        .addOnFailureListener { e ->
                            callback(false, "Erro ao criar//editar fazenda: $e.message") // Chamada de erro com a mensagem de erro
                            Log.i("error", "Erro ao criar/editar fazenda: $e")
                        }
                } else {
                    callback(false, "Já existe uma fazenda com o código fornecido.")
                }
            }
            .addOnFailureListener { e ->
                callback(false, e.message) // Chamada de erro com a mensagem de erro
                Log.i("error", "Erro ao buscar fazenda: $e")
            }
    }

    fun delete(id: String, callback: (Boolean, String?) -> Unit) {
        db.collection("farm").document(id).delete().addOnSuccessListener {
                Log.i("success", "Fazenda deletada com sucesso")
                callback(true, null) // Chamada de sucesso
            }.addOnFailureListener { e ->
                Log.i("error", "erro ao deletar fazenda : $e")
                callback(false, e.message) // Chamada de erro com a mensagem de erro
            }
    }
}

//var query: Query = db.collection("farm")
//query = query.whereEqualTo("code", farm.code)
//query.get().addOnSuccessListener { result ->
//    Log.i("teste", result.toString())
//}.addOnFailureListener { e ->
//}