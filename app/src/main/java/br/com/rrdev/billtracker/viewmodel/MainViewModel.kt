package br.com.rrdev.billtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.rrdev.billtracker.models.states.UIState
import br.com.rrdev.billtracker.repositories.FirebaseRepository

class MainViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository.getInstance()
    val uiState = firebaseRepository.dbDespesasLiveData as LiveData<UIState>

    fun getRegistrosFromFireStore(){
        firebaseRepository.getDespesasFromDB()
    }

    /*
    private fun lastSignInAccount(): User? {
        val acct = GoogleSignIn.getLastSignedInAccount(application)

        return if (acct != null) {
            User(id=acct.id, name = acct.displayName, email = acct.email, photoUrl = acct.photoUrl)
        } else null
    }

    private fun signOut(){
        googleSignClient.signOut()
            .addOnCompleteListener(this) {task ->
                // ...
            }
    }
    */


}