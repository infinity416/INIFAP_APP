package com.example.riego.fragments



import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.FragmentTransaction
import com.example.riego.CustomMarkerView
import com.example.riego.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


private lateinit var lineList: List<String>
private lateinit var lineDataset: LineDataSet
private lateinit var lineData: LineData



class GraficoFragment : Fragment() {

    var client = OkHttpClient()
    var DBZ = OkHttpClient()
    var header = "confidential-apiKey"
    var datestar = "01/05/2024"
    var dateend = "12/07/2024"
    lateinit var historicoFragment: HistoricoFragment
    lateinit var lineDateSet : LineDataSet
    var fechas: ArrayList<String> = ArrayList()
    //var url ="https://appinifap.sytes.net/apiweb/api/riego?estacionID=41276&fechaIni="+datestar+"&fechaFin="+dateend+"&cultivo=1&crecimiento=2&suelo=2&riego=1&a1=0.8&a2=0.2&a3=0.8"
    ///url="https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=111&fechaIni=1/5/2024&fechaFin=12/5/2024&cultivo=2&crecimiento=2&suelo=3&riego=1&a1=.5&a2=.65&a3=1.55"
    //var url = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=111&fechaIni="+datestar+"&fechaFin="+dateend+"&cultivo=2&crecimiento=2&suelo=3&riego=1&a1=.5&a2=.65&a3=1.55"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val goku= arguments?.getString("Stationsid")
        var vegeta = arguments?.getString("StationsDateInput")
        var piccolo = arguments?.getString("StationsDateStart")
        var krillin = arguments?.getString("StationsCultivo")
        var gohan = arguments?.getString("StationsCrecimiento")
        var trukns = arguments?.getString("StationsSuelo")
        var goten = arguments?.getString("StationsReigo")
        var tenshihan = arguments?.getString("StationsLargo")
        var yamcha = arguments?.getString("StationsAncho")
        var chaos = arguments?.getString("StationsAgua")


        var frezzer = when (krillin){
            "Algodón"  -> 1
            "Maíz Grano"  -> 2
            "Maíz Forraje"  -> 3
            else -> "Invalid_Cultivo."
        }

        var cellperfecto = when (gohan){
            "Precoz"  -> 1
            "Intermedio"  -> 2
            "Tardío"  -> 3
            else -> "Invalid_Tipo_de_Crecimiento."
        }

        var androides17y18 = when (trukns){
            "Ligero"  -> 1
            "Media"  -> 2
            "Pesado"  -> 3
            else -> "Invalid_Tipo_de_suelo."
        }

        var majinbu = when (goten){
            "Goteo"   -> 1
            "Pivote"  -> 2
            //""  -> 3
            else -> "Invalid_Tipo_de_Goteo."
        }
        // otro funcional val url = "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+goku+"&fechaIni="+vegeta+"&fechaFin="+piccolo+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos
        println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+goku+"&fechaIni="+piccolo+"&fechaFin="+vegeta+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos)
        println("https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID="+goku+"&fechaIni="+piccolo+"&fechaFin="+vegeta+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos)
        val url = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID="+goku+"&fechaIni="+piccolo+"&fechaFin="+vegeta+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos
        val urlIvacio = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=null&fechaIni=null&fechaFin=null&cultivo=Invalid_Cultivo.&crecimiento=Invalid_Tipo_de_Crecimiento.&suelo=Invalid_Tipo_de_suelo.&riego=Invalid_Tipo_de_Goteo.&a1=null&a2=null&a3=null"

        // Inflate the layout for this fragment
        val con = inflater.inflate(R.layout.fragment_grafico, container, false)

        val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val newtworkinfo = wificonection.getActiveNetworkInfo()

        if(newtworkinfo!= null && newtworkinfo.isConnected()){

            if(url != urlIvacio){
                val lolcito = okhttp3.Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

                client.newCall(lolcito).enqueue(object : Callback, OnChartValueSelectedListener,
                    MarkerView(context, R.layout.marker_view) {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println("estamos lejos "+response.code)
                        val fax = response.code
                        if(fax == 400){
                            println("ERROR 400")
                            //REVISAR MAÑANA 3/7/24
                            println("https://appinifap.sytes.net/apiweb/api/riego?estacionID="+goku+"&fechaIni="+piccolo+"&fechaFin="+vegeta+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos)
                            val urlII = "https://appinifap.sytes.net/apiweb/api/riego?estacionID="+goku+"&fechaIni="+piccolo+"&fechaFin="+vegeta+"&cultivo="+frezzer+"&crecimiento="+cellperfecto+"&suelo="+androides17y18+"&riego="+majinbu+"&a1="+tenshihan+"&a2="+yamcha+"&a3="+chaos
                            val urlIIvacia = "https://appinifap.sytes.net/apiweb/api/riego?estacionID=null&fechaIni=null&fechaFin=null&cultivo=null&crecimiento=null&suelo=null&riego=null&a1=null&a2=null&a3=null"

                            if(urlII != urlIIvacia){
                                val sagassj = okhttp3.Request.Builder().url(urlII).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

                                DBZ.newCall(sagassj).enqueue(object : Callback{
                                    override fun onFailure(call: Call, e: IOException) {
                                        e.printStackTrace()
                                    }

                                    override fun onResponse(call: Call, responses: Response) {
                                        val ki = responses.code

                                        if(ki == 400){
                                            activity?.runOnUiThread {
                                                val tropasfrezzer = response.body!!.string()
                                                val zabon = JSONObject(tropasfrezzer)
                                                val dodoria = zabon.names().toString()
                                                val nameku = "[\"Message\"]"
                                                if(dodoria == nameku){
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
                                        }else if(ki === 500){
                                            activity?.runOnUiThread {
                                                val dialog = Dialog(context as Activity)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.alertdialog_error500)
                                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                val btnclose = dialog.findViewById<Button>(R.id.btn500)

                                                btnclose.setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                                dialog.show()
                                            }
                                        }else if(ki == 200){
                                            val saga = responses.body!!.string()
                                            val elmasalla = JSONObject(saga)
                                            val androides = elmasalla.names().toString()
                                            val tornesdecel = "[\"riego\"]"
                                            val majinbu = "[\"error\"]"
                                            if(androides == tornesdecel){
                                                activity?.runOnUiThread{
                                                    val babidi = elmasalla.getJSONObject("riego")
                                                    val dabura = babidi.getJSONArray("AguaDisponible")
                                                    val majinbugordito = babidi.getJSONArray("Abate")
                                                    if((dabura.length() == 0) && (majinbugordito.length() == 0)){
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
                                                        for(s in 0 until dabura.length()){

                                                        }

                                                        for (e in 0 until majinbugordito.length()){
                                                            
                                                        }
                                                    }
                                                }
                                            }else if( androides == majinbu){
                                                val n20 = elmasalla.getJSONObject("error").get("id")
                                                if(n20 == 1){
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
                                                }else if(n20 == 2){
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
                                                }else if(n20 == 3){
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
                                            }
                                        }

                                    }
                                })
                            }else{

                            }






                        }else if(fax == 500){
                            activity?.runOnUiThread {
                                val dialog = Dialog(context as Activity)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.alertdialog_error500)
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                val btnclose = dialog.findViewById<Button>(R.id.btn500)

                                btnclose.setOnClickListener {
                                    dialog.dismiss()
                                }
                                dialog.show()
                            }
                        }else if(fax == 200){
                            if(response.isSuccessful){
                                //activity?.runOnUiThread {
                                val lagrieta = response.body!!.string()
                                val aram = JSONObject(lagrieta)
                                val estrellaoscura = aram.names().toString()
                                val good = "[\"riego\"]"
                                val notgood = "[\"error\"]"
                                if(estrellaoscura==notgood){
                                    println("Ahi un error")
                                    val arena = aram.getJSONObject("error").get("id")
                                    //val codeid = aram.get("id")
                                    println(arena)
                                    if(arena === 1){
                                        activity?.runOnUiThread {
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
                                    }else if(arena === 2){
                                        activity?.runOnUiThread {
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

                                    }else if(arena === 3){
                                        activity?.runOnUiThread {
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
                                    }
                                }else if(estrellaoscura==good){
                                    println("Pase fino caballero....")
                                    if(response.isSuccessful){
                                        activity?.runOnUiThread {
                                            println("mirame.... ")
                                            val arena = aram.getJSONObject("riego")
                                            println(arena.length())
                                            val unoparatodos = arena.getJSONArray("AguaDisponible")
                                            val cincovs5 = arena.getJSONArray("Abate")
                                            println("dimon " + unoparatodos.length())
                                            if(unoparatodos.length() == 0){
                                                activity?.runOnUiThread {
                                                    val dialogs = Dialog(context as Activity)
                                                    dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                    dialogs.setCancelable(false)
                                                    dialogs.setContentView(R.layout.alertdialog_notdatagap)
                                                    dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                                    val btnclose = dialogs.findViewById<Button>(R.id.btnsindatagrap)

                                                    btnclose.setOnClickListener {
                                                        dialogs.dismiss()
                                                    }
                                                    dialogs.show()
                                                }
                                            }else{
                                                println("JASOU " + unoparatodos)
                                                println("SELECCION DE CAMPEON..." + cincovs5.length())

                                                var valores: ArrayList<String> = ArrayList()
                                                var Meses: ArrayList<String> = ArrayList()
                                                var MesesNUM: ArrayList<Int> = ArrayList()
                                                var valores2: ArrayList<String> = ArrayList()
                                                for (S in 0 until unoparatodos.length()) {
                                                    val velocito = unoparatodos.getJSONObject(S)
                                                    val velocitodate = velocito.get("Fecha")
                                                    val velocitovalue = velocito.get("Valor")
                                                    fechas.add(velocitodate.toString())
                                                    valores.add(velocitovalue.toString())
                                                    //println("solo sabes SALTAR:::: "+velocito)
                                                    // println("Es hora de bailar.... "+ velocitodate)
                                                    //println("SU.. Su.. Su KI es MAS DE " +velocitovalue+"!!!!!, y Aumentado")
                                                    val mC1 = velocitodate.toString().get(3)
                                                    val mC2 = velocitodate.toString().get(4)
                                                    ///println("FU..."+mC1+"SION...."+mC2+" HAAAAA!!!!!"+mC1+mC2)
                                                    val mmc = mC1.toString() + mC2.toString()
                                                    //println(mmc)
                                                    if (mmc.toInt() == 1) {
                                                        //println("Son Enero" +mmc)
                                                        Meses.add("Enero")
                                                        MesesNUM.add(1)
                                                    } else if (2 == mmc.toInt()) {
                                                        //println("Son Febrero " +mmc)
                                                        Meses.add("Febrero")
                                                        MesesNUM.add(2)
                                                    } else if (3 == mmc.toInt()) {
                                                        //println("Son Marzo " +mmc)
                                                        Meses.add("Marzo")
                                                        MesesNUM.add(3)
                                                    } else if (4 == mmc.toInt()) {
                                                        //println("Son Abril " +mmc)
                                                        Meses.add("Abril")
                                                        MesesNUM.add(4)
                                                    } else if (5 == mmc.toInt()) {
                                                        //println("Son Mayo " +mmc)
                                                        Meses.add("Mayo")
                                                        MesesNUM.add(5)
                                                    } else if (6 == mmc.toInt()) {
                                                        //println("Son Junio " +mmc)
                                                        Meses.add("Junio")
                                                        MesesNUM.add(6)
                                                    } else if (7 == mmc.toInt()) {
                                                        //println("Son Julio " +mmc)
                                                        Meses.add("Julio")
                                                        MesesNUM.add(7)
                                                    } else if (8 == mmc.toInt()) {
                                                        //println("Son Agosto " +mmc)
                                                        Meses.add("Agosto")
                                                        MesesNUM.add(8)
                                                    } else if (9 == mmc.toInt()) {
                                                        //println("Son Septiembre " +mmc)
                                                        Meses.add("Septiembre")
                                                        MesesNUM.add(9)
                                                    } else if (10 == mmc.toInt()) {
                                                        //println("Son Octubre " +mmc)
                                                        Meses.add("Octubre")
                                                        MesesNUM.add(10)
                                                    } else if (11 == mmc.toInt()) {
                                                        //println("Son Noviembre " +mmc)
                                                        Meses.add("Noviembre")
                                                        MesesNUM.add(11)
                                                    } else if (12 == mmc.toInt()) {
                                                        //println("Son Diciembre " +mmc)
                                                        Meses.add("Diciembre")
                                                        MesesNUM.add(12)
                                                    }
                                                }
                                                /****-Capitulo II-***/

                                                for (R in 0 until cincovs5.length()) {
                                                    val eye = cincovs5.getJSONObject(R)
                                                    val eyevalue = eye.get("Valor")
                                                    valores2.add(eyevalue.toString())
                                                }
                                                /****--**/
                                                println(valores)
                                                println(fechas)
                                                println(Meses)
                                                println(MesesNUM)
                                                println(valores2.size)
                                                println(valores2)


                                                /*****/
                                                println(Meses.filter { it === "Mayo" }.size)
                                                println(Meses.filter { it === "Junio" }.size)
                                                /****/

                                                val bodyLinesGrafic = con.findViewById<LineChart>(R.id.ViewGrafica)



                                                val descripcion = Description()
                                                descripcion.setText("Lamina de riego(mm)")
                                                descripcion.setPosition(310f, 38f)
                                                descripcion.setTextSize(10f)
                                                bodyLinesGrafic.setDescription(descripcion)
                                                bodyLinesGrafic.getAxisRight().setDrawLabels(false)


                                                val EjeX = bodyLinesGrafic.getXAxis()
                                                EjeX.setValueFormatter(IndexAxisValueFormatter(fechas))
                                                EjeX.setCenterAxisLabels(true)
                                                EjeX.setPosition(XAxis.XAxisPosition.BOTTOM)
                                                EjeX.setGranularity(1f)
                                                EjeX.setGranularityEnabled(true)

                                                bodyLinesGrafic.setDragEnabled(true)
                                                bodyLinesGrafic.setVisibleXRangeMaximum(1f)



                                                val datagrafic : ArrayList<BarEntry> =  ArrayList()
                                                val datagrafic2 : ArrayList<BarEntry> =  ArrayList()

                                                for(I in 0 until valores.size){
                                                    val value7w7 = valores.get(I).toFloat()
                                                    val valueUwU = valores2.get(I).toFloat()
                                                    val i = I.toFloat()
                                                    datagrafic.add(BarEntry(i, value7w7))
                                                    datagrafic2.add(BarEntry(i, valueUwU))
                                                }

                                                //val conjunto1 = BarDataSet(datagrafic, "AguaDisponible")

                                                lineDateSet = LineDataSet(datagrafic as List<Entry>?, "Agua Disponible")
                                                lineDateSet.setLineWidth(4f)
                                                lineDateSet.setColor(Color.BLUE)
                                                lineDateSet.setDrawFilled(true)
                                                lineDateSet.setFillDrawable(Color.argb(60,108, 64,205).toDrawable())
                                                //lineDateSet.valueTextColor = Color.GREEN
                                                lineDateSet.setDrawCircles(true)
                                                lineDateSet.setDrawCircleHole(true)
                                                lineDateSet.setCircleColor(Color.GREEN)
                                                lineDateSet.setCircleRadius(3f)
                                                lineDateSet.setValueTextSize(5f)
                                                //lineDateSet.setDrawValues(false)
                                                lineDateSet.setDrawValues(true)



                                                //lineDateSet.setValueTextColors(Color.YELLOW)



                                                val lineDateSet2 = LineDataSet(datagrafic2 as List<Entry>?, "Abate")
                                                lineDateSet2.setLineWidth(4f)
                                                lineDateSet2.setColor(Color.RED)
                                                lineDateSet2.setDrawFilled(true)
                                                lineDateSet2.setFillDrawable(Color.argb(60,158, 64,104).toDrawable())
                                                lineDateSet2.setDrawCircles(true)
                                                lineDateSet2.setDrawCircleHole(true)
                                                lineDateSet2.setCircleColor(Color.MAGENTA)
                                                lineDateSet2.setCircleRadius(3f)
                                                lineDateSet2.setValueTextSize(5f)
                                                lineDateSet2.setValueTextSize(5f)
                                                lineDateSet.setValueTextColor(Color.BLACK)


                                                val linedate = LineData(lineDateSet, lineDateSet2)


                                                //bodyLinesGrafic.setNoDataText("No hay datos que graficar...")
                                                //bodyLinesGrafic.setNoDataTextColor(Color.BLACK)
                                                bodyLinesGrafic.data = linedate
                                                bodyLinesGrafic.setPinchZoom(true)
                                                bodyLinesGrafic.setOnChartValueSelectedListener(this)
                                                bodyLinesGrafic.setDrawBorders(true)
                                                bodyLinesGrafic.setDrawMarkers(true)
                                                //bodyLinesGrafic.setDrawMarkerViews(true)
                                                bodyLinesGrafic.invalidate()
                                            }
                                        }
                                    }

                                }
                                //}
                            }
                        }
                    }



                    @SuppressLint("MissingInflatedId", "ResourceType")
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        val nel = (e?.y).toString()
                        val nel2 = (e?.x)?.toInt()
                        val ñal = fechas.get(nel2!!.toInt())
                        Toast.makeText(context, "${nel}, ${ñal}", Toast.LENGTH_SHORT).show()
                        val txtvalue = con.findViewById<TextView>(R.id.valueview)
                        txtvalue.text = nel+"-"+ñal

                        //val MarkerView = con.findViewById<MarkerView>(R.layout.marker_view)
                        //val nel2 = findViewById<TextView>(R.id.valueview)
                        //super.refreshContent(e, h)
                        println( fechas.get(nel2!!.toInt()))
                    }

                    override fun onNothingSelected() {
                        println("no seleciono punto.")
                    }

                    /*@SuppressLint("MissingInflatedId", "ResourceType")

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            val nel = (e?.y).toString()
                            //val MarkerView = con.findViewById<MarkerView>(R.layout.marker_view)
                            val textview = con.findViewById<TextView>(R.id.marker_text)
                            textview.text = nel
                            val nel2 = con.findViewById<TextView>(R.id.valueview)
                            println(nel)
                        }

                        override fun onNothingSelected() {

                        }*/

                })

            }else{
                println("Vacio")
            }


            var batk = con.findViewById<FloatingActionButton>(R.id.btnBackGRafp)

            batk.setOnClickListener {
                historicoFragment = HistoricoFragment()
                childFragmentManager
                    .beginTransaction()
                    .replace(R.id.ViewGraficoFragment,historicoFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
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

        return con
    }
}



/*val lineChart = con.findViewById<LineChart>(R.id.ViewGrafica)

val descripcion = Description()
descripcion.setText("Xbox")
descripcion.setPosition(150f, 15f)
lineChart.setDescription(descripcion)
lineChart.getAxisRight().setDrawLabels(false)

lineList = Arrays.asList("Jhon","Linda","Kelly","Fred")

val lxAxis = lineChart.getXAxis()
lxAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
lxAxis.setValueFormatter(IndexAxisValueFormatter(lineList))
lxAxis.setLabelCount(4)
lxAxis.setAxisLineWidth(2f)
lxAxis.setAxisLineColor(Color.GREEN)
lxAxis.setGranularity(1f)

val yAxis = lineChart.getAxisLeft()
yAxis.setAxisMinimum(0f)
yAxis.setAxisMaximum(100f)
yAxis.setAxisLineWidth(2f) //grosos de la liena
yAxis.setAxisLineColor(Color.GREEN) //color de la linea
yAxis.setLabelCount(10)


val entries1 = ArrayList<BarEntry>()
entries1.add(BarEntry(0f, 10f))
entries1.add(BarEntry(1f,10f))
entries1.add(BarEntry(2f,15f))
entries1.add(BarEntry(3f,45f))
entries1.add(BarEntry(4f,55f))
entries1.add(BarEntry(5f,35f))


val entries2 = ArrayList<BarEntry>()
entries2.add(BarEntry(0f,0f))
entries2.add(BarEntry(1f,5f))
entries2.add(BarEntry(2f,10f))
entries2.add(BarEntry(3f,15f))
entries2.add(BarEntry(4f,25f))
entries2.add(BarEntry(5f,35f))

val dataset1 = LineDataSet(entries1 as List<com.github.mikephil.charting.data.Entry>?,  "Bajas")
//dataset1.setColor(Color.BLUE)
val dataset2 = LineDataSet(entries2 as List<com.github.mikephil.charting.data.Entry>?,  "Años")
//dataset2.setColor(Color.RED)

val lineDataset1 = LineDataSet(dataset1.values, dataset1.label)

val lineDataset2 = LineDataSet(dataset2.values, dataset2.label)

lineData = LineData(lineDataset1, lineDataset2)
lineChart.data = lineData

lineChart.invalidate()*/