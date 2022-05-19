package com.tyron.talkrr.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyron.talkrr.R
import com.tyron.talkrr.ui.util.text.ValidatingTextField
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = getViewModel()) {

    val isLoggingIn = viewModel.loading.observeAsState()
    val usernameErrorMessage = viewModel.usernameErrorMessage.observeAsState()
    val passwordErrorMessage = viewModel.passwordErrorMessage.observeAsState()

    LoginScreenContent(
        onLogin = viewModel::login,
        isLoggingIn = isLoggingIn.value ?: false,
        usernameErrorMessage = usernameErrorMessage.value ?: "",
        passwordErrorMessage = passwordErrorMessage.value ?: "",
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    onLogin: ((email: String, password: String) -> Unit)? = null,
    isLoggingIn: Boolean = false,
    usernameErrorMessage: String = "",
    passwordErrorMessage: String = "",
) {
    Scaffold(
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Header()
                Spacer(modifier = Modifier.height(32.dp))
                Content(
                    onLogin = onLogin,
                    isLoggingIn = isLoggingIn,
                    usernameErrorMessage = usernameErrorMessage,
                    passwordErrorMessage = passwordErrorMessage,
                )
            }
        }
    )
}

@Composable
fun Header() {
    Text(
        text = stringResource(R.string.login_title),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        modifier = Modifier
            .padding(start = 16.dp)
    )
    Text(
        text = stringResource(R.string.register_btn),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 24.sp,
        modifier = Modifier
            .padding(start = 16.dp)
    )
}

@Composable
private fun Content(
    onLogin: ((email: String, password: String) -> Unit)?,
    isLoggingIn: Boolean,
    usernameErrorMessage: String = "",
    passwordErrorMessage: String = "",
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

    ValidatingTextField(
        label = {
            Text(stringResource(R.string.login_email_txtfield_label))
        },
        error = usernameErrorMessage,
        value = email.value,
        onValueChange = { email.value = it },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        shape = CircleShape,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(24.dp))
    ValidatingTextField(
        label = {
            Text(stringResource(R.string.login_password_txtfield_label))
        },
        error = passwordErrorMessage,
        value = password.value,
        onValueChange = { password.value = it },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image =
                if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            // TODO: Localize
            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconToggleButton(
                checked = passwordVisible.value,
                onCheckedChange = { passwordVisible.value = !passwordVisible.value }
            ) {
                Icon(imageVector = image, description)
            }
        },
        shape = CircleShape,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = {
            if (isLoggingIn) {
                return@Button
            }
            onLogin?.invoke(email.value, password.value)
        },
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
    ) {
        val hasErrors = usernameErrorMessage.isNotEmpty() or passwordErrorMessage.isNotEmpty()

        if (isLoggingIn and !hasErrors) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(stringResource(R.string.login_btn))
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreenContent()
}