package com.tyron.talkrr.data.network.fcm

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Saves the FCM ID tokens in Firestore.
 */
class FcmTokenUpdater @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val externalScopeApp: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    fun updateTokenForUser(userId: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            // Write token to /users/<userId>/fcmTokens/<token[0..TOKEN_ID_LENGTH]/
            val tokenInfo = mapOf(
                LAST_VISIT_KEY to FieldValue.serverTimestamp(),
                TOKEN_ID_KEY to token
            )

            // All Firestore operations start from the main thread to avoid concurrency issues.
            externalScopeApp.launch(mainDispatcher) {
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(FCM_IDS_COLLECTION)
                    .document(token.take(TOKEN_ID_LENGTH))
                    .set(tokenInfo, SetOptions.merge()).addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            Timber.d("FCM ID token successfully uploaded for user $userId\"")
//                        } else {
//                            Timber.e("FCM ID token: Error uploading for user $userId")
//                        }
                    }
            }
        }
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val LAST_VISIT_KEY = "lastVisit"
        private const val TOKEN_ID_KEY = "tokenId"
        private const val FCM_IDS_COLLECTION = "fcmTokens"
        private const val TOKEN_ID_LENGTH = 25
    }
}
