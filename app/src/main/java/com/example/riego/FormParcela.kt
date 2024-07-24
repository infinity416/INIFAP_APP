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
    val suelo = arrayOf("Ligero","Media","Pesado")
    val rigo = arrayOf("Goteo", "Pivote")


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    lateinit var input16 : EditText

    private lateinit var databse: DBparcela
    private lateinit var parcela: Parcela
    private lateinit var parcelaLiveData: LiveData<Parcela>

    private lateinit var dbase: DBparcela
    private lateinit var clones: Parcela
    private lateinit var parcelaTropper: LiveData<Parcela>
    private lateinit var parcelabusqueda: LiveData<Parcela>

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
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
                }else if(itemRiego == "Pivote"){
                    println(" el tipo de riego es: "+itemRiego)
                    input16.visibility = View.INVISIBLE
                }/*else if( position == 2){
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
            val input8 = findViewById<EditText>(R.id.aguaInput)
            val input9 = findViewById<EditText>(R.id.largoInput)
            val input10 = findViewById<EditText>(R.id.anchoInput)
            val input11 = findViewById<EditText>(R.id.timeInput)
            val nanendoSwitch = findViewById<Switch>(R.id.switch1)
            input16 =  findViewById(R.id.CMInput)

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
                }else if(parcela.riego == "Pivote"){
                    println("no es goteo es: "+parcela.riego)
                    input16.isEnabled = false
                    input16.visibility = View.INVISIBLE
                }
                input7.setText(parcela.suelo)
                input8.setText(parcela.agua)
                input9.setText(parcela.larg)
                input10.setText(parcela.anch)
                input11.setText(parcela.hora)
                input16.setText(parcela.cmXsuko)
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
                var cmxsk = ""
                val triego = input6.text.toString()
                    if (triego == "Goteo"){
                        input16.text.toString()
                    }else if(triego == "Pivote"){
                        input16.setText("")
                    }
                val tsuelo = input7.text.toString()
                // val agua = input8.text.toString()
                if(nanendoSwitch.isChecked){
                    val cantwater = input8.text.toString().toDouble()
                    val convert =(0.264172*cantwater)
                    agua = convert.toString()
                }else{
                    agua = input8.text.toString()
                }
                val largo = input9.text.toString()
                val ancho = input10.text.toString()
                val hora = input11.text.toString()
                val nunu = input16.text.toString()
                println("en un yeti!!! "+nunu)

                if(nunu != ""){
                    cmxsk = input16.text.toString()
                }else{
                    input16.setText("")
                    cmxsk = ""
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
                }else if(agua.isEmpty()){
                    input8.setError("Ingrese la cantidad de agua")
                    return@setOnClickListener
                }else if(largo.isEmpty()){
                    input9.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else if(ancho.isEmpty()){
                    input10.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else if(hora.isEmpty()){
                    input11.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else{
                    /*dbase = DBparcela.getDatabase(this)
                    parcelaTropper = dbase.parcelas().existeName(nombre)
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
                    dialog.show()

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

                }else if(itemRiego == "Pivote"){
                    println(" el tipo de riego es: "+itemRiego)
                    input16.visibility = View.INVISIBLE


                }/*else if( position == 2){
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
            val input8 = findViewById<EditText>(R.id.aguaInput)
            val input9 = findViewById<EditText>(R.id.largoInput)
            val input10 = findViewById<EditText>(R.id.anchoInput)
            val input11 = findViewById<EditText>(R.id.timeInput)
            val nanendoSwitch = findViewById<Switch>(R.id.switch1)
            input16 =  findViewById(R.id.CMInput)
            input16.isEnabled = false
            input16.visibility = View.INVISIBLE
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
                    val convert =(0.264172*cantwater)
                    agua = convert.toString()
                }else{
                    agua = input8.text.toString()
                }
                agua
                //val agua = input8.text.toString()
                val largo = input9.text.toString()
                val ancho = input10.text.toString()
                val hora = input11.text.toString()
                val nunu = input16.text.toString()
                println("en un yeti!!! "+nunu)
                var cmxsk = ""
                if(nunu != ""){
                    cmxsk = input16.text.toString()
                }else{
                    cmxsk = ""
                }

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
                }else if(agua.isEmpty()){
                    input8.setError("Ingrese la cantidad de agua")
                    return@setOnClickListener
                }else if(largo.isEmpty()){
                    input9.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else if(ancho.isEmpty()){
                    input10.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else if(hora.isEmpty()){
                    input11.setError("Ingrese el dato solicitante")
                    return@setOnClickListener
                }else{
                    println(nombre + cultivo + lat + lon +dia+ creci + triego + tsuelo+ agua+ largo +ancho+ hora)
                    //println(cultivo)
                    //parcelaLiveData= databse.parcelas().existeName(nombre)

                    /***/

                    dbase = DBparcela.getDatabase(this)
                    parcelaTropper = dbase.parcelas().existeName(nombre)
                    parcelaTropper.observe(this, Observer {
                        if(it === null){
                            val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo,  agua, largo, ancho, hora, cmxsk)
                            println(newParcela)

                            CoroutineScope(Dispatchers.IO).launch {
                                database.parcelas().agregarParcela(newParcela)
                            }

                            val dialog = Dialog(this)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.alertdialog_save)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                            btnclose.setOnClickListener {
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
                        //continamos aqui mañana 23/07/24
                         /*if(it === null ){
                            println("shingeki no kyojin!!!! esta VACIO, que se cree!!!")
                            //plyer1 = "1"
                             resultado = 1
                             /*val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo,  agua, largo, ancho, hora, cmxsk)
                             println(newParcela)

                             CoroutineScope(Dispatchers.IO).launch {
                                 database.parcelas().agregarParcela(newParcela)
                                 //(this@GraficoFragment.context as Activity).finish()
                                 //  requireActivity()?.onBackPressed()

                             }
                             val dialog = Dialog(this)
                             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                             dialog.setCancelable(false)
                             dialog.setContentView(R.layout.alertdialog_save)
                             dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                             val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                             btnclose.setOnClickListener {
                                 dialog.dismiss()
                                 this@FormParcela.finish()
                             }
                             dialog.show()*/

                        }else{
                            println("eren!!!!  Ya EXISTE")
                            //player2 = "2"
                             resultado = 2
                             /*val dialogs = Dialog(this)
                             dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                             dialogs.setCancelable(false)
                             dialogs.setContentView(R.layout.alertdialog_questionfile)
                             dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                             val btncloses = dialogs.findViewById<Button>(R.id.btncloseQF)

                             btncloses.setOnClickListener {
                                 dialogs.dismiss()
                             }
                             dialogs.show()*/
                        }*/

                       /*
                        when(resultado){
                            1 -> {
                                val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo,  agua, largo, ancho, hora, cmxsk)
                                println(newParcela)

                                CoroutineScope(Dispatchers.IO).launch {
                                    database.parcelas().agregarParcela(newParcela)
                                    //(this@GraficoFragment.context as Activity).finish()
                                    //  requireActivity()?.onBackPressed()
                                }

                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.alertdialog_save)
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                                btnclose.setOnClickListener {
                                    dialog.dismiss()
                                    this.finish()
                                }
                                dialog.show()
                            }
                            2 -> {
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
                            }*/

                        //println("How is..."+plyer1)
                        //println("no hay medicina..."+player2)

                       // println("que tengo que hacer "+ nel +" "+ nelv2)


                        /*when(respuesta){
                            1 -> {
                                val newParcela = Parcela(nombre , cultivo , lat , lon ,dia, creci,  triego, tsuelo,  agua, largo, ancho, hora, cmxsk)
                                println(newParcela)

                                CoroutineScope(Dispatchers.IO).launch {
                                    database.parcelas().agregarParcela(newParcela)
                                    //(this@GraficoFragment.context as Activity).finish()
                                    //  requireActivity()?.onBackPressed()
                                }

                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.alertdialog_save)
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                val btnclose = dialog.findViewById<Button>(R.id.btnclose5)

                                btnclose.setOnClickListener {
                                    dialog.dismiss()
                                    this@FormParcela.finish()
                                }
                                dialog.show()
                            }
                            2 -> {
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
                        }*/
                       // val nel = parcelaTropper.let { name->player2 }/***no***/
                    //val nelv2 = parcelaTropper.let { name->plyer1 }/***si***/
                      //  println("que tengo que hacer "+ nel +" "+ nelv2)
                      //  Les = nel
                    })
                    /***/
                    //println("finn"+respuesta)
                     //println("bot"+Les)
                    //Toast.makeText(this , "Datos Guardados", Toast.LENGTH_LONG).show()

                    /******/




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