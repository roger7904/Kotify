package org.roger.kotify.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.roger.kotify.data.network.HttpClientProvider
import org.roger.kotify.ui.screens.ios.IosViewModel
import org.roger.kotify.ui.screens.main.MainViewModel
import org.roger.kotify.ui.screens.notification.NotificationSenderViewModel

fun commonModule() = module {
    single {
        HttpClientProvider.createHttpClient(
            json = get(),
        )
    }

    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    viewModel { NotificationSenderViewModel() }
    viewModel { MainViewModel() }
    viewModel { IosViewModel() }
}

expect fun platformModule(): Module