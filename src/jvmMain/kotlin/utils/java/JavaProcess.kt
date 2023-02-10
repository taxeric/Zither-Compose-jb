package utils.java

import utils.CmdProcess
import utils.CommandResult

object JavaProcess: CmdProcess() {

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