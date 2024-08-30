package com.example.riego.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "riego_aplicado")
data class RiegoAplicado(
    var fecha_riego_aplicado: String,
    var hora_riego_aplicado: String,
    var Id_Parcela: Int,
    var fecha_simbra_Parcela: String,
    var fecha_riego_siembra_parcela: String,
    @PrimaryKey(autoGenerate = true)
    var id_RiegoAplic: Int = 0,
): Serializable
