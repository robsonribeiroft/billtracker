package br.com.rrdev.billtracker

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.rrdev.billtracker.BillTrackerApplication.Companion.application
import br.com.rrdev.billtracker.dao.ReceitaDao
import br.com.rrdev.billtracker.models.Receita

@Database(entities = [(Receita::class)], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun receitaDao(): ReceitaDao

    companion object{

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase{
            if (instance == null){
                synchronized(AppDatabase::class.java){
                    instance = Room.databaseBuilder(application, AppDatabase::class.java, "appDB.db").build()
                }
            }
            return instance!!
        }

        fun destroyInstance(){
            instance = null
        }
    }
}