package br.com.rrdev.billtracker.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.models.Registro

class RegistroAdapter: RecyclerView.Adapter<RegistroViewHolder>() {

    var registroList = emptyList<Registro>()
        set(value) {
            val diffUtil = RegistroDiffUtil(registroList, value)
            val calculateDiff = DiffUtil.calculateDiff(diffUtil)
            calculateDiff.dispatchUpdatesTo(this)
            field = value
        }
    var onItemClick: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RegistroViewHolder(inflater.inflate(
            R.layout.registro_adapter_item,
            parent,
            false),
            onItemClick
        )
    }

    fun isEmpty() = registroList.isEmpty()

    override fun getItemCount() = registroList.size

    fun getItem(position: Int) = registroList[position]

    override fun onBindViewHolder(holder: RegistroViewHolder, position: Int) {
        holder.setRegistro(registroList[position])
    }
}