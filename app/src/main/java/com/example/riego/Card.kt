package com.example.riego

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.internal.wait
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList
import java.util.zip.Inflater

class Card(private val historlaList: ArrayList<Historial>) : RecyclerView.Adapter<Card.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val clon1 = itemView.findViewById<TextView>(R.id.textView)
        val clon2 = itemView.findViewById<TextView>(R.id.textView7)
        val clon3 = itemView.findViewById<TextView>(R.id.textView8)
        val clon4 = itemView.findViewById<TextView>(R.id.textView9)
        val clon5 = itemView.findViewById<TextView>(R.id.textView10)
        val clon6 = itemView.findViewById<TextView>(R.id.textView11)
        val clon7 = itemView.findViewById<TextView>(R.id.textView12)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val clones = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(clones)
    }

    override fun getItemCount(): Int {
        return  historlaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = historlaList[position]

        holder.clon1.text = l.Fecha
        holder.clon2.text = l.LaminaSueloActual.toString()
        holder.clon3.text = l.LaminaReponer.toString()
        holder.clon4.text = l.TiempoRiego
        holder.clon5.text = l.UCA.toString()
        holder.clon6.text = l.PrecipitacionEfectivaAcum.toString()
        holder.clon7.text = l.ETCAcum.toString()

    }
}
   /* class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cody = itemView.findViewById<TextView>(R.id.textView)

    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val clones = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(clones)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val les = list[position]
        holder.apply {
            Fecha.text = currentItem.Fecha.toString()
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }*/




///VERSION II
   /* override fun onCreateViewHolder(parent: ViewGroup, int: Int): ViewHolder {
        val clones =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(clones)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val Na = dataList[position]
        val cody = holder.itemView.findViewById<TextView>(R.id.textView)

        val sodio = "${Na.Fecha}"
        println("aquiva mas TEXTO")
        println(sodio)
        cody.text = sodio
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemFecha: TextView
        val itemClon1: TextView
        val itemclon2: TextView
        val itemclon3: TextView
        val itemclon4: TextView
        val itemclon5: TextView
        val itemclon6: TextView

        init {
            itemFecha = itemView.findViewById(R.id.textView)
            itemClon1 = itemView.findViewById(R.id.textView7)
            itemclon2 = itemView.findViewById(R.id.textView8)
            itemclon3 = itemView.findViewById(R.id.textView9)
            itemclon4 = itemView.findViewById(R.id.textView10)
            itemclon5 = itemView.findViewById(R.id.textView11)
            itemclon6 = itemView.findViewById(R.id.textView12)
        }
    }*/

///VERSION 1
    /* val f5 = arrayOf("d1", "d2", "d3", "d4", "d5")

    /*val cody = arrayOf("c1","c2","c3","c4","c5")
    val rex = arrayOf("r1","r2","r3","r4","r5")
    val hunter = arrayOf("h1","h2","h3","h4","h5")
    val echo = arrayOf("e1","e2","e3","e4","e5")
    val hardcrakid = arrayOf("har1","har2","har3","har4","har5")*/
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): Card.ViewHolder {
        val clones =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(clones)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.itemclon2.text = contacto[i]
        holder.itemclon3.text = contacto[i]
        holder.itemclon4.text = contacto[i]
        holder.itemclon5.text = contacto[i]
        holder.itemclon6.text = contacto
        /*val client = OkHttpClient()
        val header = "confidential-apiKey"
        val key = ""
        val url =
            "https://appinifap.sytes.net/apiweb/api/riego?EstacionID=4&FechaIni=01/04/2023&FechaFin=01/07/2023"
        val newimperio = okhttp3.Request.Builder().url(url)
            .header(header, "Vfm8JkqzCLYghAs0531Y1FBvgDBxu0a4OEbME").build()

        client.newCall(newimperio).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val hay = response.body!!.string()
                    val algo = JSONObject(hay)
                    val senal = algo.getJSONObject("riego")
                    val cuantosson = senal.getJSONArray("RequerimientoRiego")
                    for (i in -1 until cuantosson.length()) {
                        var contacto = cuantosson.getJSONObject(i).getString("Fecha")
                        val contacto1 = cuantosson.getJSONObject(i).getString("LaminaSueloActual")
                        /*val contacto2 = cuantosson.getJSONObject(l).getString("LaminaReponer")
                         val contacto3 = cuantosson.getJSONObject(l).getString("TiempoRiego")
                         val contacto4 = cuantosson.getJSONObject(l).getString("UCA")
                         val contacto5 = cuantosson.getJSONObject(l).getString("PrecipitacionEfectivaAcum")
                         val contacto6 = cuantosson.getJSONObject(l).getString("ETCAcum")*/
                        //holder.itemFecha.text = f5[i]
                        println("Aqui vamos de nuevo")
                        println(contacto)
                        holder.itemFecha.text = contacto

                        /*holder.itemclon2.text = contacto[i]
                        holder.itemclon3.text = contacto[i]
                        holder.itemclon4.text = contacto[i]
                        holder.itemclon5.text = contacto[i]
                        holder.itemclon6.text = contacto*/


                    }
                }
            }
        })*/
    }


    override fun getItemCount(): Int {
        return f5.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemFecha: TextView
        val itemClon1: TextView
        val itemclon2: TextView
        val itemclon3: TextView
        val itemclon4: TextView
        val itemclon5: TextView
        val itemclon6: TextView

        init {
            itemFecha = itemView.findViewById(R.id.textView)
            itemClon1 = itemView.findViewById(R.id.textView7)
            itemclon2 = itemView.findViewById(R.id.textView8)
            itemclon3 = itemView.findViewById(R.id.textView9)
            itemclon4 = itemView.findViewById(R.id.textView10)
            itemclon5 = itemView.findViewById(R.id.textView11)
            itemclon6 = itemView.findViewById(R.id.textView12)
        }
    }
}

*/
//}


