package br.com.rrdev.billtracker.utils

import java.text.NumberFormat
import java.util.*

fun Long.formatToReal(locale: Locale = Locale("pt", "BR")): String{
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this / 100.0)

}