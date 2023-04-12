package utils.adb

import utils.ShellProcess
import utils.CommandResult

data class AdbInfo(
    val path: String = "",
    val version: String = "",
)

object AdbProcess : ShellProcess(){

    var adbPath = ""

    fun checkAdb() = adbPath.isNotEmpty()

    /**
     * @param args commands
     * @param shellStatus 如果已经调用[adb shell],则该值应为true
     */
    suspend fun exec(
        args: List<String>,
        shellStatus: Boolean = false
    ): CommandResult {
        if (!checkAdb()) {
            return CommandResult(exitCode = CommandResult.CHECK_FAILED)
        }
        if (args.isEmpty()) {
            return CommandResult(exitCode = CommandResult.COMMAND_EMPTY)
        }
        val parcelArgs = mutableListOf<String>().apply {
            if (!shellStatus) {
                add(adbPath)
            }
            addAll(args)
        }
        return realExec(parcelArgs)
    }
}