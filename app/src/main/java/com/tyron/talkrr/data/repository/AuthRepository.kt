package com.tyron.talkrr.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tyron.talkrr.data.network.auth.AuthenticatedUserInfo
import com.tyron.talkrr.data.network.auth.ObserveUserAuthStateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loginWithGoogle(idToken: String) {
        withContext(ioDispatcher) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        }
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String) {
        return withContext(ioDispatcher) {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun logout() {
        return withContext(ioDispatcher) {
            firebaseAuth.signOut()
        }
    }

    fun authStateChanges(): Flow<Result<AuthenticatedUserInfo>> =
        observeUserAuthStateUseCase()

}