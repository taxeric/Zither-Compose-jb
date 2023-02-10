package utils.java

class JavaHelper {

    suspend fun environment() = JavaProcess.exec()
}