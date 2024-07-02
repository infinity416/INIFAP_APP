package com.example.riego

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentTransaction
import com.example.riego.fragments.BusquedaFragment
import com.example.riego.fragments.GraficoFragment
import com.example.riego.fragments.HistoricoFragment
import com.example.riego.fragments.ParcelasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class Home : AppCompatActivity() {

    lateinit var busquedaFragment: BusquedaFragment
    lateinit var parcelasFragment: ParcelasFragment
    lateinit var historicoFragment: HistoricoFragment
    lateinit var graficoFragment: GraficoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val Bottom_Navigation: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val Fl_Primero: FrameLayout = findViewById<FrameLayout>(R.id.fl_primero)


        parcelasFragment = ParcelasFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_primero, parcelasFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()



        Bottom_Navigation.setOnItemSelectedListener { it ->
            when(it.itemId){
                R.id.busqueda -> {
                    busquedaFragment = BusquedaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_primero,busquedaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.crear -> {
                    parcelasFragment = ParcelasFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_primero,parcelasFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.historico -> {
                    historicoFragment = HistoricoFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_primero,historicoFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.grafico -> {
                    graficoFragment = GraficoFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_primero,graficoFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

            }
            true
        }
    }
}