package br.com.rrdev.billtracker.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.FileUploadState
import br.com.rrdev.billtracker.models.Registro
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.coroutines.CoroutineContext

class NewRegistroViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    val registroLiveData = MutableLiveData<Despesa>()
    val fileUploadStateLive = MutableLiveData<FileUploadState>()
    private val db = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storeReference = firebaseStorage.reference
    private val parentJob = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable->
        Log.d("VM_LOG","${throwable.message}")
    }

    fun saveRegistroOnFireStore(despesa: Despesa){
        db.collection("despesas")
            .add(despesa)
            .addOnSuccessListener {

            }
            .addOnFailureListener{

            }
    }

    fun uploadFileToGoogleStorage(uri: Uri?){
        if (uri == null ){
            return
        }
        val childReference = storeReference.child("image/${UUID.randomUUID()}")
        childReference.putFile(uri).addOnSuccessListener {
            fileUploadStateLive.postValue(FileUploadState(true))
            Log.d("URL_CLOUD", "${childReference.downloadUrl}")
            Log.d("URL_CLOUD", "${it.uploadSessionUri}")

        }.addOnFailureListener{
            fileUploadStateLive.postValue(FileUploadState(false, error = it.message))

        }.addOnProgressListener {
            val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob + coroutineExceptionHandler


}