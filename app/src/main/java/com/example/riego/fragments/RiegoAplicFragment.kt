package com.example.riego.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.ListFormatter.FormattedList
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.CardRiego
import com.example.riego.DB.RiegoAplicado
import com.example.riego.DatetimeRiego
import com.example.riego.R
import com.example.riego.source.DBriegoaplic
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.Format


class RiegoAplicFragment : Fragment() {

    lateinit var btnSave : Button
    lateinit var btnhistorico: ImageButton
    lateinit var btnback: ImageButton

    lateinit var historicoFragment: HistoricoFragment
    lateinit var busquedaFragment: BusquedaFragment
    lateinit var riegoaplicLiveData: LiveData<RiegoAplicado>
    var trenfecha: ArrayList<String> = ArrayList()

    var riegolist = emptyList<RiegoAplicado>()
    var inputfecha = ""
    var inputhora = ""
    var client = OkHttpClient()
    var clients = OkHttpClient()
    var header = "confidential-apiKey"


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var idParcela = arguments?.getString("Stationsid")
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

        var GggParcela = arguments?.getString("StationsGGG")
        var GssParcela = arguments?.getString("StationsGSS")
        var GsgParcela = arguments?.getString("StationsGSG")

        var PgaParcela = arguments?.getString("StationsPGA")
        var PdpParcela = arguments?.getString("StationsPDP")
        var PhrParcela = arguments?.getString("StationsPHR")

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
"https://appinifap.sytes.net/apiweb/api/riego?EstacionID=11&fechaPrimerRiego=17/04/2024&fechaconsulta=29/08/2024&cultivo=1&crecimiento=2&suelo=1&riego=2&a1=1.5&a2=100&a3=0&a4=0&riegos=[07/08/2024,54, 25/08/2024,11, 27/08/2024,152]"


        /*val frsm = datesriegosiembra.toString().get(3)
        val frsm1 = datesriegosiembra.toString().get(4)
        val frsmm = frsm.toString()+frsm1.toString()
        val frsd = datesriegosiembra.toString().get(0)
        val frsd1 = datesriegosiembra.toString().get(1)
        val frsdd= frsd.toString()+frsd1.toString()
        val frsy = datesriegosiembra.toString().get(6)
        val frsy1 = datesriegosiembra.toString().get(7)
        val frsy2 = datesriegosiembra.toString().get(8)
        val frsy3 = datesriegosiembra.toString().get(9)
        val frsyyyy = frsy.toString()+frsy1.toString()+frsy2.toString()+frsy3.toString()
        //var RSFechaNEW = frsmm+"/"+frsdd+"/"+frsyyyy
        var RSFechaNEW = frsdd+"/"+frsmm+"/"+frsyyyy
        println("$RSFechaNEW mira como gozan")
*/
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
        //var FechaNEW = fcmm+"/"+fcdd+"/"+fcyyyy
        var FechaNEW = "$fcyyyy-$fcmm-$fcdd"
        println("$FechaNEW mira como gozan")



        //var datesss: java.util.Date? =SimpleDateFormat("dd/MM/yyy").parse(datesriegosiembra)
        println("madona $datesriegosiembra")

        // Inflate the layout for this fragment
        val conexyon = inflater.inflate(R.layout.fragment_riego_aplic, container, false)


        val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val newtworkinfo = wificonection.getActiveNetworkInfo()



        if(newtworkinfo!= null && newtworkinfo.isConnected()){


            println("datos de la consulta DB: $isbnParcela, $datesriegosiembra, $FechaNEW")




            println("$inputfecha lLOOK   este dato se intserto aqui los demas son de gorra, $isbnParcela, $dateinputParcela, $datestartParcela, $datesiembraParcela,$cultivoParcela,$crecimientoParcela,$sueloParcela,$riegoParcela,$largoParcela,$anchoParcela,$TrParcela,$aguaParcela,$LGsurcoParcela,$goteoParcela,$cmsurcoParcela,$cmgoteroParcela,$GggParcela,$GssParcela,$GsgParcela,$PgaParcela,$PdpParcela")
            val fechasinsert = conexyon.findViewById<EditText>(R.id.fechariegoaplicado)
            fechasinsert.setOnClickListener {
                val datePicker = DatetimeRiego { day, month, year ->
                    if (day < 10) {
                        if ((month + 1) <= 9) {
                           fechasinsert.setText("0" + day.toString() + "/" + "0" + (month + 1) + "/" + year)
                        } else {
                           fechasinsert.setText("0" + day.toString() + "/" + (month + 1) + "/" + year)
                        }
                    } else if ((month + 1) <= 9) {
                       fechasinsert.setText(day.toString() + "/" + "0" + (month + 1) + "/" + year)
                    } else {
                       fechasinsert.setText(day.toString() + "/" + (month + 1) + "/" + year)
                    }
                }.also {
                    it.show(childFragmentManager, "datePicker")
                }
            }

            val horainsert = conexyon.findViewById<EditText>(R.id.horariegoaplicado)


            val database = DBriegoaplic.getDatabase(this.context as Activity)
            btnSave = conexyon.findViewById(R.id.btnConsulta)
            btnSave.setOnClickListener {

                val modF = fechasinsert.text.toString().get(0)
                val modF1 = fechasinsert.text.toString().get(1)
                val modDD = modF.toString()+modF1.toString()
                val modF3 = fechasinsert.text.toString().get(3)
                val modF4 = fechasinsert.text.toString().get(4)
                val modMM = modF3.toString()+modF4.toString()
                val modF6 = fechasinsert.text.toString().get(6)
                val modF7 = fechasinsert.text.toString().get(7)
                val modF8 = fechasinsert.text.toString().get(8)
                val modF9 = fechasinsert.text.toString().get(9)
                val modYY = modF6.toString()+modF7.toString()+modF8.toString()+modF9.toString()
                inputfecha = modYY+"-"+modMM+"-"+modDD

                //inputfecha = fechasinsert?.text.toString()
                inputhora = horainsert.text.toString()
                if(inputfecha.isEmpty()){
                    fechasinsert.setError("Agrege una fecha")
                    return@setOnClickListener
                }else if(inputhora.isEmpty()){
                    horainsert.setError("Agrege una hora")
                    return@setOnClickListener
                }else{
                    //Toast.makeText(this.context, "Guardado", Toast.LENGTH_LONG).show()
                    println("$inputfecha este dato se intserto aqui los demas son de gorra, $isbnParcela, $dateinputParcela, $datestartParcela, $datesiembraParcela,$cultivoParcela,$crecimientoParcela,$sueloParcela,$riegoParcela,$largoParcela,$anchoParcela,$TrParcela,$aguaParcela,$LGsurcoParcela,$goteoParcela,$cmsurcoParcela,$cmgoteroParcela,$GggParcela,$GssParcela,$GsgParcela,$PgaParcela,$PdpParcela")
                    val dateRiego = RiegoAplicado(inputfecha, inputhora, isbnParcela!!.toInt(), datesiembraParcela!!, datesriegosiembra!!)
                    println("aquiva $dateRiego")

                    val dialog = Dialog(this.context as Activity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.alertdialog_saveriego)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                    btnclose.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            database.riego_aplicado().agregarRiegoAplic(dateRiego)
                            fechasinsert.setText("")
                            horainsert.setText("")
                        }
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }

            val reach = conexyon.findViewById<RecyclerView>(R.id.recyclerviewRiegoaplic)
            val notdata = conexyon.findViewById<TextView>(R.id.textViewsindata)

            val databaseriego = DBriegoaplic.getDatabase(this.context as Activity)
            databaseriego.riego_aplicado().obetenerRiegoAplic(isbnParcela!!, datesriegosiembra!!, FechaNEW ).observe(viewLifecycleOwner, Observer{
                riegolist = it

                if(riegolist.size == 0){
                    notdata.setVisibility(TextView.VISIBLE)
                    //println("$datesriegosiembra y $dateinputParcela")
                }else{
                    notdata.setVisibility(TextView.INVISIBLE)
                    reach.layoutManager = LinearLayoutManager(context)
                    reach.adapter = CardRiego(riegolist)
                    //println("$datesriegosiembra y $dateinputParcela")
                    println("datos de la consulta DB: $isbnParcela, $datesriegosiembra, $FechaNEW")
                }
            })




            btnhistorico = conexyon.findViewById(R.id.imgbtnH)
            btnhistorico.setOnClickListener {
                historicoFragment = HistoricoFragment()
                val sip = Bundle()
                sip.putString("Stationsid", idParcela)
                sip.putInt("StationsIdParcela",isbnParcela)
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
                //sip.putString("StationsPHr",PhrParcela)

                historicoFragment.arguments = sip
                childFragmentManager

                    .beginTransaction()
                    .replace(R.id.ViewRiegosAplicFragment, historicoFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                btnhistorico.setTransitionVisibility(View.GONE)
                btnback.setTransitionVisibility(View.GONE)
                println("se van $inputfecha este dato se intserto aqui los demas son de gorra, $isbnParcela, $dateinputParcela, $datestartParcela, $datesiembraParcela,$cultivoParcela,$crecimientoParcela,$sueloParcela,$riegoParcela,$largoParcela,$anchoParcela,$TrParcela,$aguaParcela,$LGsurcoParcela,$goteoParcela,$cmsurcoParcela,$cmgoteroParcela,$GggParcela,$GssParcela,$GsgParcela,$PgaParcela,$PdpParcela")

            }

            btnback = conexyon.findViewById(R.id.imageButtonBK)
            btnback.setOnClickListener {
                busquedaFragment = BusquedaFragment()
                childFragmentManager
                    .beginTransaction()
                    .replace(R.id.ViewRiegosAplicFragment, busquedaFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                btnSave.setTransitionVisibility(View.GONE)
                btnback.setTransitionVisibility(View.GONE)
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

        btnback = conexyon.findViewById(R.id.imageButtonBK)
        btnback.setOnClickListener {
            busquedaFragment = BusquedaFragment()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.ViewRiegosAplicFragment, busquedaFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            btnSave.setTransitionVisibility(View.GONE)
            btnback.setTransitionVisibility(View.GONE)
        }


        return conexyon
    }

}