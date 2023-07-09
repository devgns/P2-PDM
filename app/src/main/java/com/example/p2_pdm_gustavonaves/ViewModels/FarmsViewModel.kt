package com.example.p2_pdm_gustavonaves.ViewModels

import android.util.Log
import com.example.p2_pdm_gustavonaves.DataClasses.Farm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.UUID
import android.content.Context
import android.os.Environment

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

    fun getPropertyValueAverage(callback: (Double) -> Unit) {
        db.collection("farm")
            .get()
            .addOnSuccessListener { result ->
                var sum = 0.0
                var count = 0
                for (document in result) {
                    val farm_data = document.data

                    val farm = Farm(
                        id = farm_data["id"] as String?,
                        code = farm_data["code"] as String,
                        name = farm_data["name"] as String,
                        propertyValue = farm_data["propertyValue"].toString().toDouble(),
                        employeesNumber = farm_data["employeesNumber"].toString().toInt()
                    )

                    sum += farm.propertyValue;
                    count++;
                }
                val average = if (count > 0) sum / count else 0.0
                callback(average)
            }
            .addOnFailureListener { e ->
                callback(0.0)
                Log.i("error", "Erro ao obter fazendas: $e")
            }
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
                farmList.sortBy { it.code }
                _farms.value = farmList
            }
        }
    }

    fun createOrEdit(farm: Farm, originalCode: String?, callback: (Boolean, String?) -> Unit) {
        if (farm.id == null) {
            farm.id = UUID.randomUUID().toString()
        }

        val clientMap = hashMapOf(
            "id" to farm.id,
            "code" to farm.code,
            "name" to farm.name,
            "propertyValue" to farm.propertyValue,
            "employeesNumber" to farm.employeesNumber,
        ) as HashMap<String, Any>

        if (originalCode == farm.code) {
            // Se o código original for igual ao código atual, não é necessário fazer a consulta
            saveFarmToFirestore(farm.id!!, clientMap, callback)
        } else {
            val query = db.collection("farm").whereEqualTo("code", farm.code)
            query.get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        saveFarmToFirestore(farm.id!!, clientMap, callback)
                    } else {
                        callback(false, "Já existe uma fazenda com o código fornecido.")
                    }
                }
                .addOnFailureListener { e ->
                    callback(
                        false,
                        "Erro ao buscar fazenda: ${e.message}"
                    ) // Chamada de erro com a mensagem de erro
                    Log.i("error", "Erro ao buscar fazenda: $e")
                }
        }
    }

    private fun saveFarmToFirestore(
        id: String,
        clientMap: HashMap<String, Any>,
        callback: (Boolean, String?) -> Unit
    ) {
        db.collection("farm")
            .document(id)
            .set(clientMap)
            .addOnSuccessListener {
                callback(true, null) // Chamada de sucesso
                Log.i("success", "Sucesso ao criar/editar fazenda")
            }
            .addOnFailureListener { e ->
                callback(
                    false,
                    "Erro ao criar/editar fazenda: ${e.message}"
                ) // Chamada de erro com a mensagem de erro
                Log.i("error", "Erro ao criar/editar fazenda: $e")
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

    fun saveFarmsToFile(context: Context, callback: (Boolean, String?) -> Unit) {
        val farmsList = farms.value
        val fileName = "farms_data.txt"

        if (isExternalStorageWritable()) {
            val externalDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(externalDir, fileName)

            try {
                val fileOutputStream = FileOutputStream(file)
                val objectOutputStream = ObjectOutputStream(fileOutputStream)

                objectOutputStream.writeObject(farmsList)

                objectOutputStream.close()
                fileOutputStream.close()
                callback(true, null)
                Log.d("file", "Fazendas salvas em arquivo externo com sucesso")
            } catch (e: IOException) {
                callback(false, "Erro ao salvar fazendas em arquivo externo: $e")
                Log.d("file", "Erro ao salvar farms em arquivo externo: $e")
            }
        } else {
            Log.d("file", "Armazenamento externo não disponível")
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }
}
