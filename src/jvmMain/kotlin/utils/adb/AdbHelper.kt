package utils.adb

class AdbHelper {
    suspend fun version() = AdbProcess.exec(listOf("version"))

    suspend fun shell() = AdbProcess.exec(listOf("shell"))

    suspend fun allPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun systemPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun downloadPackages() = AdbProcess.exec(listOf("shell", "pm", "list", "packages"))

    suspend fun processes() = AdbProcess.exec(listOf("shell", "ps"))

    suspend fun devices() = AdbProcess.exec(listOf("devices"))

    suspend fun deviceModel() = AdbProcess.exec(listOf("shell", "getprop", "ro.product.model"))

    suspend fun deviceBrand() = AdbProcess.exec(listOf("shell", "getprop", "ro.product.brand"))

    suspend fun deviceName() = AdbProcess.exec(listOf("shell", "getprop", "ro.product.name"))

    suspend fun deviceBuildSDK() = AdbProcess.exec(listOf("shell", "getprop", "ro.build.version.sdk"))

    suspend fun deviceBuildRelease() = AdbProcess.exec(listOf("shell", "getprop", "ro.build.version.release"))

    suspend fun installApk(path: String) = AdbProcess.exec(listOf("install", path))

    suspend fun uninstallApk(packageName: String) = AdbProcess.exec(listOf("uninstall", packageName))

    suspend fun clearApkData(packageName: String) = AdbProcess.exec(listOf("shell", "pm", "clear", packageName))

    suspend fun clearApkDataByShell(packageName: String) = AdbProcess.exec(listOf("pm", "clear", packageName), true)
    
    suspend fun screenSize() = AdbProcess.exec(listOf("shell", "wm", "size"))

    suspend fun screenDensity() = AdbProcess.exec(listOf("shell", "wm", "density"))

    suspend fun dumpActivities(packageName: String) = AdbProcess.exec(listOf("shell", "dumpsys", "activity", "package", packageName))
}