package utils.adb

class AdbHelper {
    suspend fun version() = AdbProcess.exec(listOf("version"))

    suspend fun shell() = AdbProcess.exec(listOf("shell"))

    suspend fun allPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun systemPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun downloadPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun processes() = AdbProcess.exec(listOf("shell", "ps"))

    suspend fun devices() = AdbProcess.exec(listOf("devices"))

    suspend fun installApk(path: String) = AdbProcess.exec(listOf("install", path))

    suspend fun uninstallApk(packageName: String) = AdbProcess.exec(listOf("uninstall", packageName))

    suspend fun clearApkData(packageName: String) = AdbProcess.exec(listOf("shell", "pm", "clear", packageName))

    suspend fun clearApkDataByShell(packageName: String) = AdbProcess.exec(listOf("pm", "clear", packageName), true)
}