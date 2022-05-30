package com.example.volgaitushakov

import android.app.Application
import com.example.volgaitushakov.domain.module.GetStocksModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        setUpKoin()
    }

    private fun setUpKoin()
    {
        val getStocksModule = GetStocksModule()
        startKoin{
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(getStocksModule.getStocksApi())
        }
    }
}