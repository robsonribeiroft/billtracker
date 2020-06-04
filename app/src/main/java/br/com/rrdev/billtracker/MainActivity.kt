package br.com.rrdev.billtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.Receita
import br.com.rrdev.billtracker.models.Registro
import br.com.rrdev.billtracker.recyclers.RegistroAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()

    }


    private fun setupViews(){
        val navController = findNavController(R.id.navigation_host_fragment)
        bottom_nav.setupWithNavController(navController)
    }
}