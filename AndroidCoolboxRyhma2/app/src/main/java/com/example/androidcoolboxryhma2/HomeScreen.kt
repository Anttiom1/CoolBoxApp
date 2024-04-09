package com.example.androidcoolboxryhma2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(goToGraph: () -> Unit, logOut: () -> Unit){
    val vm: HomeViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")

                }},
                title = { "Home" }
            )}
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .height(100.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(50.dp),
                text = "CoolBox",
                fontSize = 32.sp)

            Row (modifier = Modifier
                .height(100.dp)
            ){
                Column (modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                ){
                    Text(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        , text = "Lämpötila")
                    Row (modifier = Modifier
                        .align(Alignment.CenterHorizontally)) {
                        Text("In")
                        Text("Out")
                    }
                }

                Column (modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                ){
                    Column (modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    ){
                        Text(modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            , text = "Sähkönkulutus")
                        Row (modifier = Modifier
                            .align(Alignment.CenterHorizontally)) {
                            Text("Weekly")
                            Text("Daily")
                        }
                    }
                }
            }

            Row {
                Button(
                    onClick = { goToGraph() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Graph")
                }

                Button(
                    onClick = { logOut() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Log Out")
                }
            }

        }

    }
}