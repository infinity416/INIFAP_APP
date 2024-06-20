package com.example.riego.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.FragmentTransaction
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.Card
import com.example.riego.FormParcela
import com.example.riego.Historial
import com.example.riego.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    //var url ="https://appinifap.sytes.net/apiweb/api/riego?estacionID=41276&fechaIni=15/05/2018&fechaFin=10/07/2018&cultivo=1&crecimiento=2&suelo=2&riego=1&a1=0.8&a2=0.2&a3=0.8"
    //var url = ""
    lateinit var busquedaFragment: BusquedaFragment
    lateinit var graficoFragment: GraficoFragment


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /****Atenea****/
        //var nameParcela = arguments?.getString("Stationsname")
        var idParcela = arguments?.getString("Stationsid")
        var idParcela1 = arguments?.getInt("Stationsid630")
        var idParcela2 = arguments?.getInt("Stationsid31365")
        var idParcela3 = arguments?.getInt("Stationsid")

        println("de 0 a 5: "+idParcela.toString())
        println("de 6 a 30: "+idParcela1)
        println("de 31 a 365: "+idParcela2)
        println("mas de 366: "+idParcela3)

        //var latParcela = arguments?.getString("Stationslat")?.toInt()
        var dateinputParcela = arguments?.getString("StationsDateInput")
        var datestartParcela = arguments?.getString("StationsDateStart")
        var cultivoParcela = arguments?.getString("StationsCultivo")
        var crecimientoParcela = arguments?.getString("StationsCrecimiento")
        var sueloParcela = arguments?.getString("StationsSuelo")
        var riegoParcela = arguments?.getString("StationsReigo")
        var largoParcela = arguments?.getString("StationsLargo")
        var anchoParcela = arguments?.getString("StationsAncho")
        var aguaParcela = arguments?.getString("StationsAgua")
        //println("lokkk..."+dateinputParcela+datestartParcela)
        var cultivoClave = when (cultivoParcela){
            "Maíz Grano"  -> 1
            "Maíz Forraje"  -> 2
            else -> "Invalid_Cultivo."
        }

        var cresClave = when (crecimientoParcela){
            "Precoz"  -> 1
            "Intermedio"  -> 2
            "Tardío"  -> 3
            else -> "Invalid_Tipo_de_Crecimiento."
        }

        var sueloClave = when (sueloParcela){
            "Ligero"  -> 1
            "Media"  -> 2
            "Pesado"  -> 3
            else -> "Invalid_Tipo_de_suelo."
        }

        var riegoClave = when (riegoParcela){
            "Goteo"  -> 1
            //""  -> 2
            //""  -> 3
            else -> "Invalid_Tipo_de_Goteo."
        }

        println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela)
       // println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="41276"&fechaIni="15052018"&fechaFin="10072018"&cultivo="1"&crecimiento="2"&suelo="2"&riego="1"&a1="0.8"&a2="0.2"&a3="0.8)
        // otro funcioan val url = "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela
        val url = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela

        /*****A.C.*******/
        //getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val newtworkinfo = wificonection.getActiveNetworkInfo()




        val movimiento = inflater.inflate(R.layout.fragment_historico, container, false)
        val imgnube = movimiento.findViewById<ImageView>(R.id.nube)
        imgnube.setVisibility(View.INVISIBLE)
        if(newtworkinfo!= null && newtworkinfo.isConnected()){
            imgnube.setVisibility(View.INVISIBLE)
            println("Conectado")
            val imperio = okhttp3.Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()
            val palpatin = movimiento.findViewById<RecyclerView>(R.id.recyclerViews)
            client.newCall(imperio).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val codeclave = response.code
                    println(codeclave)
                    if(codeclave == 400){
                        println("No ahi informacion que mostrar")
                    }else if(codeclave == 200){
                        if (response.isSuccessful){
                            activity?.runOnUiThread {
                                val hay = response.body!!.string()
                                val algo = JSONObject(hay)
                                val encontre =  algo.names().toString()
                                val good = "[\"riego\"]"
                                val notgood = "[\"error\"]"
                                if(encontre==notgood){
                                    val senal = algo.getJSONObject("error").get("id")
                                    if(senal === 1){
                                        println("Error en el Key")
                                    }else if(senal === 2){
                                        println("Error en el valor de la key")
                                    }else if(senal === 3){
                                        println("Error en la creacion de datos")
                                    }
                                }else if(encontre==good){
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
                    }
                }
            })
        }else{
            imgnube.setVisibility(View.VISIBLE)
            println("no conecado")
        }




        var back = movimiento.findViewById<FloatingActionButton>(R.id.btnBack)
        var next = movimiento.findViewById<FloatingActionButton>(R.id.btnNext)


        back.setOnClickListener {
            busquedaFragment = BusquedaFragment()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.ViewHistoricoFragment,busquedaFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }



        next.setOnClickListener {
            graficoFragment = GraficoFragment()
            val pck = Bundle()
            pck.putString("Stationsid", idParcela)
            pck.putString("StationsDateInput", dateinputParcela)
            pck.putString("StationsDateStart", datestartParcela)
            pck.putString("StationsCultivo", cultivoParcela)
            pck.putString("StationsCrecimiento", crecimientoParcela)
            pck.putString("StationsSuelo", sueloParcela)
            pck.putString("StationsReigo", riegoParcela)
            pck.putString("StationsLargo", largoParcela)
            pck.putString("StationsAncho", anchoParcela)
            pck.putString("StationsAgua", aguaParcela)
            graficoFragment.arguments = pck
            childFragmentManager
                .beginTransaction()
                .replace(R.id.ViewHistoricoFragment,graficoFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }


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