package com.example.riego.fragments


import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.riego.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Arrays
import kotlin.math.max


private lateinit var lineList: List<String>
private lateinit var lineDataset: LineDataSet
private lateinit var lineData: LineData



class GraficoFragment : Fragment() {

    var client = OkHttpClient()
    var header = "confidential-apiKey"
    var datestar = "01/05/2024"
    var dateend = "12/06/2024"

    //var url ="https://appinifap.sytes.net/apiweb/api/riego?estacionID=41276&fechaIni="+datestar+"&fechaFin="+dateend+"&cultivo=1&crecimiento=2&suelo=2&riego=1&a1=0.8&a2=0.2&a3=0.8"
    ///url="https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=111&fechaIni=1/5/2024&fechaFin=12/5/2024&cultivo=2&crecimiento=2&suelo=3&riego=1&a1=.5&a2=.65&a3=1.55"
    var url = "https://secrural.chihuahua.gob.mx/apiweb/api/riego?estacionID=111&fechaIni="+datestar+"&fechaFin="+dateend+"&cultivo=2&crecimiento=2&suelo=3&riego=1&a1=.5&a2=.65&a3=1.55"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val con = inflater.inflate(R.layout.fragment_grafico, container, false)
        val lolcito = okhttp3.Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

        client.newCall(lolcito).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                 e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("estamos lejos "+response.code)
                val fax = response.code
                if(fax == 400){
                   println("ERROR 400")
                }else if(fax == 500){
                    println("ERROR 500, por fechas...")
                }else if(fax == 200){
                if(response.isSuccessful){
                    activity?.runOnUiThread {
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
                                println("Error en el Key")
                            }else if(arena === 2){
                                println("Error en el valor de la key")
                            }else if(arena === 3){
                                println("Error en la creacion de datos")
                            }
                        }else if(estrellaoscura==good){
                            println("Pase fino caballero....")

                            println("mirame.... ")
                            val arena = aram.getJSONObject("riego")
                            println(arena.length())
                            val unoparatodos = arena.getJSONArray("AguaDisponible")
                            val cincovs5 = arena.getJSONArray("Abate")
                            println("JASOU " + unoparatodos)
                            println("SELECCION DE CAMPEON..." + cincovs5.length())
                            var fechas: ArrayList<String> = ArrayList()
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
                                } else if (2 === mmc.toInt()) {
                                    //println("Son Febrero " +mmc)
                                    Meses.add("Febrero")
                                    MesesNUM.add(2)
                                } else if (3 === mmc.toInt()) {
                                    //println("Son Marzo " +mmc)
                                    Meses.add("Marzo")
                                    MesesNUM.add(3)
                                } else if (4 === mmc.toInt()) {
                                    //println("Son Abril " +mmc)
                                    Meses.add("Abril")
                                    MesesNUM.add(4)
                                } else if (5 === mmc.toInt()) {
                                    //println("Son Mayo " +mmc)
                                    Meses.add("Mayo")
                                    MesesNUM.add(5)
                                } else if (6 === mmc.toInt()) {
                                    //println("Son Junio " +mmc)
                                    Meses.add("Junio")
                                    MesesNUM.add(6)
                                } else if (7 === mmc.toInt()) {
                                    //println("Son Julio " +mmc)
                                    Meses.add("Julio")
                                    MesesNUM.add(7)
                                } else if (8 === mmc.toInt()) {
                                    //println("Son Agosto " +mmc)
                                    Meses.add("Agosto")
                                    MesesNUM.add(8)
                                } else if (9 === mmc.toInt()) {
                                    //println("Son Septiembre " +mmc)
                                    Meses.add("Septiembre")
                                    MesesNUM.add(9)
                                } else if (10 === mmc.toInt()) {
                                    //println("Son Octubre " +mmc)
                                    Meses.add("Octubre")
                                    MesesNUM.add(10)
                                } else if (11 === mmc.toInt()) {
                                    //println("Son Noviembre " +mmc)
                                    Meses.add("Noviembre")
                                    MesesNUM.add(11)
                                } else if (12 === mmc.toInt()) {
                                    //println("Son Diciembre " +mmc)
                                    Meses.add("Diciembre")
                                    MesesNUM.add(12)
                                }


                                /****-Capitulo II-***/

                                for (R in 0 until cincovs5.length()) {
                                    val eye = cincovs5.getJSONObject(R)
                                    val eyevalue = eye.get("Valor")
                                    valores2.add(eyevalue.toString())
                                }
                                /****--**/
                            }
                            println(valores)
                            println(fechas)
                            println(Meses)
                            println(MesesNUM)
                            println(valores2)


                            /*****/
                            println(Meses.filter { it === "Mayo" }.size)
                            println(Meses.filter { it === "Junio" }.size)
                            /****/

                            val bodyLinesGrafic = con.findViewById<LineChart>(R.id.ViewGrafica)

                            val descripcion = Description()
                            descripcion.setText("")
                            descripcion.setPosition(150f, 55f)
                            descripcion.setTextSize(15f)
                            bodyLinesGrafic.setDescription(descripcion)
                            bodyLinesGrafic.getAxisRight().setDrawLabels(false)


                            val EjeX = bodyLinesGrafic.getXAxis()
                            EjeX.setValueFormatter(IndexAxisValueFormatter(Meses.toSet()))
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

                            val lineDateSet = LineDataSet(datagrafic as List<Entry>?, "Agua Disponible")
                            lineDateSet.setLineWidth(4f)
                            lineDateSet.setColor(Color.BLUE)
                            lineDateSet.setDrawCircles(true)
                            lineDateSet.setDrawCircleHole(true)
                            lineDateSet.setCircleColor(Color.GREEN)
                            lineDateSet.setCircleRadius(3f)
                            lineDateSet.setValueTextSize(5f)
                            //lineDateSet.setValueTextColors(Color.YELLOW)



                            val lineDateSet2 = LineDataSet(datagrafic2 as List<Entry>?, "Abate")
                            lineDateSet2.setLineWidth(4f)
                            lineDateSet2.setColor(Color.RED)
                            lineDateSet2.setDrawCircles(true)
                            lineDateSet2.setDrawCircleHole(true)
                            lineDateSet2.setCircleColor(Color.MAGENTA)
                            lineDateSet2.setCircleRadius(3f)
                            lineDateSet2.setValueTextSize(5f)

                            val linedate = LineData(lineDateSet, lineDateSet2)

                            bodyLinesGrafic.data = linedate
                            bodyLinesGrafic.setDrawBorders(true)
                            bodyLinesGrafic.invalidate()
                        }
                    }
                }
                }


            }
        })
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