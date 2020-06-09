package br.com.rrdev.billtracker.activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.states.*
import br.com.rrdev.billtracker.utils.convertTimestampToDate
import br.com.rrdev.billtracker.utils.formatOnPattern
import br.com.rrdev.billtracker.viewmodel.NewRegistroViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_registro.*
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*


class NewRegistroActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val PICK_IMAGE_REQUEST: Int = 3331
    private val PERMESSION_REQUEST: Int = 3332
    private lateinit var viewModel: NewRegistroViewModel
    private lateinit var despesa: Despesa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_registro)

        despesa = intent.extras?.getParcelable<Despesa>("registro") ?: Despesa()

        viewModel = ViewModelProvider(this).get(NewRegistroViewModel::class.java)

        setSupportActionBar(toolbar_new_registro)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        despesa.run {
            val centavos = valor % 100
            val reais = valor / 100
            edit_valor_field.setText(if (valor == 0L) "" else "$reais,$centavos")
            switch_payment.isActivated = pago
            button_select_date.text = convertTimestampToDate(data).formatOnPattern()
            edit_description_field.setText(descricao)
        }

        button_select_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            var mYear = calendar.get(Calendar.YEAR);
            var mMonth = calendar.get(Calendar.MONTH);
            var mDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog(this, OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth

                }, mYear, mMonth, mDay).run {

                setCancelable(false)
                setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        despesa.data = calendar.timeInMillis
                    } else{
                        dismiss()
                    }
                }
                show()
            }

        }

        switch_payment.setOnCheckedChangeListener { _, isChecked ->
            despesa.pago = isChecked
        }

        fab_save.setOnClickListener {
            if (allFieldsAreValid()){
                despesa.apply {
                    descricao = edit_description_field.text.toString()
                    var valorText = edit_valor_field.text.toString()
                    valor = valorText.replace(",","").toLong()
                    if (!valorText.contains(",")) valor*=100
                }
                viewModel.saveDespesaOnDB(despesa)
            }else{
                showMessage("Campos obrigatorios estão vazios")
            }

        }

        container_select_file.setOnClickListener {
            if (!allPermissionsGranted())
                ActivityCompat.requestPermissions(this, requiredPermissions, PERMESSION_REQUEST)
            else
                chooseImage()
        }

        viewModel.uiStateSavingInFirestore.observe(this, Observer(this::handleUIStateUpdate))

        viewModel.uiStateUploadFile.observe(this, Observer(this::handleUIStateUpdate))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                PICK_IMAGE_REQUEST->{
                    val uri = data?.data
                    text_name_file.text = uri?.path
                    img_file_prewiew.visibility = View.VISIBLE

                    Glide.with(this).load(uri).into(img_file_prewiew)
//                    viewModel.uploadFile(uri)
                }
                PERMESSION_REQUEST->{
                    chooseImage()
                }

            }
        }
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }



    private fun showMessage(message: String){
        Snackbar.make(fab_save, message, Snackbar.LENGTH_LONG).show()
    }

    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun handleUIStateUpdate(uiState: UIState){
        when(uiState){
            is Complete<*>->{
                showMessage("Registro Salvo com sucesso")
                finish()
            }
            is Loading->{
                Log.d("DB_FIRESTORE_TAG", "Loading")
            }
            is Error->{
                showMessage(getString(uiState.errorMsgId))
            }
            is LoadingProgress->{
                Log.d("DB_FIRESTORE_TAG", "${uiState.progress}%")
            }
        }
    }

    private fun allFieldsAreValid(): Boolean {
        val valorText = edit_valor_field.text.toString().replace(",", "")
        return (valorText.isNotEmpty() || valorText.toInt() != 0)
                && edit_description_field.text.toString().isNotEmpty()

    }

}