package com.tyron.talkrr.ui

import androidx.lifecycle.ViewModel
import com.tyron.talkrr.ui.signin.SignInViewModelDelegate

class TalkrrAppViewModel(
    signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate