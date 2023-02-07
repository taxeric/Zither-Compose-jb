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

data class AdbInfo(
    val path: String = "",
    val version: String = "",
)

val adbInfoFlow = obtainFlow2<AdbInfo>()

object AdbUtil {

    var adbPath = ""

    fun checkAdb() = adbPath.isNotEmpty()

    /**
     * @param args commands
     * @param shellStatus 是否进入设备,如果已经调用[adb shell],则该值为true,命令不会携带[adb path] adb
     */
    suspend fun exec(
        args: List<String>,
        shellStatus: Boolean = false
    ): CommandResult {
        if (checkAdb()) {
            return CommandResult(exitCode = -1)
        }
        if (args.isEmpty()) {
            return CommandResult(exitCode = -2)
        }
        val parcelArgs = mutableListOf<String>().apply {
            if (!shellStatus) {
                add(adbPath)
                add("adb")
            }
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