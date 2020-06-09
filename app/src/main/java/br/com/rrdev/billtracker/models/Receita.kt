package br.com.rrdev.billtracker.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.rrdev.billtracker.utils.convertTimestampToDate
import br.com.rrdev.billtracker.utils.formatOnPattern
import br.com.rrdev.billtracker.utils.formatToReal
import br.com.rrdev.billtracker.utils.serializeToMap
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Receita(
    @PrimaryKey(autoGenerate = true)
    var roomId: Long = 0,
    var valor: Long = 0,
    var descricao: String = "",
    var data: Long = 0,
    var recebido: Boolean = false,
    var photoUrl: String? = null
): Registro, Parcelable {

    override fun getDateFormattedToList(): String {
        val date = convertTimestampToDate(data)
        return date.formatOnPattern("dd MMM yyyy")
    }

    override fun getValueFormattedToList(): String = valor.formatToReal()

    override fun getType() = this::class.java.simpleName

    override fun paymentIsResolved(): Boolean = recebido

    override fun getDescription() = descricao

}