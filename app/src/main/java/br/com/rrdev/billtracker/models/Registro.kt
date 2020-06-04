package br.com.rrdev.billtracker.models

interface Registro {

    fun getDateFormattedToList(): String

    fun getValueFormattedToList(): String

    fun getType(): String

    fun paymentIsResolved(): Boolean

    fun getDescription(): String



}