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
                    exitCode = cmd?.exitValue()?:-3,
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
)