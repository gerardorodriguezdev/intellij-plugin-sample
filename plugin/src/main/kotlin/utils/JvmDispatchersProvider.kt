package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlin.coroutines.CoroutineContext

class JvmDispatchersProvider : DispatchersProvider {
    override fun main(): CoroutineContext = Dispatchers.Swing
    override fun io(): CoroutineContext = Dispatchers.IO
}