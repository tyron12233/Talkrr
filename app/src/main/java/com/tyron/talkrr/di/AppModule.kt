package com.tyron.talkrr.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tyron.talkrr.data.database.AppDatabase
import com.tyron.talkrr.data.network.auth.ObserveUserAuthStateUseCase
import com.tyron.talkrr.data.network.auth.datasources.AuthStateUserDataSource
import com.tyron.talkrr.data.network.auth.datasources.FirebaseAuthStateUserDataSource
import com.tyron.talkrr.data.network.auth.datasources.FirestoreRegisteredUserDataSource
import com.tyron.talkrr.data.network.auth.datasources.RegisteredUserDataSource
import com.tyron.talkrr.data.network.fcm.FcmTokenUpdater
import com.tyron.talkrr.data.repository.AuthRepository
import com.tyron.talkrr.ui.TalkrrAppViewModel
import com.tyron.talkrr.ui.login.LoginViewModel
import com.tyron.talkrr.ui.signin.FirebaseSignInViewModelDelegate
import com.tyron.talkrr.ui.signin.SignInViewModelDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { Firebase.firestore }
    single { Firebase.auth }
    single { Firebase.storage }
//    single { Firebase.functions("europe-west1") }

    // Data sources

    // Repositories
//    single { UserRepository(get(), get()) }
    single { AuthRepository(get(), get()) }

    // Room db
    single { AppDatabase.init(androidContext()) }
    factory { get<AppDatabase>().userDao() }

    // Scope
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // ViewModels
    viewModel { TalkrrAppViewModel(get()) }
    viewModel { LoginViewModel(get())}

    factory<RegisteredUserDataSource> { FirestoreRegisteredUserDataSource(get()) }
    single<AuthStateUserDataSource> { FirebaseAuthStateUserDataSource(get(), get(), get()) }
    factory { FcmTokenUpdater(get(), get()) }

    single { ObserveUserAuthStateUseCase(get(), get(), get()) }
    single<SignInViewModelDelegate> { FirebaseSignInViewModelDelegate(get(), get()) }
}