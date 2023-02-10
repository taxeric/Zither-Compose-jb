package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext

abstract class CmdProcess {

    suspend fun realExec(
        args: List<String>,
    ): CommandResult {
        println(args)
        return withContext(Dispatchers.IO) {
            var cmd: Process? = null
            try {
                cmd = Runtime.getRuntime().exec(args.toTypedArray())
                val exitCode = async {
                    runInterruptible { cmd.waitFor() }
                }
                val stdout = async {
                    runInterruptible {
                        String(cmd.inputStream.readAllBytes(), Charsets.UTF_8)
                    }
                }
                val stderr = async {
                    runInterruptible {
                        String(cmd.errorStream.readAllBytes(), Charsets.UTF_8)
                    }
                }
                CommandResult(
                    exitCode = exitCode.await(),
                    stdout = stdout.await(),
                    stderr = stderr.await(),
                )
            } catch (e: Throwable) {
                CommandResult(
                    exitCode = cmd?.exitValue()?: CommandResult.PROCESS_ERROR,
                    stdout = "failed",
                    stderr = e.message?:"unknown"
                )
            } finally {
                cmd?.destroy()
            }
        }
    }
}

data class CommandResult(
    val exitCode: Int,
    val stdout: String = "",
    val stderr: String = "",
) {
    companion object {
        const val CHECK_FAILED   = -1
        const val COMMAND_EMPTY  = -2
        const val PROCESS_ERROR  = -3

        fun checkFailed(err: String = "") = CommandResult(exitCode = CHECK_FAILED, stderr = err)
    }
}