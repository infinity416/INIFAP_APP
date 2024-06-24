package com.example.riego.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.CardABC
import com.example.riego.DB.Parcela
import com.example.riego.FormParcela
import com.example.riego.R
import com.example.riego.source.DBparcela
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ParcelasFragment : Fragment() {

    /*val cultivo = arrayOf("Frijol","Maíz","Manzano","Nogal")
    val suelo = arrayOf("Ligero","Media","Pesado")
    val rigo = arrayOf("Avance frontal","Compuertas","Goteo","Microaspersión","Pivote")

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText*/
    var parcelaList = emptyList<Parcela>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val conextion= inflater.inflate(R.layout.fragment_parcelas, container, false)

        val unsc = conextion.findViewById<RecyclerView>(R.id.recyclerList)
        var hayresgitro = conextion.findViewById<TextView>(R.id.textViewsinRegistros)

        val databaseparce = DBparcela.getDatabase(this.context as Activity)
        databaseparce.parcelas().obtenerParcela().observe(viewLifecycleOwner, Observer {
            parcelaList = it

            if(parcelaList.size==0){
                hayresgitro.setVisibility(TextView.VISIBLE)
            }else{
                hayresgitro.setVisibility(TextView.INVISIBLE)
                unsc.layoutManager = LinearLayoutManager(context)
                unsc.adapter = CardABC(parcelaList)
                println("aca......."+parcelaList.size)
            }


            val floatbtn = conextion.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            floatbtn.setOnClickListener {
                /*val nexts = ParcelasFragment()
                val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.main,nexts )
                transaction.commit()*/
                val next = Intent(this.context, FormParcela::class.java)
                startActivity(next)
            }
        })


       /* val culList = conextion.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val sueList = conextion.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
        val rieList = conextion.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)

        val adapter = this.context?.let { ArrayAdapter(it.applicationContext, R.layout.list_items, cultivo) }
        val adapter1 = this.context?.let { ArrayAdapter(it.applicationContext, R.layout.list_items, suelo) }
        val adapter2 = this.context?.let { ArrayAdapter(it.applicationContext, R.layout.list_items, rigo) }

        culList.setAdapter(adapter)
        sueList.setAdapter(adapter1)
        rieList.setAdapter(adapter2)

        culList.setOnItemClickListener { parent, view, position, id ->
            val culEle = parent.getItemAtPosition(position)
            Toast.makeText(context, "Has elegido $culEle", Toast.LENGTH_LONG).show()
        }

        sueList.setOnItemClickListener { parent, view, position, id ->
            val sueEle = parent.getItemAtPosition(position)
            Toast.makeText(context, "Has elegido $sueEle", Toast.LENGTH_LONG).show()
        }

        rieList.setOnItemClickListener { parent, view, position, id ->
            val rieEle = parent.getItemAtPosition(position)
            Toast.makeText(context, "Has elegido $rieEle", Toast.LENGTH_LONG).show()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.context as Activity)
        latitude = conextion.findViewById(R.id.latitudInput)
        longitude = conextion.findViewById(R.id.longuitdInput)
        val btnLoc = conextion.findViewById<ImageButton>(R.id.buttonUbication)


        btnLoc.setOnClickListener {
            locationGPS()
        }*/
        
        return conextion
    }

   /* private fun locationGPS() {
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED &&
            this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED) {
            this.context?.let {
                ActivityCompat.requestPermissions(
                    it as Activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    100

                )
                //parentFragment?.onDestroy()
            }
            return
        }

        val location = fusedLocationProviderClient.lastLocation

        location.addOnSuccessListener {
            if (it != null) {
                val lat = it.latitude.toString()
                val lon = it.longitude.toString()
                println("vamos haber")
                println(lat.plus(longitude.text))
                println(lon.plus(longitude.text))
                latitude.setText(lat)
                longitude.setText(lon)
            }
        }
    }*/
}