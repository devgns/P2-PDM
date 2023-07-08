package com.example.p2_pdm_gustavonaves.DataClasses

import java.io.Serializable

data class Farm(
    var id: String?,
    val code: String,
    val name: String,
    val propertyValue: Double,
    val employeesNumber: Int
): Serializable