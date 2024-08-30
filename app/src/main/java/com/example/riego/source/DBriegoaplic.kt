package com.example.riego.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.riego.DB.RiegoAplicado
import com.example.riego.controls.Controller_riego_aplicado

@Database(entities = [RiegoAplicado::class], version = 1)
abstract class DBriegoaplic: RoomDatabase() {

    abstract fun riego_aplicado(): Controller_riego_aplicado

    companion object{
        @Volatile
        private var INSTANCE:DBriegoaplic? = null

        fun getDatabase(context: Context): DBriegoaplic{
            val temporal = INSTANCE

            if(temporal != null){
                return temporal
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBriegoaplic::class.java,
                    "db_riegoaplic"
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}