package testing

import logging.Logger

class FakeLogger : Logger {
    private val _info = mutableListOf<String?>()
    val info: List<String?> = _info

    private val _error = mutableListOf<String?>()
    val error: List<String?> = _error

    override fun info(message: String?) {
        _info.add(message)
    }

    override fun error(message: String?) {
        _error.add(message)
    }
}