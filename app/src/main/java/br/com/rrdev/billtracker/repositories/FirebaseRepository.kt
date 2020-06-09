package br.com.rrdev.billtracker.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.states.*
import br.com.rrdev.billtracker.repositories.FirebaseCollection.Despesas
import br.com.rrdev.billtracker.repositories.FirebaseCollection.ImagesBucket
import br.com.rrdev.billtracker.utils.serializeToMap
import br.com.rrdev.billtracker.utils.toDataClass
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

sealed class FirebaseCollection(val name: String){
    object Despesas: FirebaseCollection("despesas")
//    object Receitas: FirebaseCollection("receitas")
    object ImagesBucket: FirebaseCollection("images")
}


class FirebaseRepository {

    private val TAG = "DB_FIRESTORE_TAG"

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    val dbLiveData = MutableLiveData<UIState>(Loading)
    val dbDespesasLiveData = MutableLiveData<UIState>(Loading)
    val fileUploadLiveData = MutableLiveData<UIState>()

    fun saveDespesaInDatabase(despesa: Despesa){
        db.collection(Despesas.name)
            .add(despesa.serializeToMap())
            .addOnSuccessListener {
                dbLiveData.postValue(Complete<Any>())
            }
            .addOnFailureListener{
                dbLiveData.postValue(Error(errorMsgId = R.string.error_save_despesa_in_db))
            }
    }

    fun updateDespesaInDatabase(despesa: Despesa){
        db.collection(Despesas.name).document(despesa.id)
            .set(despesa.serializeToMap())
            .addOnSuccessListener {
                dbLiveData.postValue(Complete<Any>())
            }
            .addOnFailureListener{
                dbLiveData.postValue(Error(errorMsgId = R.string.error_save_despesa_in_db))
            }
    }

    fun getDespesasFromDB(){
        dbDespesasLiveData.postValue(Loading)
        db.collection(Despesas.name)
            .get()
            .addOnSuccessListener { result: QuerySnapshot? ->

                val list = result?.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.data.toDataClass<Despesa>().apply {
                        id = queryDocumentSnapshot.id
                    }
                } ?: emptyList()
                dbDespesasLiveData.postValue(if (list.isEmpty()) CompleteWithNoData else Complete(list))
            }
            .addOnFailureListener {
                dbDespesasLiveData.postValue(Error(errorMsgId = R.string.error_get_despesas_from_db))
            }
    }

    suspend fun uploadImageToGoogleCloud(uri: Uri?) {
        fileUploadLiveData.postValue(Loading)

        if (uri == null){
            fileUploadLiveData.postValue(Error(R.string.error_file_upload))
            return
        }

        val fileName = UUID.randomUUID()
        val reference = storage.reference
        reference
            .child("${ImagesBucket.name}/$fileName")
            .putFile(uri)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->

                Log.d("STORAGE_URL", "URL: ${taskSnapshot.uploadSessionUri?.path}")
                Log.d("STORAGE_URL", "URL: ${taskSnapshot.storage.downloadUrl}")

            fileUploadLiveData.postValue(CompleteWithNoData)

        }.addOnFailureListener{
            fileUploadLiveData.postValue(Error(R.string.error_file_upload))

        }.addOnProgressListener {
            val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
            Log.d("STORAGE_TAGE", "progress: $progress")
            fileUploadLiveData.postValue(LoadingProgress(progress = progress.toInt()))

        }
    }


    companion object{

        @Volatile
        private var instance: FirebaseRepository? = null

        fun getInstance(): FirebaseRepository{
            if (instance == null){
                synchronized(FirebaseRepository::class.java){
                    instance = FirebaseRepository()
                }
            }
            return instance!!
        }
    }
}