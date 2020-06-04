package br.com.rrdev.billtracker.recyclers

import androidx.recyclerview.widget.DiffUtil
import br.com.rrdev.billtracker.models.Registro

class RegistroDiffUtil(private val oldList: List<Registro>, private val newList: List<Registro>): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }
}