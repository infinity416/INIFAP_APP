package com.example.riego.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.riego.DB.Parcela
import com.example.riego.DateTime
import com.example.riego.R
import com.example.riego.source.DBparcela
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class BusquedaFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var mMaparcela: GoogleMap
    lateinit var historicoFragment: HistoricoFragment

   /* private lateinit var databse: DBparcela
    private lateinit var parcelaLiveData: LiveData<Parcela>*/
    //private lateinit var pacelaLists : Parcela
    var parcelaList = emptyList<Parcela>()
    private lateinit var parcela: Parcela
    private lateinit var parcelaLiveData: LiveData<Parcela>

    var client = OkHttpClient()
    var header = "confidential-apiKey"


    var howis = arrayListOf("-opciones-")
    var calve = arrayListOf("")
    var km = ""
    var inputfecha = ""

    //private lateinit var  dx : FragmentActivity
    @SuppressLint("FragmentLiveDataObserve", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val con = inflater.inflate(R.layout.fragment_busqueda, container, false)
       //
       //
       //
       //
       //
       //
       //
       //
       /////////////////
         val interFecha = con.findViewById<EditText>(R.id.fechrangoInput)
        interFecha.setOnClickListener {
            val datePicker = DateTime{ day, month, year ->
                if(day<10){
                    if((month+1) <= 9){
                        interFecha.setText("0"+day.toString()+"/"+"0"+(month+1)+"/"+year )
                    }else{
                        interFecha.setText("0"+day.toString()+"/"+(month+1)+"/"+year)
                    }
                }else if((month+1) <= 9){
                    interFecha.setText(day.toString()+"/"+"0"+(month+1)+"/"+year)
                }else{
                    interFecha.setText(day.toString()+"/"+(month+1)+"/"+year )
                }




                //var nux.text = interFecha.setText(day.toString()+"/"+month.toString()+"/"+year.toString())
            }
            datePicker.show(childFragmentManager,"datePicker")

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

        val rango = con.findViewById<EditText>(R.id.distanciaInput)



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
            for (G in 0 until parcelaList.size){
                howis.add(parcelaList.get(G).naame)
                val contrac = ArrayAdapter((this.context as Activity).applicationContext, R.layout.list_items, howis )
                lisParcelas.setOnItemClickListener { parent, view, position, id ->
                    val itemParcelaName = parent.getItemAtPosition(position)
                    parcelaLiveData=datadb.parcelas().consutaParcelaName(itemParcelaName.toString())
                    parcelaLiveData.observe(this, Observer {
                        parcela = it
                        var namelat = parcela.lat
                        val namelon = parcela.lon
                        println("LOKKKK"+namelat + namelon)
                    })
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







        //
        //
        //
        //
        //
        //
        //
        //
        /////////////////

        val btnConsulta = con.findViewById<Button>(R.id.btnConsulta)
        btnConsulta?.setOnClickListener{
             km = rango.text.toString()
            inputfecha = interFecha?.text.toString()
            val Nparcela = lisParcelas.text.toString()

//            println("cadena de catos lat:"+parcela.lat+", lon:"+parcela.lon+", name:"+parcela.naame+", km:"+km+" y fecha:"+ interFecha?.text.toString()+"  ATT: INFINITY")

            if(km.isEmpty()) {
                rango.setError("Ingrese la distancia")
                return@setOnClickListener
            }else if(Nparcela.isEmpty()){
                //lisParcelas.setError("Seleccione la parcela")
                Toast.makeText(this.context, "Selecione la parcela", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else if (inputfecha.isEmpty()){
                interFecha.setError("Seleccione la fecha")
                return@setOnClickListener
            }else{
                Toast.makeText(this.context, "Buscando...", Toast.LENGTH_LONG).show()
                btnConsulta.setTransitionVisibility(View.GONE)
                createFragment()
            }
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

        println("cadena de catos lat:"+this.parcela.lat.toDouble()+", lon:"+this.parcela.lon.toDouble()+", name:"+this.parcela.naame+", km:"+this.km+" y fecha:"+ this.inputfecha +"  ATT: INFINITY")

        var url = "https://appinifap.sytes.net/apiweb/api/localizar?latitud=${parcela.lat}&longitud=${parcela.lon}&distanciaEst=${km}"

        val nes = okhttp3.Request.Builder().url(url).build()

        client.newCall(nes).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    activity?.runOnUiThread {
                        val tari = response.body!!.string()
                        val  Atari= JSONObject(tari)
                        val GameCube = Atari.getJSONArray("estacion_cercana")
                        //println(GameCube.length())
                        if(GameCube.length() === 0){
                            println("No hay estaciones dentro de ese rango...")
                        }
                        else{
                            for (E in 0 until GameCube.length()){
                                val ND64 = GameCube.getJSONObject(E)
                                val kmStation = ND64.getInt("Dias")
                                val nameStation = ND64.getString("EstacionName")
                                val idStation = ND64.getInt("EstacionID")
                                val latStation = ND64.getInt("Latitud")
                                val lonStation = ND64.getInt("Longitud")
                                //println(kmStation.toString())
                                val neg = -1
                                val k = 0
                                val e = 5
                                val r = 6
                                val o = 30
                                val p = 31
                                val p1 = 365
                                val i = 366
                                val coordeStation= LatLng(latStation.toDouble(), lonStation.toDouble())

                                if(kmStation === neg){
                                    println("Fuera del Rango de Chihuahua "+kmStation)
                                }else if((kmStation>=k) && (kmStation<=e)){
                                    println("Dentro del rango de 0...5 " +kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(parcela.naame +", "+ nameStation +", "+ coordeStation+", "+ kmStation ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green)).anchor(0.0f, 0.0f)
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
                                            val args = Bundle()
                                            //args.putString("Stationsname", nameStation.toString())
                                            args.putInt("Stationsid", idStation)
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


                                }else if((kmStation>=r) && (kmStation<=o)){
                                    println("Dentro del rango de 6...30 "+kmStation)
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(parcela.naame +", "+ nameStation +", "+ coordeStation+", "+ kmStation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_amarillo)).anchor(0.0f, 0.0f)
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
                                            val args = Bundle()
                                            //args.putString("Stationsname", nameStation.toString())
                                            args.putInt("Stationsid", idStation)
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
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(parcela.naame +", "+ nameStation +", "+ coordeStation+", "+ kmStation ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_naranja)).anchor(0.0f, 0.0f)
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
                                            val args = Bundle()
                                            //args.putString("Stationsname", nameStation.toString())
                                            args.putInt("Stationsid", idStation)
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
                                    val ubicactionStation = MarkerOptions().position(coordeStation).title(parcela.naame +", "+ nameStation +", "+ coordeStation +", "+ kmStation ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_rojo)).anchor(0.0f, 0.0f)
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
                                            val args = Bundle()
                                            //args.putString("Stationsname", nameStation.toString())
                                            args.putInt("Stationsid", idStation)
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

                                }

                            }
                        }
                        val coordeParcela= LatLng(parcela.lat.toDouble(), parcela.lon.toDouble())
                        val ubicactionParcela = MarkerOptions().position(coordeParcela).title(parcela.naame +", "+ coordeParcela).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tractor)).anchor(0.0f, 0.0f)
                        mMaparcela.addMarker(ubicactionParcela)
                        mMaparcela.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(coordeParcela, 16.5f),
                            4000,
                            null
                        )
                    }
                }
            }
        })
        return
    }
}