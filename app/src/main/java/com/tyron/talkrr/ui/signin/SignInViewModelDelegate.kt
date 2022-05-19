package com.tyron.talkrr.ui.signin

import com.tyron.talkrr.data.network.auth.AuthenticatedUserInfo
import com.tyron.talkrr.data.repository.AuthRepository
import com.tyron.talkrr.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

interface SignInViewModelDelegate {

    /**
     * Live updated value of the current user
     */
    val userInfo: StateFlow<AuthenticatedUserInfo?  >

    val userId: StateFlow<String?>

    /**
     * Returns the current user id value, null if not available
     */
    val userIdValue: String?

    val userValue: User?

    val userState: StateFlow<SignInState>
}

internal class FirebaseSignInViewModelDelegate(
    authRepository: AuthRepository,
    applicationScope: CoroutineScope
) : SignInViewModelDelegate {

    private val currentFirebaseUser: Flow<Result<AuthenticatedUserInfo>> =
        authRepository.authStateChanges().map {
            it
        }

    override val userInfo: StateFlow<AuthenticatedUserInfo?> =
        currentFirebaseUser
            .map { it.getOrNull() }
            .stateIn(applicationScope, SharingStarted.WhileSubscribed(), null)

    override val userState: StateFlow<SignInState> =
        userInfo
            .map { it?.getState() ?: SignInState.LOADING }
            .stateIn(applicationScope, SharingStarted.Eagerly, SignInState.LOADING)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userId: StateFlow<String?> =
        userInfo
            .mapLatest { it?.getUid() }
            .stateIn(applicationScope, SharingStarted.WhileSubscribed(), null)

    override val userIdValue: String? =
        userInfo.value?.getUid()

    override val userValue: User?
        get() = userInfo.value?.let {
            User(
                id = it.getUid()?: "",
                name = it.getName()?: "",
                username = it.getUsername() ?: "",
                imageUrl = it.getProfilePictureUrl()?: ""
            )
        }
}