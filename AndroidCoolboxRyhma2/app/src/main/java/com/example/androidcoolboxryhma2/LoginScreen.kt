package com.example.androidcoolboxryhma2

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.LoginViewModel

@Composable
fun LoginScreen(goToCategoriesScreen: () -> Unit){
    val vm: LoginViewModel = viewModel()
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = vm.loginState.value.error) {
        vm.loginState.value.error?.let {
            Toast.makeText(context, vm.loginState.value.error, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = vm.loginState.value.loginDone) {
        if(vm.loginState.value.loginDone) {
            vm.setLogin(false)
            goToCategoriesScreen()
        }
    }

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
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                value = vm.loginState.value.password,
                onValueChange = { newPassword -> vm.setPassword(newPassword)},
                label = {Text("Password")},
                trailingIcon = {
                    //Button to change password visibility
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        //Change icon and show password in clear text or as hidden
                        Icon(imageVector = if (isPasswordVisible) ImageVector.vectorResource(R.drawable.baseline_visibility_24)
                        else ImageVector.vectorResource(R.drawable.baseline_visibility_off_24)
                            , contentDescription = if (isPasswordVisible) "Hide password"
                            else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = vm.loginState.value.username != "" &&
                        vm.loginState.value.password != "",
                onClick = {
                    vm.login()
                }) {
                    Text("Login")
                }
            }
        }
    }
}