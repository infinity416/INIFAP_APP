package com.example.riego.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.Card
import com.example.riego.Historial
import com.example.riego.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class HistoricoFragment : Fragment() {

    //Variables
    var historlaList = arrayListOf<Historial>()
    var client = OkHttpClient()
    var header = "confidential-apiKey"
    var key = ""
    //var url = "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=4&FechaIni=01/04/2023&FechaFin=01/07/2023"
    var url ="https://appinifap.sytes.net/apiweb/api/riego?estacionID=41276&fechaIni=15/05/2018&fechaFin=10/07/2018&cultivo=1&crecimiento=2&suelo=2&riego=1&a1=0.8&a2=0.2&a3=0.8"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val movimiento = inflater.inflate(R.layout.fragment_historico, container, false)
        val palpatin = movimiento.findViewById<RecyclerView>(R.id.recyclerViews)
        val imperio = okhttp3.Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()
        client.newCall(imperio).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    activity?.runOnUiThread {
                        val hay = response.body!!.string()
                        val algo = JSONObject(hay)
                        val senal = algo.getJSONObject("riego")
                        val cuantosson = senal.getJSONArray("RequerimientoRiego")
                        for (l in 0 until cuantosson.length()){
                            val contacto = cuantosson.getJSONObject(l)
                            val historial = Historial(
                                contacto.getString("Fecha"),
                                contacto.getDouble("LaminaSueloActual"),
                                contacto.getDouble("LaminaReponer"),
                                contacto.getString("TiempoRiego"),
                                contacto.getDouble("UCA"),
                                contacto.getInt("PrecipitacionEfectivaAcum"),
                                contacto.getDouble("ETCAcum")
                            )
                            historlaList.add(historial)
                            Log.d("Ejemploplox", historlaList.toString())
                    }
                        palpatin.layoutManager = LinearLayoutManager(context)
                        palpatin.adapter = Card(historlaList)
                    }
                }
            }
        })
        return  movimiento
    }
}



/*
        val jedi = vieww.findViewById<TextView>(R.id.fecha)
        val sith = vieww.findViewById<TextView>(R.id.durocomolapiedra)
        val client = OkHttpClient()
        val headers =  "confidential-apiKey"
        val key = ""
        val url =  "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=4&FechaIni=01/04/2023&FechaFin=01/07/2023"
        val republica = okhttp3.Request.Builder().url(url).header(headers, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

        client.newCall(republica).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    activity?.runOnUiThread {

                        jedi.text = headers
                        val hay = response.body!!.string()
                        val algo = JSONObject(hay)
                        val senal = algo.getJSONObject("riego")
                        val cuantosson = senal.getJSONArray("RequerimientoRiego")
                        for (l in 0 until cuantosson.length()){
                            val contacto = cuantosson.getJSONObject(l).getString("Fecha")
                            sith.text = contacto
                        }
                    }
                }
            }
        })*/
// Inflate the layout for this fragment
/**val dtarry = contenedor.getJSONArray("RequerimientoRiego")
                println(dtarry)
                //val dtta = dtarry.getJSONArray("")
                for (i in 0 until (dtarry.length())) {
                    val Fecha = dtarry.getJSONObject(i).getString("LaminaReponer")
                    //sith?.textClassifier = Fecha
                    sith?.text="I´m your Father!!!"
                    println(Fecha)
                }
                println(dtarry.length())
                println("pito")
            }
        }
    }
})*/