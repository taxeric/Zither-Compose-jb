package utils

object AdbHelper {
    suspend fun version() = AdbUtil.exec(listOf("version"))

    suspend fun shell() = AdbUtil.exec(listOf("shell"))

    suspend fun allPackages() = AdbUtil.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun systemPackages() = AdbUtil.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun downloadPackages() = AdbUtil.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun processes() = AdbUtil.exec(listOf("shell", "ps"))

    suspend fun devices() = AdbUtil.exec(listOf("devices"))

    suspend fun installApk(path: String) = AdbUtil.exec(listOf("install", path))

    suspend fun uninstallApk(packageName: String) = AdbUtil.exec(listOf("uninstall", packageName))
}