@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.example.riego.controls

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.riego.DB.Parcela
import java.text.DecimalFormat

@Dao
interface Controller_parcelas {
    @Query("SELECT * FROM parcelas ORDER BY id asc")
    fun obtenerParcela(): LiveData<List<Parcela>>

    @Insert
    fun agregarParcela(vararg parcela: Parcela)

   @Query("SELECT * FROM parcelas WHERE id=:id")
    fun consutaParcela(id: Int): LiveData<Parcela>

    @Update
    fun editarParcela(parcela: Parcela)


    @Query("DELETE FROM parcelas WHERE id=:id")
    fun borrarParcela(id: Int)

    @Query("SELECT * FROM parcelas")
    fun consultarLocalitation(): LiveData<List<Parcela>>

    @Query("SELECT * FROM parcelas WHERE naame=:naame")
    fun consutaParcelaName(naame: String): LiveData<Parcela>

    @Query("SELECT * FROM parcelas WHERE naame=:naame")
    fun existeName(naame: String): LiveData<Parcela>
}