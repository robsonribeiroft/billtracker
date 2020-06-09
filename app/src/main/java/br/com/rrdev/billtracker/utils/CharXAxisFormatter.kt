package br.com.rrdev.billtracker.utils

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class CharXAxisFormatter: IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return convertTimestampToDate(value.toLong()).formatOnPattern("dd/MM/yyyy")
    }
}