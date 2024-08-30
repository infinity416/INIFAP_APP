package com.example.riego.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.riego.CardABC
import com.example.riego.DB.Parcela
import com.example.riego.controls.Controller_parcelas


@Database(entities = [Parcela::class], version =1)
abstract class DBparcela: RoomDatabase() {

    abstract fun parcelas(): Controller_parcelas

    companion object{
        @Volatile
        private  var INSTANCE:DBparcela? = null

        fun getDatabase(context: Context): DBparcela {
            val templ = INSTANCE

            if(templ != null){
                return templ
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBparcela::class.java,
                    "db_parcela"
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}


