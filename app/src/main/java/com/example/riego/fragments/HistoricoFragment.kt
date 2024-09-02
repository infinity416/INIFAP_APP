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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.Card
import com.example.riego.DB.RiegoAplicado
import com.example.riego.Historial
import com.example.riego.Home
import com.example.riego.R
import com.example.riego.databinding.ActivityHomeBinding
import com.example.riego.source.DBriegoaplic
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
    var trenfecha: ArrayList<String> = ArrayList()
    var it = emptyList<RiegoAplicado>()
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
    lateinit var riegoapliFragment: RiegoAplicFragment
    

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
        var isbnParcela = arguments?.getInt("StationsIdParcela")
        var dateinputParcela = arguments?.getString("StationsDateInput")
        var datestartParcela = arguments?.getString("StationsDateStart")
        var datesiembraParcela = arguments?.getString("StationsDateSembrada")
        var datesriegosiembra = arguments?.getString("StationsDateRiegoSiembra")
        var cultivoParcela = arguments?.getString("StationsCultivo")
        var crecimientoParcela = arguments?.getString("StationsCrecimiento")
        var sueloParcela = arguments?.getString("StationsSuelo")
        var riegoParcela = arguments?.getString("StationsReigo")

        var largoParcela = arguments?.getString("StationsLargo")
        var anchoParcela = arguments?.getString("StationsAncho")
        var TrParcela = arguments?.getString("StationsTimeR")
        var aguaParcela = arguments?.getString("StationsAgua")

        var LGsurcoParcela = arguments?.getString("StationsLGSurco")
        var goteoParcela = arguments?.getString("StationsGotero")
        var cmsurcoParcela = arguments?.getString("StationsCMSurco")
        var cmgoteroParcela = arguments?.getString("StationsCMGoteo")

        var GggParcela = arguments?.getString("StationsGGg")
        var GssParcela = arguments?.getString("StationsGSs")
        var GsgParcela = arguments?.getString("StationsGSg")

        var PgaParcela = arguments?.getString("StationsPGa")
        var PdpParcela = arguments?.getString("StationsPDp")
        //var PhrParcela = arguments?.getString("StationsPHR")

        //println("lokkk..."+dateinputParcela+datestartParcela)
        var cultivoClave = when (cultivoParcela){
            "Algodón"       -> 1
            "Maíz Grano"    -> 2
            "Maíz Forraje"  -> 3
            else            -> "Invalid_Cultivo."
        }

        var cresClave = when (crecimientoParcela){
            "Temprano"      -> 1
            "Intermedio"  -> 2
            "Tardío"      -> 3
            else          -> "Invalid_Tipo_de_Crecimiento."
        }

        var sueloClave = when (sueloParcela){
            "Ligero"  -> 1
            "Medio"   -> 2
            "Pesado"  -> 3
            else      -> "Invalid_Tipo_de_suelo."
        }

        var riegoClave = when (riegoParcela){
            "Goteo"             -> 1
            "Pivote"            -> 2
            "Avance frontal"    -> 3
            "Compuertas"        -> 6
            "Surco"             -> 7
            else            -> "Invalid_Tipo_de_Goteo."
        }
        println(" $dateinputParcela, $datestartParcela, $datesiembraParcela, $datesriegosiembra, $cultivoParcela,$crecimientoParcela,$sueloParcela,$riegoParcela,$largoParcela,$anchoParcela,$TrParcela,$aguaParcela,$LGsurcoParcela,$goteoParcela,$cmsurcoParcela,$cmgoteroParcela,$GggParcela,$GssParcela,$GsgParcela,$PgaParcela,$PdpParcela")

        /**3puntos**/
        val fcm = dateinputParcela.toString().get(0)
        val fcm1 = dateinputParcela.toString().get(1)
        val fcdd = fcm.toString()+fcm1.toString()
        val fcd = dateinputParcela.toString().get(3)
        val fcd1 = dateinputParcela.toString().get(4)
        val fcmm= fcd.toString()+fcd1.toString()
        val fcy = dateinputParcela.toString().get(6)
        val fcy1 = dateinputParcela.toString().get(7)
        val fcy2 = dateinputParcela.toString().get(8)
        val fcy3 = dateinputParcela.toString().get(9)
        val fcyyyy = fcy.toString()+fcy1.toString()+fcy2.toString()+fcy3.toString()
        var FechaNEW = "$fcyyyy-$fcmm-$fcdd"


        val rsfcm = datesriegosiembra.toString().get(8)
        val rsfcm1 = datesriegosiembra.toString().get(9)
        val rsfcdd = rsfcm.toString()+rsfcm1.toString()
        val rsfcd = datesriegosiembra.toString().get(5)
        val rsfcd1 = datesriegosiembra.toString().get(6)
        val rsfcmm= rsfcd.toString()+rsfcd1.toString()
        val rsfcy = datesriegosiembra.toString().get(0)
        val rsfcy1 = datesriegosiembra.toString().get(1)
        val rsfcy2 = datesriegosiembra.toString().get(2)
        val rsfcy3 = datesriegosiembra.toString().get(3)
        val rsfcyyyy = rsfcy.toString()+rsfcy1.toString()+rsfcy2.toString()+rsfcy3.toString()
        var rsFechaNEW = "$rsfcdd/$rsfcmm/$rsfcyyyy"
        /**3punts**/



        val movimiento = inflater.inflate(R.layout.fragment_historico, container, false)
        var haydata = movimiento.findViewById<TextView>(R.id.textViewsindata)


           //getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val newtworkinfo = wificonection.getActiveNetworkInfo()

            if(newtworkinfo!= null && newtworkinfo.isConnected()){

                val databaseriego = DBriegoaplic.getDatabase(this.context as Activity)
                databaseriego.riego_aplicado().obetenerRiegoAplic(isbnParcela!!, datesriegosiembra!!, FechaNEW ).observe(this.viewLifecycleOwner, Observer {
                    for(le in 0 until it.size){
                        val autobot = it.get(le).fecha_riego_aplicado.get(8).toString()
                        val autobot1 = it.get(le).fecha_riego_aplicado.get(9).toString()
                        val desrticonDD = autobot+autobot1
                        val autobot3 = it.get(le).fecha_riego_aplicado.get(5).toString()
                        val autobot4 = it.get(le).fecha_riego_aplicado.get(6).toString()
                        val desrticonMM = autobot3+autobot4
                        val autobot6 = it.get(le).fecha_riego_aplicado.get(0).toString()
                        val autobot7 = it.get(le).fecha_riego_aplicado.get(1).toString()
                        val autobot8 = it.get(le).fecha_riego_aplicado.get(2).toString()
                        val autobot9 = it.get(le).fecha_riego_aplicado.get(3).toString()
                        val desrticonYY = autobot6+autobot7+autobot8+autobot9
                        val rsdate = "$desrticonDD/$desrticonMM/$desrticonYY"
                        val rstime = it.get(le).hora_riego_aplicado
                        val rsdatetime = rsdate+","+rstime
                        trenfecha.add(rsdatetime)
                    }
                    val miraww = trenfecha.joinToString(",","","")

                    val url = if(riegoClave == 1){
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$GssParcela&a2=$GsgParcela&a3=$GggParcela&a4=0&riegos=$miraww"
                    }else if(riegoClave == 2){
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$PdpParcela&a2=$PgaParcela&a3=0&a4=0&riegos=$miraww"
                    }else if(riegoClave == 3){
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$largoParcela&a2=$anchoParcela&a3=$riegoParcela&a4=$TrParcela&riegos=$miraww"
                    }else if(riegoClave == 6){
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$cmsurcoParcela&a2=$LGsurcoParcela&a3=$cmgoteroParcela&a4=$goteoParcela&riegos=$miraww"
                    }else if(riegoClave == 7){
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$cmsurcoParcela&a2=$LGsurcoParcela&a3=$cmgoteroParcela&a4=$goteoParcela&riegos=$miraww"
                    } else {
                        "https://secrural.chihuahua.gob.mx/apiweb/api/riego?EstacionID=null&fechaPrimerRiego=null&fechaconsulta=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null&a4=null&riegos=null"
                    }

                    /**BODY V3 STAR**/
                    //solovino
                    val solovino = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=null&fechasiembra=null&fechaultriego=null&fechaconsulta=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null&a4=null&riegos=null"
                    if(url==solovino){
                        ///revisas esto el lunes 24/06/2024
                        haydata.setVisibility(TextView.VISIBLE)
                        println("sin datos "+url)
                    }else{
                        println("con datos "+url)
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
                                    val solovino = "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=null&fechaPrimerRiego=null&fechaconsulta=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null&a4=null&riegos=null"
                                    //val urlii= "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechasiembra="+datestartParcela+"&fechaconsulta="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela

                                    val urlii = if(riegoClave == 1){
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$GssParcela&a2=$GsgParcela&a3=$GggParcela&a4=0&riegos=$miraww"
                                    }else if(riegoClave == 2){
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$PdpParcela&a2=$PgaParcela&a3=0&a4=0&riegos=$miraww"
                                    }else if(riegoClave == 3){
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$largoParcela&a2=$anchoParcela&a3=$riegoParcela&a4=$TrParcela&riegos=$miraww"
                                    }else if(riegoClave == 6){
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$cmsurcoParcela&a2=$LGsurcoParcela&a3=$cmgoteroParcela&a4=$goteoParcela&riegos=$miraww"
                                    }else if(riegoClave == 7){
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=$idParcela&fechaPrimerRiego=$rsFechaNEW&fechaconsulta=$dateinputParcela&cultivo=$cultivoClave&crecimiento=$cresClave&suelo=$sueloClave&riego=$riegoClave&a1=$cmsurcoParcela&a2=$LGsurcoParcela&a3=$cmgoteroParcela&a4=$goteoParcela&riegos=$miraww"
                                    } else {
                                        "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=null&fechaPrimerRiego=null&fechaconsulta=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null&a4=null&riegos=null"
                                    }

                                    if(urlii==solovino){
                                        haydata.setVisibility(TextView.VISIBLE)
                                        println("sin datos"+ urlii)
                                    }else{
                                        println("con datos"+ urlii)
                                        haydata.setVisibility(TextView.INVISIBLE)

                                        //println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+idParcela+"&fechasiembra="+datestartParcela+"&fechaconsulta="+dateinputParcela+"&cultivo="+cultivoClave+"&crecimiento="+cresClave+"&suelo="+sueloClave+"&riego="+riegoClave+"&a1="+largoParcela+"&a2="+anchoParcela+"&a3="+aguaParcela)

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
                                                            val res4 = algoii.getJSONObject("error").get("info")
                                                            activity?.runOnUiThread {
                                                                val dialog =
                                                                    Dialog(context as Activity)
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                                dialog.setCancelable(false)
                                                                dialog.setContentView(R.layout.alertdialog_id1error)
                                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid1)
                                                                nel.setText(res4.toString())
                                                                dialog.window?.setBackgroundDrawable(
                                                                    ColorDrawable(Color.TRANSPARENT)
                                                                )

                                                                val btnclose =
                                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                                btnclose.setOnClickListener {
                                                                    dialog.dismiss()
                                                                }
                                                                dialog.show()
                                                            }
                                                        }else if(senalii == 2){
                                                            val res4 = algoii.getJSONObject("error").get("info")
                                                            activity?.runOnUiThread {
                                                                val dialog =
                                                                    Dialog(context as Activity)
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                                dialog.setCancelable(false)
                                                                dialog.setContentView(R.layout.alertdialog_id2error)
                                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid2)
                                                                nel.setText(res4.toString())
                                                                dialog.window?.setBackgroundDrawable(
                                                                    ColorDrawable(Color.TRANSPARENT)
                                                                )

                                                                val btnclose =
                                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                                btnclose.setOnClickListener {
                                                                    dialog.dismiss()
                                                                }
                                                                dialog.show()
                                                            }
                                                        }else if(senalii == 3){
                                                            val res4 = algoii.getJSONObject("error").get("info")
                                                            activity?.runOnUiThread {
                                                                val dialog =
                                                                    Dialog(context as Activity)
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                                dialog.setCancelable(false)
                                                                dialog.setContentView(R.layout.alertdialog_id3error)
                                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid3)
                                                                nel.setText(res4.toString())
                                                                dialog.window?.setBackgroundDrawable(
                                                                    ColorDrawable(Color.TRANSPARENT)
                                                                )

                                                                val btnclose =
                                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                                btnclose.setOnClickListener {
                                                                    dialog.dismiss()
                                                                }
                                                                dialog.show()
                                                            }
                                                        }else if(senalii == 4){
                                                            val res4 = algoii.getJSONObject("error").get("info")
                                                            activity?.runOnUiThread{
                                                                val dialog = Dialog(context as Activity)
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                                dialog.setCancelable(false)
                                                                dialog.setContentView(R.layout.alertdialog_rs)
                                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogrs)
                                                                nel.setText(res4.toString())
                                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                                val btnclose = dialog.findViewById<Button>(R.id.btnclosers)

                                                                btnclose.setOnClickListener {
                                                                    dialog.dismiss()
                                                                }
                                                                dialog.show()
                                                            }
                                                        }
                                                    } else if(encontreii == verygood) {
                                                        if (responses.isSuccessful) {
                                                            activity?.runOnUiThread {
                                                                val senalii = algoii.getJSONObject("riego")
                                                                val cuantossonii = senalii.getJSONArray("RequerimientoRiego")
                                                                val diadeaguaii = senalii.get("DiasConAgua")
                                                                val textdiaswaterii = movimiento.findViewById<TextView>(R.id.diasconagua15)
                                                                if (cuantossonii.length() == 0) {
                                                                    activity?.runOnUiThread {
                                                                        textdiaswaterii.text =
                                                                            diadeaguaii.toString()
                                                                        val dialog =
                                                                            Dialog(context as Activity)
                                                                        dialog.requestWindowFeature(
                                                                            Window.FEATURE_NO_TITLE
                                                                        )
                                                                        dialog.setCancelable(false)
                                                                        dialog.setContentView(R.layout.alertdialog_notserver)
                                                                        dialog.window?.setBackgroundDrawable(
                                                                            ColorDrawable(Color.TRANSPARENT)
                                                                        )

                                                                        val btnclose =
                                                                            dialog.findViewById<Button>(
                                                                                R.id.btnclose3
                                                                            )

                                                                        btnclose.setOnClickListener {
                                                                            dialog.dismiss()
                                                                        }
                                                                        dialog.show()
                                                                    }
                                                                } else {
                                                                    println("has tomado el camio II")
                                                                    textdiaswaterii.text = diadeaguaii.toString()
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
                                            val res4 = algo.getJSONObject("error").get("info")
                                            activity?.runOnUiThread {
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_id1error)
                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid1)
                                                nel.setText(res4.toString())
                                                dialog.window?.setBackgroundDrawable(
                                                    ColorDrawable(
                                                        Color.TRANSPARENT
                                                    )
                                                )

                                                val btnclose =
                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }else if(senal == 2){
                                            println("Error en el valor de la key")
                                            val res4 = algo.getJSONObject("error").get("info")
                                            activity?.runOnUiThread {
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_id2error)
                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid2)
                                                nel.setText(res4.toString())
                                                dialog.window?.setBackgroundDrawable(
                                                    ColorDrawable(
                                                        Color.TRANSPARENT
                                                    )
                                                )

                                                val btnclose =
                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }else if(senal == 3){
                                            println("Error en la creacion de datos")
                                            val res4 = algo.getJSONObject("error").get("info")
                                            activity?.runOnUiThread {
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_id3error)
                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogid3)
                                                nel.setText(res4.toString())
                                                dialog.window?.setBackgroundDrawable(
                                                    ColorDrawable(
                                                        Color.TRANSPARENT
                                                    )
                                                )

                                                val btnclose =
                                                    dialog.findViewById<Button>(R.id.btnclose2)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }else if(senal == 4){
                                            val res4 = algo.getJSONObject("error").get("info")
                                            activity?.runOnUiThread{
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_rs)
                                                val nel = dialog.findViewById<TextView>(R.id.msmdialogrs)
                                                nel.setText(res4.toString())
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btnclosers)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }
                                    }else if(encontre==good){
                                        if (response.isSuccessful) {
                                            activity?.runOnUiThread{
                                                val senal = algo.getJSONObject("riego")
                                                val cuantosson = senal.getJSONArray("RequerimientoRiego")
                                                val diadeagua = senal.get("DiasConAgua")
                                                val textdiaswater = movimiento.findViewById<TextView>(R.id.diasconagua15)
                                                println("halo 2 "+cuantosson.length())
                                                println("halo ce "+cuantosson)
                                                if(cuantosson.length()==0){
                                                    println("no ahi datos")
                                                    textdiaswater.text = diadeagua.toString()
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
                                                    textdiaswater.text = diadeagua.toString()
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

                    /**BODY V3 END**/

                    
                })//consulta de la cadena de fechas de rs

            }else{
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
        //var next = movimiento.findViewById<ImageButton>(R.id.imageButton1)
        var next = movimiento.findViewById<ImageButton>(R.id.imgbtnG)


        back.setOnClickListener {
            //busquedaFragment = BusquedaFragment()
            riegoapliFragment = RiegoAplicFragment()
            val sip = Bundle()
            sip.putString("Stationsid", idParcela)
            sip.putInt("StationsIdParcela",isbnParcela!!)
            sip.putString("StationsDateInput", dateinputParcela)
            sip.putString("StationsDateStart", datestartParcela)
            sip.putString("StationsDateSembrada", datesiembraParcela)
            sip.putString("StationsDateRiegoSiembra", datesriegosiembra)
            sip.putString("StationsCultivo", cultivoParcela)
            sip.putString("StationsCrecimiento", crecimientoParcela)
            sip.putString("StationsSuelo", sueloParcela)
            sip.putString("StationsReigo", riegoParcela)
            sip.putString("StationsLargo", largoParcela)
            sip.putString("StationsAncho", anchoParcela)
            sip.putString("StationsTimeR", TrParcela)
            sip.putString("StationsAgua", aguaParcela)
            sip.putString("StationsLGSurco",LGsurcoParcela )
            sip.putString("StationsGotero",goteoParcela)
            sip.putString("StationsCMSurco",cmsurcoParcela)
            sip.putString("StationsCMGoteo",cmgoteroParcela)
            sip.putString("StationsGGG",GggParcela)
            sip.putString("StationsGSS",GssParcela)
            sip.putString("StationsGSG",GsgParcela)
            sip.putString("StationsPGA",PgaParcela)
            sip.putString("StationsPDP",PdpParcela)
            //sip.putString("StationsPHR",PhrParcela)
            riegoapliFragment.arguments = sip

            childFragmentManager
                .beginTransaction()
                .replace(R.id.ViewHistoricoFragment,riegoapliFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            next.setTransitionVisibility(View.GONE)
            back.setTransitionVisibility(View.GONE)
            println("HISTORIAL A RIEGO $isbnParcela, $dateinputParcela, $datestartParcela, $datesiembraParcela,$cultivoParcela,$crecimientoParcela,$sueloParcela,$riegoParcela,$largoParcela,$anchoParcela,$TrParcela,$aguaParcela,$LGsurcoParcela,$goteoParcela,$cmsurcoParcela,$cmgoteroParcela,$GggParcela,$GssParcela,$GsgParcela,$PgaParcela,$PdpParcela")

        }



        next.setOnClickListener {
            graficoFragment = GraficoFragment()
            val sip = Bundle()
            sip.putString("Stationsid", idParcela)
            sip.putInt("StationsIdParcela",isbnParcela!!)
            sip.putString("StationsDateInput", dateinputParcela)
            sip.putString("StationsDateStart", datestartParcela)
            sip.putString("StationsDateSembrada", datesiembraParcela)
            sip.putString("StationsDateRiegoSiembra", datesriegosiembra)
            sip.putString("StationsCultivo", cultivoParcela)
            sip.putString("StationsCrecimiento", crecimientoParcela)
            sip.putString("StationsSuelo", sueloParcela)
            sip.putString("StationsReigo", riegoParcela)
            sip.putString("StationsLargo", largoParcela)
            sip.putString("StationsAncho", anchoParcela)
            sip.putString("StationsTimeR", TrParcela)
            sip.putString("StationsAgua", aguaParcela)
            sip.putString("StationsLGSurco",LGsurcoParcela )
            sip.putString("StationsGotero",goteoParcela)
            sip.putString("StationsCMSurco",cmsurcoParcela)
            sip.putString("StationsCMGoteo",cmgoteroParcela)
            sip.putString("StationsGGg",GggParcela)
            sip.putString("StationsGSs",GssParcela)
            sip.putString("StationsGSg",GsgParcela)
            sip.putString("StationsPGa",PgaParcela)
            sip.putString("StationsPDp",PdpParcela)

            graficoFragment.arguments = sip
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
