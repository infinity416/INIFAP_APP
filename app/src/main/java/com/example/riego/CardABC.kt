package com.example.riego

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.DB.Parcela
import com.example.riego.databinding.FragmentGraficoBinding
import com.example.riego.fragments.HistoricoFragment
import com.example.riego.fragments.ParcelasFragment
import com.example.riego.source.DBparcela
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.zip.Inflater

class CardABC(private val parcelaList: List<Parcela>): RecyclerView.Adapter<CardABC.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sierra1 = itemView.findViewById<TextView>(R.id.txtcultivo)
        val sierra2 = itemView.findViewById<TextView>(R.id.txtnombre)
        val sierra3 = itemView.findViewById<TextView>(R.id.txtfecha)
        val btnbr = itemView.findViewById<ImageButton>(R.id.btndelete)
        val btned = itemView.findViewById<ImageButton>(R.id.btnedit)


        //val dx = itemView.context.startActivity(next)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val unsc = LayoutInflater.from(container.context).inflate(R.layout.card_abc, container, false)

        //correjir(LayoutInflater, ViewGroup)
        return ViewHolder(unsc)
    }


    override fun getItemCount(): Int {
        return parcelaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val G = parcelaList[position]

        holder.sierra1.text = G.cultivo
        holder.sierra2.text = G.id.toString()
        holder.sierra3.text = G.fecha

        val database = DBparcela.getDatabase(holder.itemView.context)

        //editstart
        holder.btned.setOnClickListener {
            val gogo = Intent(holder.itemView.context, FormParcela::class.java)
            gogo.putExtra("id", G.id)
            holder.itemView.context.startActivity(gogo as Intent?)
            print("mira.....:0")
            println(G.id)
        }
        //editfinish

       // correjir()

        //deletestart
        holder.btnbr.setOnClickListener{
            val dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alertdialog_delete)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnclose = dialog.findViewById<Button>(R.id.btnclose7)

            btnclose.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    database.parcelas().borrarParcela(G.id)
                    //println("elimina a "+G.id)
                    //println(database.parcelas().borrarParcela(G.id))
                }
                dialog.dismiss()
            }
            dialog.show()


        }
        //deletefinish
    }







    /* @SuppressLint("SuspiciousIndentation")
     fun correjir(): View {
         val oni = inflater.inflate(R.layout.card_abc, container, false)
         val btned = oni.findViewById<ImageButton>(R.id.btnedit)
         val btnbr = oni.findViewById<ImageButton>(R.id.btndelete)
             btned.setOnClickListener {
                val onix = Intent(oni.context, FragmentGraficoBinding::class.java)
             oni.context.startActivity(onix)
             }
         //val database = DBparcela.getDatabase(oni.context)
         btnbr.setOnClickListener {
             //database.parcelas().borrarParcela(1)
         }
         return oni
     }*/



}