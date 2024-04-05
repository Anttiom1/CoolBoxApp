package com.example.androidcoolboxryhma2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginClick: () -> Unit){
    val vm: LoginViewModel = viewModel()

Box(modifier = Modifier.fillMaxSize()) {
    when {
        vm.loginState.value.loading -> CircularProgressIndicator(
            modifier = Modifier.align(
                Alignment.Center
            )
        )
        else -> Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = vm.loginState.value.username,
                onValueChange = { newUsername -> vm.setUsername(newUsername)},
                label = {Text("Username")}
                )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                visualTransformation = PasswordVisualTransformation(),
                value = vm.loginState.value.password,
                onValueChange = { newPassword -> vm.setPassword(newPassword)},
                label = {Text("Password")}
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = vm.loginState.value.username != "" &&
                        vm.loginState.value.password != "",
                onClick = {
                    vm.login()
                    onLoginClick()
                    /*************** goToNextScreen() ****************/
                }) {
                    Text("Login")
                }
            }
        }
    }
}