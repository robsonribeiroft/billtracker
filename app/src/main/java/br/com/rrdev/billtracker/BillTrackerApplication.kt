package br.com.rrdev.billtracker

import android.app.Application
import android.content.Context

class BillTrackerApplication: Application() {


    companion object{
        lateinit var application: Context
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        application = applicationContext
        database = AppDatabase.getInstance()
    }
}