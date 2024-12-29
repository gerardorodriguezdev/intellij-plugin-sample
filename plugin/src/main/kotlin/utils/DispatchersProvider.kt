package utils

import kotlin.coroutines.CoroutineContext

interface DispatchersProvider {
    fun main(): CoroutineContext
    fun io(): CoroutineContext
}