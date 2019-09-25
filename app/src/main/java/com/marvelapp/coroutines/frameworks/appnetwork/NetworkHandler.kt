package com.marvelapp.coroutines.frameworks.appnetwork

import androidx.lifecycle.MutableLiveData
import com.marvelapp.coroutines.AppExceptions
import com.marvelapp.coroutines.marvelhome.data.entities.MarvelCharactersBaseResponse
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.resume

const val genericErrorMessage = "Something didn't go as planned"

fun <RESPONSE : MarvelCharactersBaseResponse<RESPONSE>> getRemoteDate(function: NetworkHandler<RESPONSE>.() -> Unit)
        : MutableLiveData<ScreenState<RESPONSE>> = NetworkHandler<RESPONSE>().apply(function).mapResponseToScreenState()


class NetworkHandler<RESPONSE : Any> {

    lateinit var client: Deferred<Response<RESPONSE>>

    fun mapResponseToScreenState(): MutableLiveData<ScreenState<RESPONSE>> {

        val screenState = MutableLiveData<ScreenState<RESPONSE>>()

        screenState.value = ScreenState.LoadingState

        GlobalScope.launch {
            runCatching {
                client.getRemoteData().getDataOrThrowException() as MarvelCharactersBaseResponse<RESPONSE>
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    screenState.value = ScreenState.DataStat(it.retrieveData())
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    screenState.value = ScreenState.ErrorState(it)
                }
            }
        }
        return screenState
    }

    private suspend fun <T : Any> Deferred<Response<T>>.getRemoteData(): ResponseState<T> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            GlobalScope.launch {
                runCatching {
                    await()
                }.onFailure {
                    cancellableContinuation.resume(ResponseState.EXCEPTION(it))
                }.onSuccess {
                    cancellableContinuation.resume(
                        if (it.isSuccessful) {
                            getBodyOrThrowException(it)
                        } else {
                            ResponseState.EXCEPTION(
                                AppExceptions.HttpException(
                                    it.errorBody()?.string() ?: genericErrorMessage
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun <T : Any> getBodyOrThrowException(response: Response<T>?): ResponseState<T> {
        return response?.let { nonNullResponse ->
            nonNullResponse.body()?.let { body ->
                ResponseState.SUCCESS(body)
            } ?: ResponseState.EXCEPTION(AppExceptions.GenericErrorException(genericErrorMessage))
        } ?: ResponseState.EXCEPTION(AppExceptions.GenericErrorException(genericErrorMessage))

    }

    private fun <T : Any> ResponseState<T>.getDataOrThrowException(throwable: Throwable? = null): T {
        return when (this) {
            is ResponseState.SUCCESS -> value
            is ResponseState.EXCEPTION -> throw throwable ?: exception
        }
    }
}

sealed class ScreenState<out T : Any> {
    class DataStat<T : Any>(val value: T) : ScreenState<T>()
    class ErrorState(val exception: Throwable) : ScreenState<Nothing>()
    object LoadingState : ScreenState<Nothing>()

}

private sealed class ResponseState<out T : Any> {
    class SUCCESS<T : Any>(val value: T) : ResponseState<T>()
    class EXCEPTION(val exception: Throwable) : ResponseState<Nothing>()
}