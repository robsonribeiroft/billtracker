package br.com.rrdev.billtracker.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.rrdev.billtracker.models.Receita

@Dao
interface ReceitaDao {

    @Query("SELECT * FROM receita ORDER BY data COLLATE LOCALIZED ASC")
    fun getAllReceitasOrderByDate(): List<Receita>


    @Query("SELECT * FROM receita WHERE descricao LIKE '%'||:text||'%'")
    fun searchReceitasByText(text: String): List<Receita>
}