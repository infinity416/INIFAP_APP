package com.example.riego

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.riego.DB.RiegoAplicado
import com.example.riego.source.DBparcela
import com.example.riego.source.DBriegoaplic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardRiego(private val riegolist: List<RiegoAplicado>): RecyclerView.Adapter<CardRiego.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val noob = itemView.findViewById<TextView>(R.id.txtfecha)
        val noob1 = itemView.findViewById<ImageButton>(R.id.btndelete)
        val noob2 = itemView.findViewById<TextView>(R.id.txthora)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val unsc = LayoutInflater.from(container.context).inflate(R.layout.card_riego, container, false)
        return ViewHolder(unsc)
    }


    override fun getItemCount(): Int {
         return riegolist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val L = riegolist[position]

        /***View Start***/
        val camF = L.fecha_riego_aplicado.get(8).toString()
        val camF1 = L.fecha_riego_aplicado.get(9).toString()
        val camFDD = camF+camF1
        val camF3 = L.fecha_riego_aplicado.get(5).toString()
        val camF4 = L.fecha_riego_aplicado.get(6).toString()
        val camFMM = camF3+camF4
        val camF6 = L.fecha_riego_aplicado.get(0).toString()
        val camF7 = L.fecha_riego_aplicado.get(1).toString()
        val camF8 = L.fecha_riego_aplicado.get(2).toString()
        val camF9 = L.fecha_riego_aplicado.get(3).toString()
        val camFYY = camF6+camF7+camF8+camF9
        holder.noob.text = "$camFDD/$camFMM/$camFYY"
        holder.noob2.text = L.hora_riego_aplicado

        val database = DBriegoaplic.getDatabase(holder.itemView.context)
        /***View End***/




        /***Delete Start***/
        holder.noob1.setOnClickListener{
            val dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alertdialog_delete_riego)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnclose = dialog.findViewById<Button>(R.id.btnclose7)

            btnclose.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    database.riego_aplicado().borrarRiegoAplic(L.id_RiegoAplic)
                }
                dialog.dismiss()
            }
            dialog.show()
        }
        /***Delete End***/
    }
}