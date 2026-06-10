package com.applogin.loginyregistro.core.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applogin.loginyregistro.core.network.RetrofitClient
import com.applogin.loginyregistro.authapp.data.local.TokenManager
import com.applogin.loginyregistro.authapp.data.repository.AuthRepositoryImpl
import com.applogin.loginyregistro.authapp.presentation.auth.AuthLoadingScreen
import com.applogin.loginyregistro.authapp.presentation.auth.AuthLoadingUiState
import com.applogin.loginyregistro.authapp.presentation.auth.AuthLoadingViewModel
import com.applogin.loginyregistro.authapp.presentation.home.HomeScreen
import com.applogin.loginyregistro.authapp.presentation.home.HomeViewModel
import com.applogin.loginyregistro.authapp.presentation.login.LoginScreen
import com.applogin.loginyregistro.authapp.presentation.login.LoginViewModel
import com.applogin.loginyregistro.authapp.presentation.register.RegisterScreen
import com.applogin.loginyregistro.authapp.presentation.register.RegisterViewModel

@androidx.compose.runtime.Composable
fun AppNavGraph() {
    val context = LocalContext.current.applicationContext
    val navController = rememberNavController()
    val repository = remember(context) {
        AuthRepositoryImpl(
            apiService = RetrofitClient.createAuthApiService(context),
            tokenManager = TokenManager(context)
        )
    }

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_LOADING
    ) {
        composable(Routes.AUTH_LOADING) {
            val viewModel: AuthLoadingViewModel = viewModel(
                factory = AuthLoadingViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsState()

            AuthLoadingScreen()

            LaunchedEffect(uiState) {
                when (uiState) {
                    AuthLoadingUiState.Authenticated -> navController.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH_LOADING) { inclusive = true }
                    }
                    AuthLoadingUiState.Unauthenticated -> navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.AUTH_LOADING) { inclusive = true }
                    }
                    AuthLoadingUiState.Loading -> Unit
                }
            }
        }

        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsState()

            LoginScreen(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = viewModel::login,
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )

            LaunchedEffect(uiState.isLoginSuccessful) {
                if (uiState.isLoginSuccessful) {
                    viewModel.resetNavigationState()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.REGISTER) {
            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsState()

            RegisterScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onRegisterClick = viewModel::register,
                onLoginClick = { navController.popBackStack() }
            )

            LaunchedEffect(uiState.isRegisterSuccessful) {
                if (uiState.isRegisterSuccessful) {
                    viewModel.resetNavigationState()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.HOME) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsState()

            HomeScreen(
                uiState = uiState,
                onLogoutClick = viewModel::logout
            )

            LaunchedEffect(uiState.isLoggedOut) {
                if (uiState.isLoggedOut) {
                    viewModel.resetLogoutState()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }
        }
    }
}
