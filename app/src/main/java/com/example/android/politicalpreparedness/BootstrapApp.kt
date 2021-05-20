package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.Repository
import com.example.android.politicalpreparedness.database.RepositoryImpl
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class BootstrapApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val capstoneAppModules = module {
            single { return@single CivicsApi.retrofitService }
            single { ElectionDatabase.getInstance(get()).electionDao }
            single<Repository> { RepositoryImpl(get(), get()) }
            viewModel { RepresentativeViewModel(get(), get()) }
            viewModel { ElectionsViewModel(get(), get()) }
            viewModel { (followString: String, unfollowString: String, election: Election) ->
                VoterInfoViewModel(
                    get(),
                    followString,
                    unfollowString,
                    election
                )
            }
        }

        startKoin {
            androidContext(this@BootstrapApp)
            androidLogger()
            modules(capstoneAppModules)
        }
    }
}
