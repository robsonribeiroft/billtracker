package br.com.rrdev.billtracker.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.recyclers.RegistroAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {


    private lateinit var registroAdapter: RegistroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        registroAdapter = RegistroAdapter().apply {
            onItemClick = ::handleOnItemClick
        }

        recycler_search.run {
            adapter = registroAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        btn_close.setOnClickListener {
            edittxt.setText("")
        }

        edittxt.requestFocus()
        edittxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btn_close.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                //TODO SEARCH HERE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun handleOnItemClick(position: Int){
        Log.d("BILLT_RACKER_LOG", "valor registro: ${registroAdapter.getItem(position)}")
    }
}