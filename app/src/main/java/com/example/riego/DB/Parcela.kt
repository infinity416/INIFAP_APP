package com.example.riego.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date

@Entity(tableName = "parcelas")
data class Parcela(
    var naame: String,
    var cultivo: String,
    var lat: String,
    var lon: String,
    var fecha: String,
    var fechariegosiem: String,
    var crecimieto: String,
    var riego: String,
    var suelo: String,
    //AF
    var pozo: String,
    var larg: String,
    var anch: String,
    var timear: String,
    //S&C
    var gotero: String,
    var cmXsuko: String,
    var largXsurco: String,
    var cmXgotero: String,
    //G
    var gastogot: String,
    var sepsurco: String,
    var sepgot: String,
    //P
    var gastoagua: String,
    var dispi: String,
    var horas: String,
    @PrimaryKey(autoGenerate = true)
 var id: Int = 0,
): Serializable