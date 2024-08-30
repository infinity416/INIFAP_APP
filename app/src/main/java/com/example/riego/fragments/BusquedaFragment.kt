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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.riego.DB.Parcela
import com.example.riego.R
import com.example.riego.source.DBparcela
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.notifyAll
import org.json.JSONObject
import java.io.IOException
import DateTimeBusqueda as DateTimeBusqueda1


class BusquedaFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var mMaparcela: GoogleMap
    lateinit var historicoFragment: HistoricoFragment
    lateinit var riegoAplicFragment: RiegoAplicFragment


    /* private lateinit var databse: DBparcela
     private lateinit var parcelaLiveData: LiveData<Parcela>*/
    //private lateinit var pacelaLists : Parcela
    var parcelaList = emptyList<Parcela>()
    private lateinit var parcela: Parcela
    private lateinit var parcelaLiveData: LiveData<Parcela>
    private lateinit var parcelaLiveData2: LiveData<Parcela>

    var client = OkHttpClient()
    var sal = OkHttpClient()
    var header = "confidential-apiKey"


    var howis = arrayListOf("")
    var calve = arrayListOf("")
    var km = ""
    var inputfecha = ""
    var urfecha = ""
    var identification = toString()
    var namelat = toString()
    var namelon = toString()
    var diaff = toString()
    var diaRS = toString()
    lateinit var btnConsulta :Button

    //private lateinit var  dx : FragmentActivity
    @SuppressLint("FragmentLiveDataObserve", "NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val con = inflater.inflate(R.layout.fragment_busqueda, container, false)

        val wificonection = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val newtworkinfo = wificonection.getActiveNetworkInfo()


        if(newtworkinfo!= null && newtworkinfo.isConnected()){

            val interFecha = con.findViewById<EditText>(R.id.fechrangoInput)
            interFecha.setOnClickListener {
                val datePicker = DateTimeBusqueda1 { day, month, year ->
                    if (day < 10) {
                        if ((month + 1) <= 9) {
                            interFecha.setText("0" + day.toString() + "/" + "0" + (month + 1) + "/" + year)
                        } else {
                            interFecha.setText("0" + day.toString() + "/" + (month + 1) + "/" + year)
                        }
                    } else if ((month + 1) <= 9) {
                        interFecha.setText(day.toString() + "/" + "0" + (month + 1) + "/" + year)
                    } else {
                        interFecha.setText(day.toString() + "/" + (month + 1) + "/" + year)
                    }


                    //var nux.text = interFecha.setText(day.toString()+"/"+month.toString()+"/"+year.toString())
                }.also {
                    it.show(childFragmentManager,"datePicker")
                }

            }

            //
            //
            //
            //
            //
            //
            //
            //
            /////////////////

            //val rango = con.findViewById<EditText>(R.id.distanciaInput)
            /****ultima*****/
            /***val ultimaRFecha = con.findViewById<EditText>(R.id.fechaultimoriego)
            ultimaRFecha.setOnClickListener {
                val datePicker = DateTimeBusqueda1 { day, month, year ->
                    if (day < 10) {
                        if ((month + 1) <= 9) {
                            ultimaRFecha.setText("0" + day.toString() + "/" + "0" + (month + 1) + "/" + year)
                        } else {
                            ultimaRFecha.setText("0" + day.toString() + "/" + (month + 1) + "/" + year)
                        }
                    } else if ((month + 1) <= 9) {
                        ultimaRFecha.setText(day.toString() + "/" + "0" + (month + 1) + "/" + year)
                    } else {
                        ultimaRFecha.setText(day.toString() + "/" + (month + 1) + "/" + year)
                    }


                    //var nux.text = interFecha.setText(day.toString()+"/"+month.toString()+"/"+year.toString())
                }.also {
                    it.show(childFragmentManager,"datePicker")
                }

            }***/
            /****END ultima****/
            //
            //
            //
            //
            //
            //
            //
            //
            /////////////////

            val lisParcelas = con.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewParcela)
            val datadb = DBparcela.getDatabase(this.context as Activity)
            datadb.parcelas().consultarLocalitation().observe(viewLifecycleOwner, Observer {
                parcelaList = it

                //var howisx = arrayListOf("")
                var fechas: ArrayList<String> = ArrayList()
                var idname: ArrayList<String> = ArrayList()

                for (G in 0 until parcelaList.size){
                    //howisx.add(parcelaList.get(G).naame)
                    val velocito = parcelaList.get(G).naame
                    val velocitov2 = parcelaList.get(G).id
                    fechas.add(velocito)
                    idname.add(velocitov2.toString())
                    println("Oh!, levay...."+fechas)
                    println("Oh!, mikasa...."+idname)
                    //howisx.add(parcelaList.get(G).naame)
                    //howis.add(parcelaList.get(G).naame)
                    //val contrac = ArrayAdapter((this.context as Activity).applicationContext, R.layout.list_items, howisxl )
                    //val contrac = ArrayAdapter(context as Activity, R.layout.list_items, howisx)
                    val contrac = ArrayAdapter(context as Activity, R.layout.list_items, fechas)

                    lisParcelas.setOnItemClickListener { parent, view, position, id ->
                        val itemParcelaName = parent.getItemAtPosition(position)
                        parent.getItemAtPosition(position)
                        println("one "+ position)
                        println("two "+ id)
                        println("tree "+ itemParcelaName)
                        parcelaLiveData=datadb.parcelas().consutaParcelaName(itemParcelaName.toString())
                        parcelaLiveData.observe(this, Observer {
                            parcela = it
                            namelat = parcela.lat
                            namelon = parcela.lon
                            identification = parcela.id.toString()
                            diaff = parcela.fecha
                            val conF = parcela.fechariegosiem.get(8).toString()
                            val conF1 = parcela.fechariegosiem.get(9).toString()
                            val confDD = conF+conF1
                            val conF3 = parcela.fechariegosiem.get(5).toString()
                            val conF4 = parcela.fechariegosiem.get(6).toString()
                            val confMM = conF3+conF4
                            val conF6 = parcela.fechariegosiem.get(0).toString()
                            val conF7 = parcela.fechariegosiem.get(1).toString()
                            val conF8 = parcela.fechariegosiem.get(2).toString()
                            val conF9 = parcela.fechariegosiem.get(3).toString()
                            val confYY = conF6+conF7+conF8+conF9
                            diaRS = "$confDD/$confMM/$confYY"
                            println("LOKKKK"+namelat + namelon+" "+it.id)
                        })

                        /*val nelnel = "Gato"
                        parcelaLiveData2=datadb.parcelas().existeName(nelnel)
                        println("soy asi " +parcelaLiveData2.value?.naame?.length)*/

                            /*if(parcelaLiveData2.value?.naame.toString() == null){
                                println("no existe")

                            }else{
                                println("ya existe")
                            }*/

                        /*parcelaLiveData2.observe(this,{
                            parcela = it
                            val nunu1 = parcela.id
                            if(nunu1>0){
                                println("Ya existe el nombre")
                                return@observe
                            }else{
                                println("no existe el nombre")
                                val nunu =  parcela.naame
                                println("cooler daimon "+nunu)
                                println("king cooler "+ it)
                            }

                        })*/


                    }
                    lisParcelas.setAdapter(contrac)
                }
            })
            //
            //
            //
            //
            //
            //
            //
            //
            /////////////////


            btnConsulta = con.findViewById<Button>(R.id.btnConsulta)
            btnConsulta.setOnClickListener{
                //km = rango.text.toString()
                inputfecha = interFecha?.text.toString()
                //urfecha = ultimaRFecha?.text.toString()
                val Nparcela = lisParcelas.text.toString()

//            println("cadena de catos lat:"+parcela.lat+", lon:"+parcela.lon+", name:"+parcela.naame+", km:"+km+" y fecha:"+ interFecha?.text.toString()+"  ATT: INFINITY")

                //if(km.isEmpty()) {
                    //rango.setError("Ingrese la distancia")
                    //return@setOnClickListener
                //}else
                if(Nparcela.isEmpty()){
                    //lisParcelas.setError("Seleccione la parcela")
                    Toast.makeText(this.context, "Selecione la parcela", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }else if(inputfecha.isEmpty()){
                    interFecha.setError("Seleccione la fecha a consultar")
                    return@setOnClickListener
                }else{
                    Toast.makeText(this.context, "Buscando...", Toast.LENGTH_LONG).show()
                    createFragment()

                }
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



    private fun createFragment(){
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }





    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMaparcela = googleMap
        //println("cadena de catos lat:"+this.parcela.lat.toDouble()+", lon:"+this.parcela.lon.toDouble()+", name:"+this.parcela.naame+" y fecha:"+ this.inputfecha +"  ATT: INFINITY, id: "+parcela.id+" ok!"+identification)

        //var url = "https://appinifap.sytes.net/apiweb/api/localizar?latitud=${namelat}&longitud=${namelon}&distanciaEst=${km}"
        //println("https://appinifap.sytes.net/apiweb/api/localizar?latitud=31.117659&longitud=-106.876329&fechaIni=15/05/2023&fechaFin=10/07/2023")
        var fechaingresa = this.inputfecha
        //var ultimafechar = this.urfecha

        val url = "https://secrural.chihuahua.gob.mx/apiweb/api/localizar?latitud=$namelat&longitud=$namelon&fechaIni=$diaRS&fechaFin=$fechaingresa"
        //println("https://secrural.chihuahua.gob.mx/apiweb/api/localizar?latitud="+namelat+"&longitud="+namelon+"&fechaIni="+diaff+"&fechaFin="+fechaingresa)
        println("https://secrural.chihuahua.gob.mx/apiweb/api/localizar?latitud=$namelat&longitud=$namelon&fechaIni=$diaRS&fechaFin=$fechaingresa")


        val nes = okhttp3.Request.Builder().url(url).build()

        client.newCall(nes).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val clavemorce = response.code
                if(clavemorce === 500){
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
                }else if (clavemorce == 400){
                    //println("https://appinifap.sytes.net/apiweb/api/localizar?latitud="+namelat+"&longitud="+namelon+"&fechaIni="+diaff+"&fechaFin="+fechaingresa)
                    println("https://appinifap.sytes.net/apiweb/api/localizar?latitud=$namelat&longitud=$namelon&fechaIni=$diaRS&fechaFin=$fechaingresa")
                    val urlII = "https://appinifap.sytes.net/apiweb/api/localizar?latitud=$namelat&longitud=$namelon&fechaIni=$diaRS&fechaFin=$fechaingresa"

                    val leona = okhttp3.Request.Builder().url(urlII).build()
                    sal.newCall(leona).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, responses: Response) {
                            val ipe = responses.code
                            if(ipe === 500){
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
                            }else if(ipe == 400){
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
                            }else if(ipe == 200){
                                if (responses.isSuccessful){
                                    activity?.runOnUiThread {
                                        val lulu = responses.body!!.string()
                                        val  nautilus= JSONObject(lulu)
                                        val bilz = nautilus.getJSONArray("estacion_cercana")



                                        for(C in 0 until bilz.length()){
                                            val brand = bilz.getJSONObject(C)
                                            val rakan = brand.getString("Clasificacion")
                                            val varuz = brand.getString("EstacionName")
                                            val tresh = brand.getInt("EstacionID")
                                            val latStation2 = brand.getDouble("Latitud")
                                            val lonStation2 = brand.getDouble("Longitud")
                                            val xayah= LatLng(latStation2, lonStation2)
                                            if(rakan.toString().toInt() == 1){
                                                val ubicactionStation = MarkerOptions().position(xayah).title(tresh.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green)).anchor(0.0f, 0.0f)
                                                mMap.addMarker(ubicactionStation)
                                                mMap.animateCamera(
                                                    CameraUpdateFactory.newLatLngZoom(xayah, 8f),
                                                    15,
                                                    null
                                                )
                                                /****/
                                                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                                    @RequiresApi(Build.VERSION_CODES.Q)
                                                    @SuppressLint("ResourceType",
                                                        "SuspiciousIndentation"
                                                    )

                                                    override fun onMarkerClick(marker: Marker): Boolean {
                                                        historicoFragment = HistoricoFragment()
                                                        riegoAplicFragment = RiegoAplicFragment()
                                                        val tit1 = marker.title
                                                        println(tit1)
                                                        val args = Bundle()
                                                        //args.putString("Stationsname", nameStation.toString())
                                                        args.putString("Stationsid", tit1)
                                                        //args.putString("Stationslat", latStation.toString())
                                                        args.putInt("StationsIdParcela",parcela.id)
                                                        args.putString("StationsDateInput", inputfecha)
                                                        args.putString("StationsDateStart", urfecha)
                                                        args.putString("StationsDateSembrada", parcela.fecha )
                                                        args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                        args.putString("StationsCultivo", parcela.cultivo)
                                                        args.putString("StationsCrecimiento", parcela.crecimieto)
                                                        args.putString("StationsSuelo", parcela.suelo)
                                                        args.putString("StationsReigo", parcela.riego)
                                                        args.putString("StationsLargo", parcela.larg)
                                                        args.putString("StationsAncho", parcela.anch)
                                                        args.putString("StationsTimeR", parcela.timear)
                                                        args.putString("StationsAgua", parcela.pozo)
                                                        args.putString("StationsLGSurco", parcela.largXsurco)
                                                        args.putString("StationsGotero", parcela.gotero)
                                                        args.putString("StationsCMSurco", parcela.cmXsuko)
                                                        args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                        args.putString("StationsGGG", parcela.gastogot)
                                                        args.putString("StationsGSS", parcela.sepsurco)
                                                        args.putString("StationsGSG", parcela.sepgot)
                                                        args.putString("StationsPGA", parcela.gastoagua)
                                                        args.putString("StationsPDP", parcela.dispi)
                                                        args.putString("StationsPHR", parcela.horas)
                                                        riegoAplicFragment.arguments = args
                                                            childFragmentManager
                                                                .beginTransaction()
                                                                .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                                .commit()

                                                        btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                        return false
                                                    }
                                                })
                                                /******/
                                            }else if(rakan.toString().toInt() == 2){
                                                val ubicactionStation = MarkerOptions().position(xayah).title(tresh.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_amarillo)).anchor(0.0f, 0.0f)
                                                mMap.addMarker(ubicactionStation)
                                                mMap.animateCamera(
                                                    CameraUpdateFactory.newLatLngZoom(xayah, 8f),
                                                    15,
                                                    null
                                                )
                                                /****/
                                                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                                    @RequiresApi(Build.VERSION_CODES.Q)
                                                    @SuppressLint("ResourceType")

                                                    override fun onMarkerClick(marker: Marker): Boolean {
                                                        historicoFragment = HistoricoFragment()
                                                        riegoAplicFragment = RiegoAplicFragment()
                                                        val tit1 = marker.title
                                                        println(tit1)
                                                        val args = Bundle()
                                                        //args.putString("Stationsname", nameStation.toString())
                                                        args.putString("Stationsid", tit1)
                                                        //args.putString("Stationslat", latStation.toString())
                                                        args.putInt("StationsIdParcela",parcela.id)
                                                        args.putString("StationsDateInput", inputfecha)
                                                        args.putString("StationsDateStart", urfecha)
                                                        args.putString("StationsDateSembrada", parcela.fecha)
                                                        args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                        args.putString("StationsCultivo", parcela.cultivo)
                                                        args.putString("StationsCrecimiento", parcela.crecimieto)
                                                        args.putString("StationsSuelo", parcela.suelo)
                                                        args.putString("StationsReigo", parcela.riego)
                                                        args.putString("StationsLargo", parcela.larg)
                                                        args.putString("StationsAncho", parcela.anch)
                                                        args.putString("StationsTimeR", parcela.timear)
                                                        args.putString("StationsAgua", parcela.pozo)
                                                        args.putString("StationsLGSurco", parcela.largXsurco)
                                                        args.putString("StationsGotero", parcela.gotero)
                                                        args.putString("StationsCMSurco", parcela.cmXsuko)
                                                        args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                        args.putString("StationsGGG", parcela.gastogot)
                                                        args.putString("StationsGSS", parcela.sepsurco)
                                                        args.putString("StationsGSG", parcela.sepgot)
                                                        args.putString("StationsPGA", parcela.gastoagua)
                                                        args.putString("StationsPDP", parcela.dispi)
                                                        args.putString("StationsPHR", parcela.horas)
                                                        riegoAplicFragment.arguments = args
                                                        childFragmentManager
                                                            .beginTransaction()
                                                            .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                            .commit()

                                                        btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                        return false
                                                    }
                                                })
                                                /******/
                                            }else if(rakan.toString().toInt() == 3){
                                                val ubicactionStation = MarkerOptions().position(xayah).title(tresh.toString() ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_rojo)).anchor(0.0f, 0.0f)
                                                mMap.addMarker(ubicactionStation)
                                                mMap.animateCamera(
                                                    CameraUpdateFactory.newLatLngZoom(xayah, 8f),
                                                    15,
                                                    null
                                                )
                                                /****/
                                                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                                    @RequiresApi(Build.VERSION_CODES.Q)
                                                    @SuppressLint("ResourceType")

                                                    override fun onMarkerClick(marker: Marker): Boolean {
                                                        historicoFragment = HistoricoFragment()
                                                        riegoAplicFragment = RiegoAplicFragment()
                                                        val tit1 = marker.title
                                                        println(tit1)
                                                        val args = Bundle()
                                                        //args.putString("Stationsname", nameStation.toString())
                                                        args.putString("Stationsid", tit1)
                                                        //args.putString("Stationslat", latStation.toString())
                                                        args.putInt("StationsIdParcela",parcela.id)
                                                        args.putString("StationsDateInput", inputfecha)
                                                        args.putString("StationsDateStart", urfecha)
                                                        args.putString("StationsDateSembrada", parcela.fecha)
                                                        args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                        args.putString("StationsCultivo", parcela.cultivo)
                                                        args.putString("StationsCrecimiento", parcela.crecimieto)
                                                        args.putString("StationsSuelo", parcela.suelo)
                                                        args.putString("StationsReigo", parcela.riego)
                                                        args.putString("StationsLargo", parcela.larg)
                                                        args.putString("StationsAncho", parcela.anch)
                                                        args.putString("StationsTimeR", parcela.timear)
                                                        args.putString("StationsAgua", parcela.pozo)
                                                        args.putString("StationsLGSurco", parcela.largXsurco)
                                                        args.putString("StationsGotero", parcela.gotero)
                                                        args.putString("StationsCMSurco", parcela.cmXsuko)
                                                        args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                        args.putString("StationsGGG", parcela.gastogot)
                                                        args.putString("StationsGSS", parcela.sepsurco)
                                                        args.putString("StationsGSG", parcela.sepgot)
                                                        args.putString("StationsPGA", parcela.gastoagua)
                                                        args.putString("StationsPDP", parcela.dispi)
                                                        args.putString("StationsPHR", parcela.horas)
                                                        riegoAplicFragment.arguments = args
                                                        childFragmentManager
                                                            .beginTransaction()
                                                            .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                            .commit()

                                                        btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                        return false
                                                    }
                                                })
                                                /******/
                                            }
                                        }
                                       /*** val coordeParcela= LatLng(parcela.lat.toDouble(), parcela.lon.toDouble())
                                        val ubicactionParcela = MarkerOptions().position(coordeParcela).title(parcela.naame +", "+ coordeParcela).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tractor)).anchor(0.0f, 0.0f)
                                        mMaparcela.addMarker(ubicactionParcela )
                                        mMaparcela.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(coordeParcela, 12f),
                                            15,
                                            null,
                                        )***/
                                    }

                                }

                            }

                        }

                    })
                    return
                }else if(clavemorce == 200){

                    if(response.isSuccessful){
                        activity?.runOnUiThread {
                            val tari = response.body!!.string()
                            val  Atari= JSONObject(tari)
                            val GameCube = Atari.getJSONArray("estacion_cercana")
                            //println(GameCube.length())



                                for (E in 0 until GameCube.length()){
                                    val ND64 = GameCube.getJSONObject(E)
                                    //val kmStation = ND64.getInt("Dias")
                                    val clasification = ND64.getString("Clasificacion")
                                    val nameStation = ND64.getString("EstacionName")
                                    val idStation = ND64.getInt("EstacionID")
                                    val latStation = ND64.getDouble("Latitud")
                                    val lonStation = ND64.getDouble("Longitud")
                                    val coordeStation= LatLng(latStation, lonStation)

                                    if(clasification.toString().toInt() == 1){
                                        println("dentro de 1 "+ clasification)
                                        val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green)).anchor(0.0f, 0.0f)
                                        mMap.addMarker(ubicactionStation)
                                        mMap.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(coordeStation, 8f),
                                            15,
                                            null
                                        )
                                        /****/
                                        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                            @RequiresApi(Build.VERSION_CODES.Q)
                                            @SuppressLint("ResourceType")

                                            override fun onMarkerClick(marker: Marker): Boolean {
                                                historicoFragment = HistoricoFragment()
                                                riegoAplicFragment = RiegoAplicFragment()
                                                val tit1 = marker.title
                                                println(tit1)
                                                val args = Bundle()
                                                //args.putString("Stationsname", nameStation.toString())
                                                args.putString("Stationsid", tit1)
                                                //args.putString("Stationslat", latStation.toString())
                                                args.putInt("StationsIdParcela",parcela.id)
                                                args.putString("StationsDateInput", inputfecha)
                                                args.putString("StationsDateStart", urfecha)
                                                args.putString("StationsDateSembrada", parcela.fecha)
                                                args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                args.putString("StationsCultivo", parcela.cultivo)
                                                args.putString("StationsCrecimiento", parcela.crecimieto)
                                                args.putString("StationsSuelo", parcela.suelo)
                                                args.putString("StationsReigo", parcela.riego)
                                                args.putString("StationsLargo", parcela.larg)
                                                args.putString("StationsAncho", parcela.anch)
                                                args.putString("StationsTimeR", parcela.timear)
                                                args.putString("StationsAgua", parcela.pozo)
                                                args.putString("StationsLGSurco", parcela.largXsurco)
                                                args.putString("StationsGotero", parcela.gotero)
                                                args.putString("StationsCMSurco", parcela.cmXsuko)
                                                args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                args.putString("StationsGGG", parcela.gastogot)
                                                args.putString("StationsGSS", parcela.sepsurco)
                                                args.putString("StationsGSG", parcela.sepgot)
                                                args.putString("StationsPGA", parcela.gastoagua)
                                                args.putString("StationsPDP", parcela.dispi)
                                                args.putString("StationsPHR", parcela.horas)
                                                riegoAplicFragment.arguments = args
                                                childFragmentManager
                                                    .beginTransaction()
                                                    .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .commit()

                                                btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                return false
                                            }
                                        })
                                        /******/
                                    }else if(clasification.toString().toInt() == 2){
                                        println("dentro de 2 "+ clasification)
                                        val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_amarillo)).anchor(0.0f, 0.0f)
                                        mMap.addMarker(ubicactionStation)
                                        mMap.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(coordeStation, 8f),
                                            15,
                                            null
                                        )

                                        /****/
                                        mMap.setOnMarkerClickListener(object : OnMarkerClickListener {
                                            @RequiresApi(Build.VERSION_CODES.Q)
                                            @SuppressLint("ResourceType")
                                            override fun onMarkerClick(marker: Marker): Boolean {
                                                historicoFragment = HistoricoFragment()
                                                riegoAplicFragment = RiegoAplicFragment()
                                                val tit1 = marker.title
                                                println(tit1)
                                                val args = Bundle()
                                                //args.putString("Stationsname", nameStation.toString())
                                                args.putString("Stationsid", tit1)
                                                //args.putString("Stationslat", latStation.toString())
                                                args.putInt("StationsIdParcela",parcela.id)
                                                args.putString("StationsDateInput", inputfecha)
                                                args.putString("StationsDateStart", urfecha)
                                                args.putString("StationsDateSembrada", parcela.fecha)
                                                args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                args.putString("StationsCultivo", parcela.cultivo)
                                                args.putString("StationsCrecimiento", parcela.crecimieto)
                                                args.putString("StationsSuelo", parcela.suelo)
                                                args.putString("StationsReigo", parcela.riego)
                                                args.putString("StationsLargo", parcela.larg)
                                                args.putString("StationsAncho", parcela.anch)
                                                args.putString("StationsTimeR", parcela.timear)
                                                args.putString("StationsAgua", parcela.pozo)
                                                args.putString("StationsLGSurco", parcela.largXsurco)
                                                args.putString("StationsGotero", parcela.gotero)
                                                args.putString("StationsCMSurco", parcela.cmXsuko)
                                                args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                args.putString("StationsGGG", parcela.gastogot)
                                                args.putString("StationsGSS", parcela.sepsurco)
                                                args.putString("StationsGSG", parcela.sepgot)
                                                args.putString("StationsPGA", parcela.gastoagua)
                                                args.putString("StationsPDP", parcela.dispi)
                                                args.putString("StationsPHR", parcela.horas)
                                                riegoAplicFragment.arguments = args
                                                childFragmentManager
                                                    .beginTransaction()
                                                    .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .commit()

                                                btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                return false
                                            }
                                        })
                                        /******/
                                    }else if(clasification.toString().toInt() == 3){
                                        println("dentro de 3 "+ clasification)
                                        val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString() ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_rojo)).anchor(0.0f, 0.0f)
                                        mMap.addMarker(ubicactionStation)
                                        mMap.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(coordeStation, 8f),
                                            15,
                                            null
                                        )

                                        /****/
                                        mMap.setOnMarkerClickListener(object : OnMarkerClickListener {
                                            @RequiresApi(Build.VERSION_CODES.Q)
                                            @SuppressLint("ResourceType")
                                            override fun onMarkerClick(marker: Marker): Boolean {
                                                historicoFragment = HistoricoFragment()
                                                riegoAplicFragment = RiegoAplicFragment()
                                                val tit1 = marker.title
                                                println(tit1)
                                                val args = Bundle()
                                                //args.putString("Stationsname", nameStation.toString())
                                                args.putString("Stationsid", tit1)
                                                //args.putString("Stationslat", latStation.toString())
                                                args.putInt("StationsIdParcela",parcela.id)
                                                args.putString("StationsDateInput", inputfecha)
                                                args.putString("StationsDateStart", urfecha)
                                                args.putString("StationsDateSembrada", parcela.fecha)
                                                args.putString("StationsDateRiegoSiembra", parcela.fechariegosiem)
                                                args.putString("StationsCultivo", parcela.cultivo)
                                                args.putString("StationsCrecimiento", parcela.crecimieto)
                                                args.putString("StationsSuelo", parcela.suelo)
                                                args.putString("StationsReigo", parcela.riego)
                                                args.putString("StationsLargo", parcela.larg)
                                                args.putString("StationsAncho", parcela.anch)
                                                args.putString("StationsTimeR", parcela.timear)
                                                args.putString("StationsAgua", parcela.pozo)
                                                args.putString("StationsLGSurco", parcela.largXsurco)
                                                args.putString("StationsGotero", parcela.gotero)
                                                args.putString("StationsCMSurco", parcela.cmXsuko)
                                                args.putString("StationsCMGoteo", parcela.cmXgotero)
                                                args.putString("StationsGGG", parcela.gastogot)
                                                args.putString("StationsGSS", parcela.sepsurco)
                                                args.putString("StationsGSG", parcela.sepgot)
                                                args.putString("StationsPGA", parcela.gastoagua)
                                                args.putString("StationsPDP", parcela.dispi)
                                                args.putString("StationsPHR", parcela.horas)
                                                riegoAplicFragment.arguments = args
                                                childFragmentManager
                                                    .beginTransaction()
                                                    .replace(R.id.ViewBusquedaFragment, riegoAplicFragment)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .commit()

                                                btnConsulta.setTransitionVisibility(View.INVISIBLE)
                                                return false
                                            }
                                        })
                                        /******/
                                    }
                                    println(clasification+" vs")
                                    /*****
                                    //println(kmStation.toString())
                                    val neg = -1
                                    val k = 0
                                    val e = 5
                                    val r = 6
                                    val o = 30
                                    val p = 31
                                    val p1 = 365
                                    val i = 366
                                    val coordeStation= LatLng(latStation, lonStation)

                                    /*if(kmStation === neg){
                                    println("Fuera del Rango de Chihuahua "+kmStation)
                                    }else */
                                    if((kmStation>=k) && (kmStation<=e)){
                                    println("Dentro del rango de 0...5 " +kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green)).anchor(0.0f, 0.0f)
                                    mMap.addMarker(ubicactionStation)
                                    mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(coordeStation, 16.5f),
                                    4000,
                                    null
                                    )

                                    /****/
                                    mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    @SuppressLint("ResourceType")

                                    override fun onMarkerClick(marker: Marker): Boolean {
                                    historicoFragment = HistoricoFragment()
                                    val tit1 = marker.title
                                    println(tit1)
                                    val args = Bundle()
                                    //args.putString("Stationsname", nameStation.toString())
                                    args.putString("Stationsid", tit1)
                                    //args.putString("Stationslat", latStation.toString())
                                    args.putString("StationsDateInput", inputfecha)
                                    args.putString("StationsDateStart", parcela.fecha)
                                    args.putString("StationsCultivo", parcela.cultivo)
                                    args.putString("StationsCrecimiento", parcela.crecimieto)
                                    args.putString("StationsSuelo", parcela.suelo)
                                    args.putString("StationsReigo", parcela.riego)
                                    args.putString("StationsLargo", parcela.larg)
                                    args.putString("StationsAncho", parcela.anch)
                                    args.putString("StationsAgua", parcela.pozo)
                                    historicoFragment.arguments = args
                                    childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.ViewBusquedaFragment,historicoFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()

                                    return false
                                    }
                                    })
                                    /******/


                                    }else if((kmStation>=r) && (kmStation<=o)){
                                    println("Dentro del rango de 6...30 "+kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_amarillo)).anchor(0.0f, 0.0f)
                                    mMap.addMarker(ubicactionStation)
                                    mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(coordeStation, 16.5f),
                                    4000,
                                    null
                                    )

                                    /****/
                                    mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    @SuppressLint("ResourceType")
                                    override fun onMarkerClick(marker: Marker): Boolean {
                                    historicoFragment = HistoricoFragment()
                                    val tit1 = marker.title
                                    println(tit1)
                                    val args = Bundle()
                                    //args.putString("Stationsname", nameStation.toString())
                                    args.putString("Stationsid", tit1)
                                    //args.putString("Stationslat", latStation.toString())
                                    args.putString("StationsDateInput", inputfecha)
                                    args.putString("StationsDateStart", parcela.fecha)
                                    args.putString("StationsCultivo", parcela.cultivo)
                                    args.putString("StationsCrecimiento", parcela.crecimieto)
                                    args.putString("StationsSuelo", parcela.suelo)
                                    args.putString("StationsReigo", parcela.riego)
                                    args.putString("StationsLargo", parcela.larg)
                                    args.putString("StationsAncho", parcela.anch)
                                    args.putString("StationsAgua", parcela.agua)
                                    historicoFragment.arguments = args
                                    childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.ViewBusquedaFragment,historicoFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()

                                    return false
                                    }
                                    })
                                    /******/

                                    }else if((kmStation>=p) && (kmStation<=p1)){
                                    println("Dentro del rango de 31...365 "+kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_naranja)).anchor(0.0f, 0.0f)
                                    mMap.addMarker(ubicactionStation)
                                    mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(coordeStation, 16.5f),
                                    4000,
                                    null
                                    )

                                    /****/
                                    mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    @SuppressLint("ResourceType")
                                    override fun onMarkerClick(marker: Marker): Boolean {
                                    historicoFragment = HistoricoFragment()
                                    val tit1 = marker.title
                                    println(tit1)
                                    val args = Bundle()
                                    //args.putString("Stationsname", nameStation.toString())
                                    args.putString("Stationsid", tit1)
                                    //args.putString("Stationslat", latStation.toString())
                                    args.putString("StationsDateInput", inputfecha)
                                    args.putString("StationsDateStart", parcela.fecha)
                                    args.putString("StationsCultivo", parcela.cultivo)
                                    args.putString("StationsCrecimiento", parcela.crecimieto)
                                    args.putString("StationsSuelo", parcela.suelo)
                                    args.putString("StationsReigo", parcela.riego)
                                    args.putString("StationsLargo", parcela.larg)
                                    args.putString("StationsAncho", parcela.anch)
                                    args.putString("StationsAgua", parcela.agua)
                                    historicoFragment.arguments = args
                                    childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.ViewBusquedaFragment,historicoFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()

                                    return false
                                    }
                                    })
                                    /******/

                                    }else if(kmStation>=i){
                                    println("Dentro del rango de mas de 366 "+kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(idStation.toString() ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_rojo)).anchor(0.0f, 0.0f)
                                    mMap.addMarker(ubicactionStation)
                                    mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(coordeStation, 16.5f),
                                    4000,
                                    null
                                    )

                                    /****/
                                    mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                    @SuppressLint("ResourceType")
                                    override fun onMarkerClick(marker: Marker): Boolean {
                                    historicoFragment = HistoricoFragment()
                                    val tit1 = marker.title
                                    println(tit1)
                                    val args = Bundle()
                                    //args.putString("Stationsname", nameStation.toString())
                                    args.putString("Stationsid", tit1)
                                    //args.putString("Stationslat", latStation.toString())
                                    args.putString("StationsDateInput", inputfecha)
                                    args.putString("StationsDateStart", parcela.fecha)
                                    args.putString("StationsCultivo", parcela.cultivo)
                                    args.putString("StationsCrecimiento", parcela.crecimieto)
                                    args.putString("StationsSuelo", parcela.suelo)
                                    args.putString("StationsReigo", parcela.riego)
                                    args.putString("StationsLargo", parcela.larg)
                                    args.putString("StationsAncho", parcela.anch)
                                    args.putString("StationsAgua", parcela.agua)
                                    historicoFragment.arguments = args
                                    childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.ViewBusquedaFragment,historicoFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()

                                    return false
                                    }
                                    })
                                    /******/

                                    }*/

                                }

                            /*** val coordeParcela= LatLng(parcela.lat.toDouble(), parcela.lon.toDouble())
                            val ubicactionParcela = MarkerOptions().position(coordeParcela).title(parcela.naame +", "+ coordeParcela).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tractor)).anchor(0.0f, 0.0f)
                            mMaparcela.addMarker(ubicactionParcela)
                            mMaparcela.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(coordeParcela, 8f),
                                15,
                                null,
                            )*///

                        }
                    }
                }
            }
        })
        return
    }
}