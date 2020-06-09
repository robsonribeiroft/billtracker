package br.com.rrdev.billtracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.activities.NewRegistroActivity
import br.com.rrdev.billtracker.activities.SearchActivity
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.Receita
import br.com.rrdev.billtracker.models.Registro
import br.com.rrdev.billtracker.models.states.*
import br.com.rrdev.billtracker.recyclers.RegistroAdapter
import br.com.rrdev.billtracker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.container_registro_header.*
import kotlinx.android.synthetic.main.container_registro_header.view.*
import kotlinx.android.synthetic.main.fragment_registro.*
import kotlinx.android.synthetic.main.fragment_registro.view.*


class RegistroFragment: Fragment() {

    private lateinit var registroAdapter: RegistroAdapter
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerForContextMenu(view.header.button_select_filter)

        button_search.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        fab_detail_registro.setOnClickListener {
            startActivity(Intent(context, NewRegistroActivity::class.java))
        }

        registroAdapter = RegistroAdapter().apply {
            onItemClick = ::handleOnItemClick
        }

        recycler.run {
            adapter = registroAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer(this::handleUIStateUpdate))

    }

    override fun onResume() {
        super.onResume()
        viewModel.getRegistrosFromFireStore()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menu_filter, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_all_registros-> {
                Log.d("MENU", "menu_all_registros")
                true
            }
            R.id.menu_despesas->{
                Log.d("MENU", "menu_despesas")
                true
            }
            R.id.menu_receitas->{
                Log.d("MENU", "menu_receitas")
                true
            }
            else-> super.onContextItemSelected(item)
        }
    }

    private fun handleOnItemClick(position: Int){
        val intent = Intent(context, NewRegistroActivity::class.java).apply {
            putExtra("registro", registroAdapter.getItem(position) as Despesa)
        }
        startActivity(intent)
        Log.d("BILLT_RACKER_LOG", "valor registro: ${registroAdapter.getItem(position)}")
    }

    private fun handleUIStateUpdate(uiState: UIState){
        when(uiState){
            is Loading->{
                recycler.visibility = View.GONE
                load_content.visibility = View.VISIBLE
                empty_content.visibility = View.GONE
            }

            is CompleteWithNoData->{
                recycler.visibility = View.GONE
                load_content.visibility = View.GONE
                empty_content.visibility = View.VISIBLE
            }
            is Complete<*>->{
                registroAdapter.registroList = uiState.data as List<Registro>
                recycler.visibility = View.VISIBLE
                load_content.visibility = View.GONE
                empty_content.visibility = View.GONE
            }
            is Error->{
                if (!registroAdapter.isEmpty()) {
                    recycler.visibility = View.VISIBLE
                    load_content.visibility = View.GONE
                    empty_content.visibility = View.GONE
                }  else{
                    recycler.visibility = View.GONE
                    load_content.visibility = View.GONE
                    empty_content.visibility = View.VISIBLE
                }
                Toast.makeText(context, getString(uiState.errorMsgId), Toast.LENGTH_LONG).show()
            }
        }
    }
}