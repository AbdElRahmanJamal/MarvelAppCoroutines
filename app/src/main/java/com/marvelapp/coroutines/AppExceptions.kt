package com.marvelapp.coroutines

import java.io.IOException

sealed class AppExceptions : IOException() {

    object NoConnectivityException : AppExceptions()
    data class HttpException(val errorMessage: String) : AppExceptions()
    data class GenericErrorException(val errorMessage: String) : AppExceptions()

}