package com.example.riego.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "parcelas")
data class Parcela(
    var naame: String,
    var cultivo: String,
    var lat: String,
    var lon: String,
    var fecha: String,
    var crecimieto: String,
    var riego: String,
    var suelo: String,
    var agua: String,
    var larg: String,
    var anch: String,
    var hora: String,
    var cmXsuko: String,
    @PrimaryKey(autoGenerate = true)
 var id: Int = 0,
): Serializable