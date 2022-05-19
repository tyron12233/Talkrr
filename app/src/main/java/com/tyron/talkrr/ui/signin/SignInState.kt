package com.tyron.talkrr.ui.signin

/**
 * The state of the current sign in operation
 */
enum class SignInState {
    /**
     * The data is still being fetched from the network. The authentication state of the
     * user is not yet determined.
     */
    LOADING,

    /**
     * The user is logged in but has not set up their account credentials.
     */
    LOGGED_NOT_VALID,

    /**
     * The user is logged in.
     */
    LOGGED_VALID,

    /**
     * The device is not connected to the internet. The authentication state cannot be detemined.
     */
    NOT_CONNECTED
}