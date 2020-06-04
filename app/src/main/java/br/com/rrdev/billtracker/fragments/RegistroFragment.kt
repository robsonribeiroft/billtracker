package br.com.rrdev.billtracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.activities.NewRegistroActivity
import br.com.rrdev.billtracker.activities.SearchActivity
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.Receita
import br.com.rrdev.billtracker.models.Registro
import br.com.rrdev.billtracker.recyclers.RegistroAdapter
import kotlinx.android.synthetic.main.container_registro_header.*
import kotlinx.android.synthetic.main.container_registro_header.view.*
import kotlinx.android.synthetic.main.fragment_registro.*
import kotlinx.android.synthetic.main.fragment_registro.view.*


class RegistroFragment: Fragment() {

    private lateinit var registroAdapter: RegistroAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val staticList = listOf<Registro>(
            Despesa(
            valor = 1200,
            data = System.currentTimeMillis(),
            descricao = "O valor é em centavosO valor é em centavosO valor é em centavosO valor é em centavosO valor é em centavos",
                pago = true),
            Despesa(
                valor = 1200,
                data = System.currentTimeMillis(),
                descricao = "O valor é em centavos", pago = false),
            Receita(
                valor = 1200,
                data = System.currentTimeMillis(),
                descricao = "O valor é em centavos", recebido = true),
            Receita(
                valor = 1200,
                data = System.currentTimeMillis(),
                descricao = "O valor é em centavos", recebido = false)
        )
        registroAdapter.registroList = staticList


        registerForContextMenu(view.header.button_select_filter)
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
        Log.d("BILLT_RACKER_LOG", "valor registro: ${registroAdapter.getItem(position)}")
    }
}