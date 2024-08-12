package com.example.riego

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.riego.DB.Parcela
import com.example.riego.source.DBparcela
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class FormParcela : AppCompatActivity() {

    val cultivo = arrayOf("Algodón","Maíz Grano","Maíz Forraje")
    val cresimiento = arrayOf("Precoz", "Intermedio", "Tardío")
    val suelo = arrayOf("Ligero","Medio","Pesado")
    val rigo = arrayOf("Goteo", "Pivote", "Compuertas", "Avance frontal", "Surco" /*"Grabedad"*/)


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    lateinit var input8 : EditText
    lateinit var input9 : EditText
    lateinit var input10 : EditText
    lateinit var input11 : EditText
    lateinit var input16 : EditText
    lateinit var input17 : EditText
    lateinit var input18 : EditText
    lateinit var input19 : EditText

    private lateinit var databse: DBparcela
    private lateinit var parcela: Parcela
    private lateinit var parcelaLiveData: LiveData<Parcela>

    private lateinit var dbase: DBparcela
    private lateinit var clones: Parcela
    private lateinit var parcelaTropper: LiveData<Parcela>
    private lateinit var parcelabusqueda: LiveData<Parcela>

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var idParcela = intent.getIntExtra("id", 0)


        if (idParcela != 0){
            setContentView(R.layout.activity_form_parcela)
            val lisCultivo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val lisRiego = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
            val lisSuelo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
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
                //Toast.makeText(this, "Has elegido $itemCultivo", Toast.LENGTH_LONG).show()
            }
            lisSuelo.setOnItemClickListener { parent, view, position, id ->
                val itemSuelo = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $itemSuelo", Toast.LENGTH_LONG).show()
            }

            lisRiego.setOnItemClickListener { parent, view, position, id ->
                val itemRiego = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $itemRiego", Toast.LENGTH_LONG).show()

                /****/
                if(itemRiego == "Goteo"){
                    println("Capitulo II. el tipo de riego es: "+itemRiego)
                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input17.visibility = View.VISIBLE
                    input17.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input19.visibility = View.VISIBLE
                    input19.isEnabled = true

                    input8.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                }else if(itemRiego == "Pivote"){
                    println(" el tipo de riego es: "+itemRiego)
                    input16.visibility = View.INVISIBLE
                    input17.visibility = View.INVISIBLE
                    input18.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE


                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true


                }else if(itemRiego == "Compuertas"){
                    println(" el tipo de riego es: "+itemRiego)
                    input17.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                }else if(itemRiego == "Avance frontal"){
                    println(" el tipo de riego es: "+itemRiego)

                    input16.visibility = View.INVISIBLE
                    input17.visibility = View.INVISIBLE
                    input18.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true

                    input10.visibility = View.VISIBLE
                    input10.isEnabled = true

                }else if(itemRiego == "Surco"){
                    println(" el tipo de riego es: "+itemRiego)

                    input17.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true
                }
                /*else if( position == 2){
                println("Capitulo II. el tipo de riego es: "+itemRiego)
                longitude.visibility = View.VISIBLE
                latitude.visibility = View.INVISIBLE
            }*/
                /****/
            }

            lisCresimiento.setOnItemClickListener { parent, view, position, id ->
                val itemCrecimiento = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $$itemCrecimiento", Toast.LENGTH_LONG).show()
            }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            latitude = findViewById(R.id.latitudInput)
            longitude = findViewById(R.id.longuitdInput)

            val btnLoc = findViewById<ImageButton>(R.id.buttonUbication)



            val input1 = findViewById<EditText>(R.id.nameInput)
            val input2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val input5 = findViewById<EditText>(R.id.dateInput)
            input5.setOnClickListener {
                val datePicker = DateTime{day, month, year ->
                    //input5.setText(day.toString()+"/"+(month+1).toString()+"/"+year.toString())
                    if(day<10){
                        if((month+1) <= 9){
                            input5.setText("0"+day.toString()+"/"+"0"+(month+1)+"/"+year )
                        }else{
                            input5.setText("0"+day.toString()+"/"+(month+1)+"/"+year)
                        }
                    }else if((month+1) <= 9){
                        input5.setText(day.toString()+"/"+"0"+(month+1)+"/"+year)
                    }else{
                        input5.setText(day.toString()+"/"+(month+1)+"/"+year )
                    }
                }
                datePicker.show(supportFragmentManager,"datePicker")
            }
            val input13 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)
            val input6 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
            val input7 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
            input8 = findViewById<EditText>(R.id.aguaInput)
            input9 = findViewById<EditText>(R.id.largoInput)
            input10 = findViewById<EditText>(R.id.anchoInput)
            input11 = findViewById<EditText>(R.id.timeInput)
            val nanendoSwitch = findViewById<Switch>(R.id.switch1)
            input16 =  findViewById(R.id.CMInput)
            input17 = findViewById(R.id.CMInput0)
            input18 = findViewById(R.id.CMInput1)
            input19 = findViewById(R.id.CMInput2)

            println("Comando!!!! Comando!!!")
            println(idParcela)
            //


            btnLoc.setOnClickListener {
                //locationGPS()
                newlocationGPS()
            }

            databse= DBparcela.getDatabase(this)
            parcelaLiveData = databse.parcelas().consutaParcela(idParcela)
            parcelaLiveData.observe(this, Observer {
                parcela = it
                //var n1 =input1.setText(parcela.naame).toString()
               var clock1 = input1.setText(parcela.naame).toString()
                var z =parcela.naame
                input2.setText(parcela.cultivo)
                latitude.setText(parcela.lat)
                longitude.setText(parcela.lon)
                input5.setText(parcela.fecha)
                input13.setText(parcela.crecimieto)
                input6.setText(parcela.riego)
                if(parcela.riego ==  "Goteo") {
                    println("es goteo")
                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input17.visibility = View.VISIBLE
                    input17.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input19.visibility = View.VISIBLE
                    input19.isEnabled = true

                    input8.visibility = View.INVISIBLE
                    input8.isEnabled = false

                    input9.visibility = View.INVISIBLE
                    input9.isEnabled = false

                    input10.visibility = View.INVISIBLE
                    input10.isEnabled = false

                }else if(parcela.riego == "Pivote"){
                    println("no es goteo es: "+parcela.riego)

                    input16.visibility = View.INVISIBLE
                    input16.isEnabled = false

                    input17.visibility = View.INVISIBLE
                    input17.isEnabled = false

                    input18.visibility = View.INVISIBLE
                    input18.isEnabled = false

                    input19.visibility = View.INVISIBLE
                    input19.isEnabled = false

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true

                    input10.visibility = View.INVISIBLE
                    input10.isEnabled = false

                }else if(parcela.riego == "Compuertas"){

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input17.visibility = View.INVISIBLE
                    input17.isEnabled = false

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input19.visibility = View.INVISIBLE
                    input19.isEnabled = false

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.INVISIBLE
                    input9.isEnabled = false

                    input10.visibility = View.INVISIBLE
                    input10.isEnabled = false

                }else if(parcela.riego == "Avance frontal"){

                    input16.visibility = View.INVISIBLE
                    input16.isEnabled = false

                    input17.visibility = View.INVISIBLE
                    input17.isEnabled = false

                    input18.visibility = View.INVISIBLE
                    input18.isEnabled = false

                    input19.visibility = View.INVISIBLE
                    input19.isEnabled = false

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true

                    input10.visibility = View.VISIBLE
                    input10.isEnabled = true

                }else if(parcela.riego == "Surco"){

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input17.visibility = View.INVISIBLE
                    input17.isEnabled = false

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input19.visibility = View.INVISIBLE
                    input19.isEnabled = false

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.INVISIBLE
                    input9.isEnabled = false

                    input10.visibility = View.INVISIBLE
                    input10.isEnabled = false

                }
                input7.setText(parcela.suelo)
                input8.setText(parcela.pozo)
                input9.setText(parcela.larg)
                input10.setText(parcela.anch)
                input11.setText(parcela.dias)
                input16.setText(parcela.largXsurco)
                input17.setText(parcela.gotero)
                input18.setText(parcela.cmXsuko)
                input19.setText(parcela.cmXgotero)
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
            var agua = ""
            //nanendoSwitch.setOnClickListener {


            //}

            val savebtn = findViewById<Button>(R.id.saveBtn)
            savebtn.setOnClickListener {
                val nombre = input1.text.toString()
                val cultivo = input2.text.toString()
                val lat = latitude.text.toString()
                val lon = longitude.text.toString()
                val dia = input5.text.toString()
                val creci = input13.text.toString()
                val triego = input6.text.toString()
                    if (triego == "Goteo"){
                        input16.text.toString()
                        input17.text.toString()
                        input18.text.toString()
                        input19.text.toString()
                        input8.setText("")
                        input9.setText("")
                        input10.setText("")
                    }else if(triego == "Pivote"){
                        input16.setText("")
                        input17.setText("")
                        input18.setText("")
                        input19.setText("")
                        input8.text.toString()
                        input9.text.toString()
                        input10.setText("")
                    }else if(triego == "Compuertas" ){
                        input16.text.toString()
                        input17.setText("")
                        input18.text.toString()
                        input19.setText("")
                        input8.text.toString()
                        input9.setText("")
                        input10.setText("")
                    }else if(triego == "Avance frontal"){
                        input16.setText("")
                        input17.setText("")
                        input18.setText("")
                        input19.setText("")
                        input8.text.toString()
                        input9.text.toString()
                        input10.text.toString()
                    }else if(triego == "Surco"){
                        input16.text.toString()
                        input17.setText("")
                        input18.text.toString()
                        input19.setText("")
                        input8.text.toString()
                        input9.setText("")
                        input10.setText("")
                    }
                val tsuelo = input7.text.toString()
                // val agua = input8.text.toString()
                if(nanendoSwitch.isChecked){
                    val cantwater = input8.text.toString().toDouble()
                    val convert =(3.78541*cantwater)
                    agua = convert.toString()
                }else{
                    agua = input8.text.toString()
                }

                val dias = input11.text.toString()

                val amumu_2 =  input9.text.toString()
                val largo = if(amumu_2 != ""){
                    input9.text.toString()
                }else{
                    input9.setText("")
                    ""
                }

                val amumu_1 = input10.text.toString()
                val ancho = if(amumu_1 != ""){
                    input10.text.toString()
                }else{
                    input10.setText("")
                    ""
                }

                val nunu = input16.text.toString()
                println("en un yeti!!! "+nunu)
                val lgxsrc = if(nunu != ""){
                    input16.text.toString()
                }else{
                    input16.setText("")
                    ""
                }

                val amumu = input17.text.toString()
                val gotero = if(amumu != ""){
                    input17.text.toString()
                }else{
                    input17.setText("")
                    ""
                }

                val anumu1 = input18.text.toString()
                val cmxsk = if(anumu1 != ""){
                    input18.text.toString()
                }else{
                    input18.setText("")
                    ""
                }

                val amumu2 = input19.text.toString()
                val cmxgo = if(amumu2 != ""){
                    input19.text.toString()
                }else{
                    input19.setText("")
                    ""
                }
                /******/


                if(nombre.isEmpty()){
                    input1.setError("Ingrese el nombre de la parcela")
                    return@setOnClickListener
                }else if(cultivo.isEmpty()){
                    input2.setError("Seleccione el cultivo")
                    return@setOnClickListener
                }else if(lat.isEmpty()){
                    latitude.setError("Ingrese la latitud")
                    return@setOnClickListener
                }else if(lon.isEmpty()){
                    longitude.setError("Ingrese la longitud")
                    return@setOnClickListener
                }else if(dia.isEmpty()){
                    input5.setError("Ingrese la fecha de inicio de siembra")
                    return@setOnClickListener
                }else if(creci.isEmpty()){
                    input13.setError("Seleccione el tipo de crecimiento")
                    return@setOnClickListener
                }else if(triego.isEmpty()){
                    input6.setError("Seleccione el tipo de riego")
                    return@setOnClickListener
                }else if(tsuelo.isEmpty()){
                    input7.setError("Seleccione el tipo de suelo")
                    return@setOnClickListener
                }else if(dias.isEmpty()){
                    input11.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else{
                    dbase = DBparcela.getDatabase(this)
                    parcelabusqueda = dbase.parcelas().consutaParcelaName(nombre)
                    parcelabusqueda.observe( this, Observer {
                        println("miraaaaaaaaa"+it.id)
                        println("miraaaaaaaaa"+idParcela)
                        if(it.id == idParcela){

                            val dialog = Dialog(this)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.alertdialog_update)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val btnclose = dialog.findViewById<Button>(R.id.btnclose6)

                            btnclose.setOnClickListener {
                                CoroutineScope(Dispatchers.IO).launch {
                                    parcela.id = idParcela
                                    parcela.naame = nombre
                                    parcela.cultivo = cultivo
                                    parcela.lat = lat
                                    parcela.lon = lon
                                    parcela.fecha = dia
                                    parcela.crecimieto = creci
                                    parcela.riego = triego
                                    parcela.suelo = tsuelo
                                    parcela.pozo = agua
                                    parcela.larg = largo
                                    parcela.anch = ancho
                                    parcela.dias = dias
                                    parcela.largXsurco = lgxsrc
                                    parcela.gotero = gotero
                                    parcela.cmXsuko =  cmxsk
                                    parcela.cmXgotero = cmxgo
                                    //println("HOLAAAAAAAAAAAAA!!!!!!!!!!")
                                    println(parcela)
                                    databse.parcelas().editarParcela(parcela)
                                }
                                dialog.dismiss()
                                this@FormParcela.finish()
                            }
                            dialog.show()
                        }else{
                            val dialogs = Dialog(this)
                            dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogs.setCancelable(false)
                            dialogs.setContentView(R.layout.alertdialog_questionfile)
                            dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val btncloses = dialogs.findViewById<Button>(R.id.btncloseQF)

                            btncloses.setOnClickListener {
                                dialogs.dismiss()
                            }
                            dialogs.show()
                        }
                    })
                    /*parcelaTropper = dbase.parcelas().existeName(nombre)
                   parcelaTropper.observe( this, Observer {
                       if(it === null){
                           CoroutineScope(Dispatchers.IO).launch {
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
                               parcela.cmXsuko =  cmxsk

                               println("HOLAAAAAAAAAAAAA!!!!!!!!!!")
                               println(parcela)
                               databse.parcelas().editarParcela(parcela)

                               //Toast.makeText(this@FormParcela.baseContext.applicationContext , "Datos Actualizados", Toast.LENGTH_LONG).show()
                           }

                       }else{

                               val dialogs = Dialog(this)
                               dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                               dialogs.setCancelable(false)
                               dialogs.setContentView(R.layout.alertdialog_questionfile)
                               dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                               val btncloses = dialogs.findViewById<Button>(R.id.btncloseQF)

                               btncloses.setOnClickListener {
                                   dialogs.dismiss()
                               }
                               dialogs.show()

                       }
                   })*/
                    //Toast.makeText(this, "Datos Actualizados", Toast.LENGTH_LONG).show()

                    /***CoroutineScope(Dispatchers.IO).launch {
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
                        parcela.cmXsuko =  cmxsk

                        println("HOLAAAAAAAAAAAAA!!!!!!!!!!")
                        println(parcela)
                        databse.parcelas().editarParcela(parcela)

                        //Toast.makeText(this@FormParcela.baseContext.applicationContext , "Datos Actualizados", Toast.LENGTH_LONG).show()
                    }
                    //Toast.makeText(this, "Datos Actualizados", Toast.LENGTH_LONG).show()
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.alertdialog_update)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val btnclose = dialog.findViewById<Button>(R.id.btnclose6)

                    btnclose.setOnClickListener {
                        dialog.dismiss()
                        this@FormParcela.finish()
                    }
                    dialog.show()*/

                }
            }


        }else{
            setContentView(R.layout.activity_form_parcela)
            val lisCultivo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val lisRiego = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
            val lisSuelo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
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
                //Toast.makeText(this, "Has elegido $itemCultivo", Toast.LENGTH_LONG).show()
            }
            lisSuelo.setOnItemClickListener { parent, view, position, id ->
                val itemSuelo = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $itemSuelo", Toast.LENGTH_LONG).show()
            }

            lisRiego.setOnItemClickListener { parent, view, position, id ->
                val itemRiego = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $itemRiego", Toast.LENGTH_LONG).show()

                /****/
                if(itemRiego == "Goteo"){
                    println("Capitulo II. el tipo de riego es: "+itemRiego)
                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input17.visibility = View.VISIBLE
                    input17.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input19.visibility = View.VISIBLE
                    input19.isEnabled = true

                    input8.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                }else if(itemRiego == "Pivote"){
                    println(" el tipo de riego es: "+itemRiego)
                    input16.visibility = View.INVISIBLE
                    input17.visibility = View.INVISIBLE
                    input18.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE


                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true


                }else if(itemRiego == "Compuertas"){
                    println(" el tipo de riego es: "+itemRiego)
                    input17.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                }else if(itemRiego == "Avance frontal"){
                    println(" el tipo de riego es: "+itemRiego)

                    input16.visibility = View.INVISIBLE
                    input17.visibility = View.INVISIBLE
                    input18.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true

                    input9.visibility = View.VISIBLE
                    input9.isEnabled = true

                    input10.visibility = View.VISIBLE
                    input10.isEnabled = true

                }else if(itemRiego == "Surco"){
                    println(" el tipo de riego es: "+itemRiego)

                    input17.visibility = View.INVISIBLE
                    input19.visibility = View.INVISIBLE
                    input9.visibility = View.INVISIBLE
                    input10.visibility = View.INVISIBLE

                    input16.visibility = View.VISIBLE
                    input16.isEnabled = true

                    input18.visibility = View.VISIBLE
                    input18.isEnabled = true

                    input8.visibility = View.VISIBLE
                    input8.isEnabled = true
                }
                /*else if( position == 2){
                println("Capitulo II. el tipo de riego es: "+itemRiego)
                longitude.visibility = View.VISIBLE
                latitude.visibility = View.INVISIBLE
            }*/
                /****/
            }

            lisCresimiento.setOnItemClickListener { parent, view, position, id ->
                val itemCrecimiento = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Has elegido $$itemCrecimiento", Toast.LENGTH_LONG).show()
            }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            latitude = findViewById(R.id.latitudInput)
            longitude = findViewById(R.id.longuitdInput)

            val btnLoc = findViewById<ImageButton>(R.id.buttonUbication)


            /*val floatbtn = findViewById<Button>(R.id.btnexit)
            floatbtn.setOnClickListener{
                    finish()
            }*/

            //
            val input1 = findViewById<EditText>(R.id.nameInput)
            val input2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val input3 = findViewById<EditText>(R.id.latitudInput)
            val input4 = findViewById<EditText>(R.id.longuitdInput)
            val input5 = findViewById<EditText>(R.id.dateInput)
            input5.setOnClickListener {
                val datePicker = DateTime{day, month, year ->
                    //input5.setText(day.toString()+"/"+(month+1).toString()+"/"+year.toString())
                    if(day<10){
                        if((month+1) <= 9){
                            input5.setText("0"+day.toString()+"/"+"0"+(month+1)+"/"+year )
                        }else{
                            input5.setText("0"+day.toString()+"/"+(month+1)+"/"+year)
                        }
                    }else if((month+1) <= 9){
                        input5.setText(day.toString()+"/"+"0"+(month+1)+"/"+year)
                    }else{
                        input5.setText(day.toString()+"/"+(month+1)+"/"+year )
                    }
                }
                datePicker.show(supportFragmentManager,"datePicker")
            }
            val input13 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)
            val input6 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
            val input7 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
            input8 = findViewById<EditText>(R.id.aguaInput)
            input8.isEnabled = false
            input8.visibility = View.INVISIBLE

            input9 = findViewById<EditText>(R.id.largoInput)
            input9.isEnabled = false
            input9.visibility = View.INVISIBLE

            input10 = findViewById<EditText>(R.id.anchoInput)
            input10.isEnabled = false
            input10.visibility = View.INVISIBLE

            input11 = findViewById<EditText>(R.id.timeInput)


            val nanendoSwitch = findViewById<Switch>(R.id.switch1)

            input16 =  findViewById(R.id.CMInput)
            input16.isEnabled = false
            input16.visibility = View.INVISIBLE

            input17 = findViewById(R.id.CMInput0)
            input17.isEnabled = false
            input17.visibility = View.INVISIBLE

            input18 = findViewById(R.id.CMInput1)
            input18.isEnabled = false
            input18.visibility = View.INVISIBLE

            input19 = findViewById(R.id.CMInput2)
            input19.isEnabled = false
            input19.visibility = View.INVISIBLE

            println("Comando!!!! Comando!!!")
            println(idParcela)
            //

            val database = DBparcela.getDatabase(this)

            btnLoc.setOnClickListener {
                //locationGPS()
                newlocationGPS()
            }

            var agua = ""
            val savebtn = findViewById<Button>(R.id.saveBtn)
            savebtn.setOnClickListener {

                val nombre = input1.text.toString()
                val cultivo = input2.text.toString()
                val lat = latitude.text.toString()
                val lon = longitude.text.toString()
                val dia = input5.text.toString()
                val creci = input13.text.toString()
                val triego = input6.text.toString()
                val tsuelo = input7.text.toString()
                if(nanendoSwitch.isChecked){
                    val cantwater = input8.text.toString().toDouble()
                    val convert =(3.78541*cantwater)
                    agua = convert.toString()
                }else{
                    agua = input8.text.toString()
                }
                //agua
                //val agua = input8.text.toString()
                //val largo = input9.text.toString()
                //val ancho = input10.text.toString()
                val dias = input11.text.toString()

                /**START**/
                val nunu_3= input9.text.toString()
                val largo = if(nunu_3 != ""){
                    input9.text.toString()
                }else{
                    ""
                }

                val nunu_2= input10.text.toString()
                val ancho = if(nunu_2 != ""){
                    input10.text.toString()
                }else{
                    ""
                }

                val nunu = input16.text.toString()
                println("en un yeti!!! "+nunu)
                val lgxsrc = if(nunu != ""){
                    input16.text.toString()
                }else{
                    ""
                }

                val nunu1 = input17.text.toString()
                val gotero = if(nunu1 != ""){
                    input17.text.toString()
                }else{
                    ""
                }

                val nunu2 = input18.text.toString()
                val cmxsk = if(nunu2 != ""){
                    input18.text.toString()
                }else{
                    ""
                }

                val nunu3 = input19.text.toString()
                val cmxgo = if(nunu3 != ""){
                    input19.text.toString()
                }else{
                    ""
                }
                /**END**/
                /******/
                if(nombre.isEmpty()){
                    input1.setError("Ingrese el nombre de la parcela")
                    return@setOnClickListener
                }else if(cultivo.isEmpty()){
                    input2.setError("Seleccione el cultivo")
                    return@setOnClickListener
                }else if(lat.isEmpty()){
                    input3.setError("Ingrese la latitud")
                    return@setOnClickListener
                }else if(lon.isEmpty()){
                    input4.setError("Ingrese la longitud")
                    return@setOnClickListener
                }else if(dia.isEmpty()){
                    input5.setError("Ingrese la fecha de inicio de siembra")
                    return@setOnClickListener
                }else if(creci.isEmpty()){
                    input13.setError("Seleccione el tipo de crecimiento")
                    return@setOnClickListener
                }else if(triego.isEmpty()){
                    input6.setError("Seleccione el tipo de riego")
                    return@setOnClickListener
                }else if(tsuelo.isEmpty()){
                    input7.setError("Seleccione el tipo de suelo")
                    return@setOnClickListener
                }/*else if(agua.isEmpty()){
                    input8.setError("Ingrese la cantidad de agua")
                    return@setOnClickListener
                }else if(largo.isEmpty()){
                    input9.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else if(ancho.isEmpty()){
                    input10.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }*/else if(dias.isEmpty()){
                    input11.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else{
                    println(nombre + cultivo + lat + lon +dia+ creci + triego + tsuelo+ agua+ largo +ancho+ dias)
                    //println(cultivo)
                    //parcelaLiveData= databse.parcelas().existeName(nombre)

                    /***/

                    dbase = DBparcela.getDatabase(this)
                    parcelaTropper = dbase.parcelas().existeName(nombre)
                    parcelaTropper.observe(this, Observer {
                        if (it?.naame == nombre){
                            println("ya existe ese dato ${it?.naame}, y $nombre son iguales")
                            val dialogs = Dialog(this)
                            dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogs.setCancelable(false)
                            dialogs.setContentView(R.layout.alertdialog_questionfile)
                            dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val btncloses = dialogs.findViewById<Button>(R.id.btncloseQF)

                            btncloses.setOnClickListener {
                                dialogs.dismiss()
                            }
                            dialogs.show()
                        }else{
                            println("dato unico ${it?.naame}, y $nombre son diferentes")

                            val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo,  agua, largo, ancho, dias, cmxsk, lgxsrc, cmxgo, gotero)
                            println(newParcela)

                            val dialog = Dialog(this)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.alertdialog_save)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                            btnclose.setOnClickListener {
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.parcelas().agregarParcela(newParcela)
                                }
                                dialog.dismiss()
                                this@FormParcela.finish()
                            }
                            dialog.show()
                        }
                    })
                }
            }
        }

        val aborte = findViewById<Button>(R.id.closeBtn)
        aborte.setOnClickListener {
            this@FormParcela.finish()
        }

        return
    }



    private fun newlocationGPS() {
        /**GPS**/
        val gpsconection = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsstatus = gpsconection.isProviderEnabled(LocationManager.GPS_PROVIDER)



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 104)
            return
        }

        val localizacion = fusedLocationProviderClient.lastLocation
        localizacion.addOnSuccessListener {
            if(gpsstatus==true){
                println("encendio")
                /**GPS**/
                val location : Location?= it

                if(location != null){
                    latitude.setText(location.latitude.toString())
                    longitude.setText(location.longitude.toString())
                }else{
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.alertdialog_singps)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val btnclose = dialog.findViewById<Button>(R.id.btnsingps)

                    btnclose.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }


            }else{
                println("apagado")
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.alertdialog_offgps)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val btnclose = dialog.findViewById<Button>(R.id.btnclose8)

                btnclose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }

        }



    }











    /*private fun locationGPS(){
        if (this.let {
            ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
        } != PackageManager.PERMISSION_GRANTED &&
            this.let {
                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
                this.let {
                    ActivityCompat.requestPermissions(it as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
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
    }*/
}