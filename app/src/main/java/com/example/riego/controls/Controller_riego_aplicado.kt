@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.example.riego.controls

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.riego.DB.RiegoAplicado
import java.sql.Date

@Dao
interface Controller_riego_aplicado {
    @Query("Select * from riego_aplicado where Id_Parcela=:Id_Parcela and fecha_riego_aplicado between :fechars and :fechacon order by fecha_riego_aplicado asc")
    fun obetenerRiegoAplic(Id_Parcela: Int, fechars: String, fechacon: String): LiveData<List<RiegoAplicado>>

    @Insert
    fun agregarRiegoAplic(vararg riegoAplicado: RiegoAplicado)

    @Query("Delete from riego_aplicado Where id_RiegoAplic=:id_RiegoAplic ")
    fun borrarRiegoAplic(id_RiegoAplic: Int)

    @Query("Select * from riego_aplicado where Id_Parcela=:Id_Parcela and fecha_riego_aplicado between :fechars and :fechacon order by fecha_riego_aplicado asc")
    fun consultaHistoricoRS(Id_Parcela: Int, fechars: String, fechacon: String): LiveData<RiegoAplicado>
}