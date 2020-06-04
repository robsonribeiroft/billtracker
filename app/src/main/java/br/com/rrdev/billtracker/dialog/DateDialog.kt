package br.com.rrdev.billtracker.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import br.com.rrdev.billtracker.R
import kotlinx.android.synthetic.main.dialog_date.*
import java.util.*

class DateDialog: DialogFragment() {

    private var onComplete: ((timestamp: Long)->Unit)? = null

    fun setOnComplete(onComplete: ((timestamp: Long)->Unit)?){
        this.onComplete = onComplete
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_date, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var resultDate = Calendar.getInstance()
        date_picker.init(date_picker.year, date_picker.month, date_picker.dayOfMonth) { view, year, monthOfYear, dayOfMonth ->
            resultDate.run {
                add(Calendar.YEAR, year)
                add(Calendar.MONTH, monthOfYear)
                add(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        button_yes.setOnClickListener {
            onComplete?.invoke(resultDate.time.time)
            dismiss()
        }

        button_no.setOnClickListener {
            onComplete?.invoke(System.currentTimeMillis())
            dismiss()
        }
    }
}