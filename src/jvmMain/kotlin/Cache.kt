import utils.adb.AdbInfo

data class Cache(
    val adb: AdbInfo
)

lateinit var localConfigPath: String
var localConfigFilename = "config.json"
