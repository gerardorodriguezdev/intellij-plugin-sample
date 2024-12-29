package logging

interface Logger {
    fun info(message: String?)
    fun error(message: String?)
}