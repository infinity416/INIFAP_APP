package com.example.riego

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.riego.DB.Parcela
import com.example.riego.source.DBparcela
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormParcela : AppCompatActivity() {

    val cultivo = arrayOf("Maíz Grano","Maíz Forraje")
    val cresimiento = arrayOf("Precoz", "Intermedio", "Tardío")
    val suelo = arrayOf("Ligero","Media","Pesado")
    val rigo = arrayOf("Goteo")


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText

    private lateinit var databse: DBparcela
    private lateinit var parcela: Parcela
    private lateinit var parcelaLiveData: LiveData<Parcela>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_parcela)
        val lisCultivo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val lisSuelo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
        val lisRiego = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        val lisCresimiento = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)

        val adapter = this.let { ArrayAdapter(it.applicationContext, R.layout.list_items, cultivo) }
        val adapter1 = this.let { ArrayAdapter(it.applicationContext, R.layout.list_items, suelo) }
        val adapter2 = this.let { ArrayAdapter(it.applicationContext, R.layout.list_items, rigo) }
        val adapter3 = this.let {ArrayAdapter(it.applicationContext, R.layout.list_items, cresimiento)}

        lisCultivo.setAdapter(adapter)
        lisSuelo.setAdapter(adapter1)
        lisRiego.setAdapter(adapter2)
        lisCresimiento.setAdapter(adapter3)

        lisCultivo.setOnItemClickListener { parent, view, position, id ->
            val itemCultivo = parent.getItemAtPosition(position)
            Toast.makeText(this, "Has elegido $itemCultivo", Toast.LENGTH_LONG).show()
        }
        lisSuelo.setOnItemClickListener { parent, view, position, id ->
            val itemSuelo = parent.getItemAtPosition(position)
            Toast.makeText(this, "Has elegido $itemSuelo", Toast.LENGTH_LONG).show()
        }

        lisRiego.setOnItemClickListener { parent, view, position, id ->
            val itemRiego = parent.getItemAtPosition(position)
            Toast.makeText(this, "Has elegido $itemRiego", Toast.LENGTH_LONG).show()
        }

        lisCresimiento.setOnItemClickListener { parent, view, position, id ->
            val itemCrecimiento = parent.getItemAtPosition(position)
            Toast.makeText(this, "Has elegido $$itemCrecimiento", Toast.LENGTH_LONG).show()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        latitude = findViewById(R.id.latitudInput)
        longitude = findViewById(R.id.longuitdInput)
        val btnLoc = findViewById<ImageButton>(R.id.buttonUbication)
        btnLoc.setOnClickListener {
            locationGPS()
        }

        /*val floatbtn = findViewById<Button>(R.id.btnexit)
        floatbtn.setOnClickListener{
                finish()
        }*/

        //
        var idParcela = intent.getIntExtra("id", 0)
        val input1 = findViewById<EditText>(R.id.nameInput)
        val input2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val input3 = findViewById<EditText>(R.id.latitudInput)
        val input4 = findViewById<EditText>(R.id.longuitdInput)
        val input5 = findViewById<EditText>(R.id.dateInput)
        val input13 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)
        val input6 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
        val input7 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        val input8 = findViewById<EditText>(R.id.aguaInput)
        val input9 = findViewById<EditText>(R.id.largoInput)
        val input10 = findViewById<EditText>(R.id.anchoInput)
        val input11 = findViewById<EditText>(R.id.timeInput)
        println("Comando!!!! Comando!!!")
        println(idParcela)
        //
if (idParcela != 0){
    databse= DBparcela.getDatabase(this)
    parcelaLiveData = databse.parcelas().consutaParcela(idParcela)
    parcelaLiveData.observe(this, Observer {
        parcela = it
        //var n1 =input1.setText(parcela.naame).toString()
        var clock1 = input1.setText(parcela.naame).toString()
        var z =parcela.naame
        input2.setText(parcela.cultivo)
        input3.setText(parcela.lat)
        input4.setText(parcela.lon)
        input5.setText(parcela.fecha)
        input13.setText(parcela.crecimieto)
        input6.setText(parcela.riego)
        input7.setText(parcela.suelo)
        input8.setText(parcela.agua)
        input9.setText(parcela.larg)
        input10.setText(parcela.anch)
        input11.setText(parcela.hora)
    })
   /* if (intent.hasExtra("parcela")){
        var actparcela = intent.extras?.getSerializable("parcela") as Parcela
        //var n1 = input1.setText(actparcela.naame).toString()
        actparcela.naame = input1.setText(parcela.naame).toString()
        input3.setText(actparcela.lat)
        input4.setText(actparcela.lon)
        input5.setText(actparcela.fecha)
        input13.setText(actparcela.crecimieto)
        input6.setText(actparcela.riego)
        input7.setText(actparcela.suelo)
        input8.setText(actparcela.agua)
        input9.setText(actparcela.larg)
        input10.setText(actparcela.anch)
        input11.setText(actparcela.hora)
        idParcela = actparcela.id
    }*/
    val savebtn = findViewById<Button>(R.id.saveBtn)
    savebtn.setOnClickListener {
        CoroutineScope(Dispatchers.IO).launch {
            val nombre = input1.text.toString()
            val cultivo = input2.text.toString()
            val lat = input3.text.toString()
            val lon = input4.text.toString()
            val dia = input5.text.toString()
            val creci = input13.text.toString()
            val triego = input6.text.toString()
            val tsuelo = input7.text.toString()
            val agua = input8.text.toString()
            val largo = input9.text.toString()
            val ancho = input10.text.toString()
            val hora = input11.text.toString()
            parcela.id = idParcela
            parcela.naame = nombre
            parcela.cultivo = cultivo
            parcela.lat = lat
            parcela.lon = lon
            parcela.fecha = dia
            parcela.crecimieto = creci
            parcela.riego = triego
            parcela.suelo = tsuelo
            parcela.agua = agua
            parcela.larg = largo
            parcela.anch = ancho
            parcela.hora = hora


            println("HOLAAAAAAAAAAAAA!!!!!!!!!!")
            println(parcela)
            databse.parcelas().editarParcela(parcela)
        }
    }


}else{

    val database = DBparcela.getDatabase(this)

    val savebtn = findViewById<Button>(R.id.saveBtn)
    savebtn.setOnClickListener {

        val nombre = input1.text.toString()
        val cultivo = input2.text.toString()
        val lat = input3.text.toString()
        val lon = input4.text.toString()
        val dia = input5.text.toString()
        val creci = input13.text.toString()
        val triego = input6.text.toString()
        val tsuelo = input7.text.toString()
        val agua = input8.text.toString()
        val largo = input9.text.toString()
        val ancho = input10.text.toString()
        val hora = input11.text.toString()
        println(nombre + cultivo + lat + lon +dia+creci+triego +tsuelo+ agua+ largo +ancho+ hora)
        //println(cultivo)
        val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo, agua, largo, ancho, hora)
        println(newParcela)
        CoroutineScope(Dispatchers.IO).launch {
            database.parcelas().agregarParcela(newParcela)
            //(this@GraficoFragment.context as Activity).finish()
            //  requireActivity()?.onBackPressed()
            this@FormParcela.finish()
        }
    }
}

        val aborte = findViewById<Button>(R.id.closeBtn)
        aborte.setOnClickListener {
            this@FormParcela.finish()
        }

        return
    }

    private fun locationGPS(){
        if (this.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED &&
            this.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED){
            this.let {
                ActivityCompat.requestPermissions(
                    it as Activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    100
                )
            }

            return
        }

        val location = fusedLocationProviderClient.lastLocation

        location.addOnSuccessListener {
            if(it != null){
                val lat = it.latitude.toString()
                val lon = it.longitude.toString()
                println("vamos haber")
                println(lat.plus(longitude.text))
                println(lon.plus(longitude.text))
                latitude.setText(lat)
                longitude.setText(lon)
            }
        }
    }
}