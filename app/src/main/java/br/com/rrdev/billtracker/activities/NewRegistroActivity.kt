package br.com.rrdev.billtracker.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.dialog.DateDialog
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.FileUploadState
import br.com.rrdev.billtracker.models.Registro
import br.com.rrdev.billtracker.utils.convertTimestampToDate
import br.com.rrdev.billtracker.utils.formatOnPattern
import br.com.rrdev.billtracker.viewmodel.NewRegistroViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_registro.*
import kotlinx.android.synthetic.main.fragment_registro.*

class NewRegistroActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val PICK_IMAGE_REQUEST: Int = 3331
    private val PERMESSION_REQUEST: Int = 3332
    private lateinit var viewModel: NewRegistroViewModel
    private var despesa = Despesa()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_registro)

        viewModel = ViewModelProvider(this).get(NewRegistroViewModel::class.java)


        button_select_date.setOnClickListener {
            val dialog = DateDialog()
            dialog.setOnComplete {
                button_select_date.text = convertTimestampToDate(it).formatOnPattern("dd/MM/yyyy")
                despesa.data = it
            }
            dialog.show(supportFragmentManager, null)
        }

        fab_save.setOnClickListener {
            despesa.apply {
                valor = processValue(text_value.text.toString())
                pago = switch_payment.isSelected
                data = if (this.data == 0L) System.currentTimeMillis() else this.data
                descricao = textview_description.text.toString()
            }

            viewModel.saveRegistroOnFireStore(despesa)
        }

        button_add_attachment.setOnClickListener {
            if (!allPermissionsGranted())
                ActivityCompat.requestPermissions(this, requiredPermissions, PERMESSION_REQUEST)
            else
                chooseImage()
        }

        viewModel.registroLiveData.observe(this, Observer {

        })

        viewModel.fileUploadStateLive.observe(this, Observer {fileUploadState ->
            if (fileUploadState.isSuccessFull){
                button_add_attachment.text = fileUploadState.url ?: "empty"
            }else{
                showMessage("FileUploadError: ${fileUploadState.error}")
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(resultCode){
                PICK_IMAGE_REQUEST->{
                    val uri = data?.data
                    viewModel.uploadFileToGoogleStorage(uri)
                }
                PERMESSION_REQUEST->{
                    chooseImage()
                }

            }
        }
    }

    private fun processValue(value: String): Long{
        return 0L
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    private fun showMessage(message: String){
        Snackbar.make(fab_save, message, Snackbar.LENGTH_LONG).show()
    }

    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}