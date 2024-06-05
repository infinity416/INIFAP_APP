package com.example.riego

data class Historial(
    val Fecha : String,
    val LaminaSueloActual : Double,
    val LaminaReponer : Double,
    val TiempoRiego: String,
    val UCA: Double,
    val PrecipitacionEfectivaAcum: Int,
    val ETCAcum: Double,
)