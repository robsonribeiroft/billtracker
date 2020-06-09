package br.com.rrdev.billtracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

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