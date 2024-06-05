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
import androidx.lifecycle.Observer
import com.example.riego.DB.Parcela
import com.example.riego.R
import com.example.riego.source.DBparcela
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class BusquedaFragment : Fragment(), OnMapReadyCallback  {


    private lateinit var mMap: GoogleMap
   /* private lateinit var databse: DBparcela
    private lateinit var parcelaLiveData: LiveData<Parcela>*/
    //private lateinit var pacelaLists : Parcela
    var parcelaList = emptyList<Parcela>()
    private lateinit var parcela: Parcela

    var client = OkHttpClient()
    var header = "confidential-apiKey"

    //private lateinit var  dx : FragmentActivity
    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val con = inflater.inflate(R.layout.fragment_busqueda, container, false)

       val lisParcelas = con.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewParcela)
        val datadb = DBparcela.getDatabase(this.context as Activity)
var howis = arrayListOf("",)
        datadb.parcelas().consultarLocalitation().observe(viewLifecycleOwner, Observer {
            parcelaList = it
            for (G in 0 until parcelaList.size) {
                  howis.add(parcelaList.get(G).naame)

                println(howis)
                val adapter3 = ArrayAdapter((this.context as Activity).applicationContext, R.layout.list_items, howis)
                lisParcelas.setAdapter(adapter3)
            }
            //val adapter3 = this.let {ArrayAdapter((this.context as Activity).applicationContext, R.layout.list_items, Why)}
            //lisParcelas.setAdapter(adapter3)
        })


        createFragment()
        return con
    }

    private fun createFragment(){
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        val datadb = DBparcela.getDatabase(this.context as Activity)
        datadb.parcelas().consultarLocalitation().observe(this, Observer {
            parcelaList = it
            for (l in 0 until parcelaList.size){
                val tripleH = parcelaList.get(l).lat
                val shawnMichaels = parcelaList.get(l).lon
                val edge = parcelaList.get(l).cultivo
                val davidrayan = parcelaList.get(l).naame
                println(tripleH + shawnMichaels)
                val cordinates = LatLng(tripleH.toDouble(), shawnMichaels.toDouble())

                ///
                val url ="https://appinifap.sytes.net/apiweb/api/localizar?latitud=${tripleH.toDouble()}&longitud=${shawnMichaels.toDouble()}&distanciaEst=250"
                println(url)
                val raw = okhttp3.Request.Builder().url(url).header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()
                client.newCall(raw).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful){
                            activity?.runOnUiThread {
                                val wwe = response.body!!.string()
                                val dx = JSONObject(wwe)
                                val yesyes = dx.getJSONArray("estacion_cercana")
                                for (E in 0 until yesyes.length()){
                                    val one23 = yesyes.getJSONObject(E)
                                    //println(one23.getString("EstadoName"))
                                    val nchi = one23.getString("EstadoName")
                                    val ties = one23.getString("EstadoID")
                                    val Cday = one23.getString("Dias")
                                    val ply1 = one23.getString("Latitud")
                                    val ply2 = one23.getString("Longitud")
                                    val douvsduo = LatLng(ply1.toDouble(), ply2.toDouble())

                                    val ch = "Chihuahua"
                                    if(ch==nchi){
                                        if(Cday >= 1.toString()){
                                            //println(Cday + cordinates + douvsduo)
                                            if(cordinates.latitude <= douvsduo.latitude){
                                                if(cordinates.longitude <= douvsduo.longitude){
                                                   println("DEntro del if "+ cordinates.toString()+" " + douvsduo.toString() + Cday.toString())
                                                }
                                                //println(cordinates.toString() + douvsduo.toString())
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                })

                ///

                val puntodeextracion = MarkerOptions().position(cordinates).title(edge +", "+ davidrayan +", "+ cordinates ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tractor)).anchor(0.0f, 0.0f)
                mMap.addMarker(puntodeextracion)
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(cordinates, 16.5f),
                    4000,
                    null
                )
            }


        })

        /*databse = DBparcela.getDatabase(this.context as Activity)
        println("HOLA "+databse)
        parcelaLiveData = databse.parcelas().consultarLocalitation()
        println("ADIOS!!!!!!!! "+parcelaLiveData)
        parcelaLiveData.observe(this, Observer {
            pacelaList = it
            println("AQUI!!!!!! "+ this.pacelaList)
            println("AQUI!!!!!! "+ this.pacelaList.lat)
            println("AQUI!!!!!! "+ this.pacelaList.lon)
            val tipleH = this.pacelaList.lat
            val showmikey = this.pacelaList.lon
            val cordinates = LatLng(tipleH.toDouble(), showmikey.toDouble())
            val puntodeextracion = MarkerOptions().position(cordinates).title(cordinates.toString())
            mMap.addMarker(puntodeextracion)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(cordinates, 16.5f),
                4000,
                null
            )

        })*/
        // parcelaLiveData = databse.parcelas().consultarLocalitation()
        /*parcelaLiveData.observe( this, Observer {
                pacelaList = it
                pacelaList.lat
                println(pacelaList.lat)
            })*/

       /* val cordinates = LatLng(37.423423, -122.083952)
        //val cordinates = LatLng(19.8077463, -99.4070038)
        val puntodeextracion = MarkerOptions().position(cordinates).title(cordinates.toString())
        mMap.addMarker(puntodeextracion)
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(cordinates, 16.5f),
            4000,
            null
        )*/
        return
    }
}


