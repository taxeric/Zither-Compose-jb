package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import kotlin.text.Charsets.UTF_8

/**
 *
 */
data class CommandResult(
    val exitCode: Int,
    val stdout: String = "",
    val stderr: String = "",
)

object AdbUtil {

    private var adbPath = ""

    fun checkAdb() = adbPath.isNotEmpty()

    suspend fun exec(args: List<String>): CommandResult {
        if (checkAdb()) {
            return CommandResult(exitCode = -1)
        }
        if (args.isEmpty()) {
            return CommandResult(exitCode = -2)
        }
        val parcelArgs = mutableListOf<String>().apply {
            add(adbPath)
            add("adb")
            addAll(args)
        }
        return withContext(Dispatchers.IO) {
            var cmd: Process? = null
            try {
                cmd = Runtime.getRuntime().exec(parcelArgs.toTypedArray())
                val exitCode = async {
                    runInterruptible { cmd.waitFor() }
                }
                val stdout = async {
                    runInterruptible {
                        String(cmd.inputStream.readAllBytes(), UTF_8)
                    }
                }
                val stderr = async {
                    runInterruptible {
                        String(cmd.errorStream.readAllBytes(), UTF_8)
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