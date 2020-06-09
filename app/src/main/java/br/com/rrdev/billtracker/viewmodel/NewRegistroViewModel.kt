package br.com.rrdev.billtracker.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.states.UIState
import br.com.rrdev.billtracker.repositories.FirebaseRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NewRegistroViewModel(application: Application): AndroidViewModel(application), CoroutineScope {


    private val firebaseRepository = FirebaseRepository.getInstance()
    val uiStateSavingInFirestore = firebaseRepository.dbLiveData as LiveData<UIState>
    val uiStateUploadFile = firebaseRepository.fileUploadLiveData as LiveData<UIState>


    private val parentJob = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable->
        Log.d("VM_LOG","${throwable.message}")
    }

    fun saveDespesaOnDB(despesa: Despesa){
        if (despesa.id.isNullOrEmpty())
            firebaseRepository.saveDespesaInDatabase(despesa)
        else{
            firebaseRepository.updateDespesaInDatabase(despesa)
        }
    }

    fun uploadFile(uri: Uri?) = viewModelScope.launch{
        firebaseRepository.uploadImageToGoogleCloud(uri)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob + coroutineExceptionHandler


}