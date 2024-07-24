package com.example.riego.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.Card
import com.example.riego.Historial
import com.example.riego.Home
import com.example.riego.R
import com.example.riego.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class HistoricoFragment : Fragment() {

    //Variables
    var historlaList = arrayListOf<Historial>()
    var client = OkHttpClient()
    var clients = OkHttpClient()
    var header = "confidential-apiKey"
    var key = ""
    //var url = "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=4&FechaIni=01/04/2023&FechaFin=01/07/2023"
    //var url ="https://appinifap.sytes.net/apiweb/api/riego?estacionID=41276&fechaIni=15/05/2018&fechaFin=10/07/2018&cultivo=1&crecimiento=2&suelo=2&riego=1&a1=0.8&a2=0.2&a3=0.8"
    //var url = ""

    lateinit var busquedaFragment: BusquedaFragment
    lateinit var graficoFragment: GraficoFragment
    lateinit var historicoFragment: HistoricoFragment



    //@SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /****Atenea****/
        //var nameParcela = arguments?.getString("Stationsname")
        var idParcela = arguments?.getString("Stationsid")
       // var idParcela1 = arguments?.getInt("Stationsid630")
        ///var idParcela2 = arguments?.getInt("Stationsid31365")
        //var idParcela3 = arguments?.getInt("Stationsid")

        //println("de 0 a 5: "+idParcela.toString())
        //println("de 6 a 30: "+idParcela1)
        //println("de 31 a 365: "+idParcela2)
       // println("mas de 366: "+idParcela3)

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
            "Algodón"       -> 1
            "Maíz Grano"    -> 2
            "Maíz Forraje"  -> 3
            else            -> "Invalid_Cultivo."
        }

        var cresClave = when (crecimientoParcela){
            "Precoz"      -> 1
            "Intermedio"  -> 2
            "Tardío"      -> 3
            else          -> "Invalid_Tipo_de_Crecimiento."
        }

        var sueloClave = when (sueloParcela){
            "Ligero"  -> 1
            "Media"   -> 2
            "Pesado"  -> 3
            else      -> "Invalid_Tipo_de_suelo."
        }

        var riegoClave = when (riegoParcela){
            "Goteo"         -> 1
            "Pivote"        -> 2
            //"Compuertas"  -> 3
            //"Gravedad"    -> 4
            else            -> "Invalid_Tipo_de_Goteo."
        }

        println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela)
       // println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="41276"&fechaIni="15052018"&fechaFin="10072018"&cultivo="1"&crecimiento="2"&suelo="2"&riego="1"&a1="0.8"&a2="0.2"&a3="0.8)
        // otro funcioan val url = "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela
        val url = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela
        println("https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela)

        /*****A.C.*******/



        val movimiento = inflater.inflate(R.layout.fragment_historico, container, false)
        var haydata = movimiento.findViewById<TextView>(R.id.textViewsindata)

        /***var  binding: ActivityHomeBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.bottomNavigation.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //binding.bottomNavigation.selectedItemId = R.id.historico
        binding.bottomNavigation.id = R.id.historico*/

        /****/
        //solovino
        val solovino = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=null&fechaIni=null&fechaFin=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null"
        //val solovinos = "https://appinifap.sytes.net/apiweb/api/riego?estacionID=null&fechaIni=null&fechaFin=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null"
        //val urlii= "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela
        //solovino
        /****/
           //getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val newtworkinfo = wificonection.getActiveNetworkInfo()


            /*/**Imagen**/
            val imgnube = movimiento.findViewById<ImageView>(R.id.nube)
            imgnube.setVisibility(View.INVISIBLE)
            /**Imagen**/*/

            if(newtworkinfo!= null && newtworkinfo.isConnected()){
                /*/**Imagen**/
                imgnube.setVisibility(View.INVISIBLE)
                println("Conectado")
                /**Imagen**/*/

                /****/
                //alertDialog


                //alertDialog
                /****/

                if(url==solovino){
                    ///revisas esto el lunes 24/06/2024
                    haydata.setVisibility(TextView.VISIBLE)
                    println("sin datos"+url)
                }else{
                    println("con datos"+url)
                    haydata.setVisibility(TextView.INVISIBLE)

                    val imperio = Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()
                    val palpatin = movimiento.findViewById<RecyclerView>(R.id.recyclerViews)
                    client.newCall(imperio).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val codeclave = response.code
                            println(codeclave)
                            if(codeclave == 400){
                                /******/
                                val solovino = "https://appinifap.sytes.net/apiweb/api/riego?estacionID=null&fechaIni=null&fechaFin=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null"
                                val urlii= "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela

                                if(urlii==solovino){
                                    haydata.setVisibility(TextView.VISIBLE)
                                    println("sin datos"+urlii)
                                }else{
                                    println("con datos"+urlii)
                                    haydata.setVisibility(TextView.INVISIBLE)

                                    //println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechaIni="+datestartParcela+"&fechaFin="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela)

                                    val imperioii = Request.Builder().url(urlii).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

                                    clients.newCall(imperioii).enqueue(object : Callback{
                                        override fun onFailure(call: Call, e: IOException) {
                                            e.printStackTrace()
                                        }

                                        override fun onResponse(call: Call, responses: Response) {
                                            val codeclaveii= responses.code
                                            if(codeclaveii == 400){
                                                activity?.runOnUiThread {
                                                    val hayii = responses.body!!.string()
                                                    val algoii = JSONObject(hayii)
                                                    val encontreii =  algoii.names().toString()
                                                    println("Encontre queso..."+encontreii)
                                                    val Howis = "[\"Message\"]"
                                                    println(Howis)
                                                    if(encontreii == Howis){
                                                        val dialog = Dialog(context as Activity)
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                        dialog.setCancelable(false)
                                                        dialog.setContentView(R.layout.alertdialog_brokenserver)
                                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                        val btnclose = dialog.findViewById<Button>(R.id.btnclose4)

                                                        btnclose.setOnClickListener {
                                                            dialog.dismiss()
                                                        }
                                                        dialog.show()
                                                    }
                                                }
                                            }else if(codeclaveii == 500){
                                                activity?.runOnUiThread {
                                                    val dialog = Dialog(context as Activity)
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                    dialog.setCancelable(false)
                                                    dialog.setContentView(R.layout.alertdialog_sindataestation)
                                                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                    val btnclose = dialog.findViewById<Button>(R.id.btnsindatastation)

                                                    btnclose.setOnClickListener {
                                                        dialog.dismiss()
                                                    }
                                                    dialog.show()
                                                }
                                            }else if(codeclaveii == 200){
                                                val hayii = responses.body!!.string()
                                                val algoii = JSONObject(hayii)
                                                val encontreii =  algoii.names().toString()
                                                val verygood = "[\"riego\"]"
                                                val notverygood = "[\"error\"]"
                                                if(encontreii == notverygood){
                                                    val senalii = algoii.getJSONObject("error").get("id")
                                                    if(senalii == 1){
                                                        val dialog = Dialog(context as Activity)
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                        dialog.setCancelable(false)
                                                        dialog.setContentView(R.layout.alertdialog_notdata)
                                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                        val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                        btnclose.setOnClickListener {
                                                            dialog.dismiss()
                                                        }
                                                        dialog.show()
                                                    }else if(senalii == 2){
                                                        val dialog = Dialog(context as Activity)
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                        dialog.setCancelable(false)
                                                        dialog.setContentView(R.layout.alertdialog_notdata)
                                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                        val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                        btnclose.setOnClickListener {
                                                            dialog.dismiss()
                                                        }
                                                        dialog.show()
                                                    }else if(senalii == 3){
                                                        val dialog = Dialog(context as Activity)
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                        dialog.setCancelable(false)
                                                        dialog.setContentView(R.layout.alertdialog_notdata)
                                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                        val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                        btnclose.setOnClickListener {
                                                            dialog.dismiss()
                                                        }
                                                        dialog.show()
                                                    }
                                                } else if(encontreii == verygood) {
                                                    if (responses.isSuccessful) {
                                                        activity?.runOnUiThread {
                                                            val senalii = algoii.getJSONObject("riego")
                                                            val cuantossonii = senalii.getJSONArray("RequerimientoRiego")
                                                            if (cuantossonii.length() == 0) {
                                                                val dialog = Dialog(context as Activity)
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                                dialog.setCancelable(false)
                                                                dialog.setContentView(R.layout.alertdialog_notserver)
                                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                                val btnclose = dialog.findViewById<Button>(R.id.btnclose3)

                                                                btnclose.setOnClickListener {
                                                                    dialog.dismiss()
                                                                }
                                                                dialog.show()
                                                            } else {
                                                                println("has tomado el camio II")
                                                                for (l in 0 until cuantossonii.length()) {
                                                                    val contacto = cuantossonii.getJSONObject(l)
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
                                        }
                                    })
                                }


                                /******/
                            }else if(codeclave === 500){
                                println("tenemos un f500")
                                //if (response.isSuccessful) {
                                    activity?.runOnUiThread {
                                        val dialog = Dialog(context as Activity)
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                        dialog.setCancelable(false)
                                        dialog.setContentView(R.layout.alertdialog_sindataestation)
                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                        val btnclose = dialog.findViewById<Button>(R.id.btnsindatastation)

                                        btnclose.setOnClickListener {
                                            dialog.dismiss()
                                        }
                                        dialog.show()
                                    }
                                //}

                            }else if(codeclave == 200){
                                        val hay = response.body!!.string()
                                        val algo = JSONObject(hay)
                                        val encontre =  algo.names().toString()
                                        val good = "[\"riego\"]"
                                        val notgood = "[\"error\"]"
                                        if(encontre==notgood){
                                            val senal = algo.getJSONObject("error").get("id")
                                            if(senal == 1){
                                                println("Error en el Key")
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_notdata)
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }else if(senal == 2){
                                                println("Error en el valor de la key")
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_notdata)
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }else if(senal == 3){
                                                println("Error en la creacion de datos")
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_notdata)
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }else if(encontre==good){
                                            if (response.isSuccessful) {
                                                activity?.runOnUiThread{
                                            val senal = algo.getJSONObject("riego")
                                            val cuantosson = senal.getJSONArray("RequerimientoRiego")
                                            println("halo 2 "+cuantosson.length())
                                            println("halo ce "+cuantosson)
                                            if(cuantosson.length()==0){
                                                println("no ahi datos")
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_notserver)
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btnclose3)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }else{
                                                println("has tomado el camio I")
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
                        }
                    })
                }



            }else{
                /**imgnube.setVisibility(View.VISIBLE)
                println("no conecado")**/

                /*val dialog = AlertDialog.Builder(context)
                 dialog.setTitle("Alerta!")
                     .setMessage("Sin Internet")
                     .setNegativeButton("Cerrar"){ dialog, which ->
                         dialog.dismiss()
                     }
                 val alertDialog: AlertDialog = dialog.create()
                 alertDialog.show()*/
                //var btndialogclose = movimiento.findViewById<Button>(R.id.btnclose1)


                val dialog = Dialog(context as Activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.alertdialog_notwifi)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val btnclose = dialog.findViewById<Button>(R.id.btnclose1)

                btnclose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }











        var back = movimiento.findViewById<ImageButton>(R.id.imageButton)
        var next = movimiento.findViewById<ImageButton>(R.id.imageButton1)


        back.setOnClickListener {
            busquedaFragment = BusquedaFragment()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.ViewHistoricoFragment,busquedaFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            next.setTransitionVisibility(View.GONE)
            back.setTransitionVisibility(View.GONE)
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
            next.setTransitionVisibility(View.GONE)
            back.setTransitionVisibility(View.GONE)
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