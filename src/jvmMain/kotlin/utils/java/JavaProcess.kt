package utils.java

import utils.ShellProcess
import utils.CommandResult

object JavaProcess: ShellProcess() {

    var javaPath = ""

    suspend fun exec(path: String = javaPath): CommandResult {
        val javaArgs = mutableListOf<String>().apply {
            if (path.isNotEmpty()) {
                add(path)
            }
            add("java")
            add("-version")
        }
        return realExec(javaArgs)
    }
}