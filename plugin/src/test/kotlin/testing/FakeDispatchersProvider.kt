package testing

import kotlinx.coroutines.Dispatchers
import utils.DispatchersProvider
import kotlin.coroutines.CoroutineContext

class FakeDispatchersProvider: DispatchersProvider {
    override fun main(): CoroutineContext = Dispatchers.Unconfined
    override fun io(): CoroutineContext = Dispatchers.Unconfined
}