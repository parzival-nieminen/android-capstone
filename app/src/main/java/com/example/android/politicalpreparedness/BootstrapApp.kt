package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class BootstrapApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val capstoneAppModules = module {
            single { ElectionDatabase.getInstance(this@BootstrapApp) }
            single { (get() as ElectionDatabase).electionDao }
        }

        startKoin {
            androidContext(this@BootstrapApp)
            androidLogger()
            //modules(capstoneAppModules)
        }
    }

}