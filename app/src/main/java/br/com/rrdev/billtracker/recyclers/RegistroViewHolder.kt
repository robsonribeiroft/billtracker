package br.com.rrdev.billtracker.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.models.Receita
import br.com.rrdev.billtracker.models.Registro
import kotlinx.android.synthetic.main.registro_adapter_item.view.*

class RegistroViewHolder(private val view: View,
                         onItemClick: ((position: Int) -> Unit)? = null): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            onItemClick?.invoke(adapterPosition)
        }
    }

    fun setRegistro(registro: Registro){
        view.run {
            bar_indicator.setBackgroundColor(
                resources.getColor(
                    if (registro.getType() == Receita::class.java.simpleName) R.color.colorIndicatorBarReceita
                    else R.color.colorIndicatorBarDespesa,
                    null)
            )

            img_icon_status.setImageResource(
                if (registro.paymentIsResolved()) R.drawable.ic_confirmado
                else R.drawable.ic_pendente
            )

            textview_description.text = registro.getDescription()
            textview_date.text = registro.getDateFormattedToList()
            textview_price.text = registro.getValueFormattedToList()

        }
    }
}
