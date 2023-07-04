package com.xdmse.logouttestfromscratch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.amplifyframework.kotlin.core.Amplify
import com.xdmse.logouttestfromscratch.ui.theme.LogOutTestFromScratchTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogOutTestFromScratchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        val scrollState = rememberScrollState()
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .verticalScroll(scrollState)
                        ) {
                            LoginComposable()
                            Divider()
                            FetchAuthSessionComposable()
                            Divider()
                            GetCurrentUserComposable()
                        }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable() {
    val coroutineScope = rememberCoroutineScope()
    val loginString = remember { mutableStateOf("") }
    val passString = remember { mutableStateOf("") }
    val loginResult = remember { mutableStateOf("") }
    Text("Login")
    TextField(value = loginString.value, onValueChange = { loginString.value = it })
    Text("Pass")
    TextField(value = passString.value, onValueChange = { passString.value = it })
    TextButton(onClick = {
        coroutineScope.launch {
            val result = CognitoAuthentication().loginUser(loginString.value, passString.value)
            loginResult.value = result.toString()
        }
    }) {
        Text("Login")
    }
    Text("Login result: ${loginResult.value}")
}

@Composable
fun FetchAuthSessionComposable() {
    val coroutineScope = rememberCoroutineScope()
    val fetchAuthSessionResult = remember { mutableStateOf("") }
    TextButton(onClick = {
        coroutineScope.launch {
            val result = Amplify.Auth.fetchAuthSession()
            fetchAuthSessionResult.value = result.toString()
        }
    }) {
        Text("Fetch auth session")
    }
    Text("FetchAuthSession result: ${fetchAuthSessionResult.value}")
}

@Composable
fun GetCurrentUserComposable() {
    val coroutineScope = rememberCoroutineScope()
    val string = remember { mutableStateOf("") }
    TextButton(onClick = {
        coroutineScope.launch {
            try {
                val result = Amplify.Auth.getCurrentUser()
                string.value = result.toString()
            } catch (e: Exception) {
                string.value = e.toString()
            }
        }
    }) {
        Text("GetCurrentUser")
    }
    Text("GetCurrentUser result: ${string.value}")
}