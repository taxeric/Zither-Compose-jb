import utils.adb.AdbInfo
import utils.sign.SignInfo

data class Cache(
    val adb: AdbInfo,
    val sign: SignInfo,
) {
    companion object{
        fun default() = Cache(
            adb = AdbInfo(),
            sign = SignInfo()
        )
    }
}

var localCache = Cache.default()

lateinit var localConfigPath: String
var localConfigFilename = "config.json"
