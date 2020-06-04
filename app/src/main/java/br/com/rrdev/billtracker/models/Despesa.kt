package br.com.rrdev.billtracker.models

import android.os.Parcelable
import br.com.rrdev.billtracker.utils.convertTimestampToDate
import br.com.rrdev.billtracker.utils.formatOnPattern
import br.com.rrdev.billtracker.utils.formatToReal
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Despesa(var valor: Long = 0,
                   var descricao: String = "",
                   var data: Long = 0,
                   var pago: Boolean = false,
                   var photo: String? = null
): Registro, Parcelable {

    override fun getDateFormattedToList(): String {
        val date = convertTimestampToDate(data)
        return date.formatOnPattern("dd MMM yyyy")
    }

    override fun getValueFormattedToList(): String = valor.formatToReal()

    override fun getType(): String = this::class.java.simpleName

    override fun paymentIsResolved(): Boolean = pago

    override fun getDescription(): String = descricao
}