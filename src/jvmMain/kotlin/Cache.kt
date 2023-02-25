import utils.adb.AdbInfo
import utils.obtainFlow2
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

data class Device(
    val model: String,
    val brand: String = "",
    val sdkVersion: Int = 0,
    val releaseVersion: Int = 0,
    val remark: String = "",
    val valid: Boolean = false,
) {
    companion object{
        fun default() = Device(
            model = "unknown",
        )
    }
}

var localCache = Cache.default()
var currentDevice: Device = Device.default()

val adbInfoFlow = obtainFlow2<AdbInfo>()
val signFlow = obtainFlow2<SignInfo>()
val deviceFlow = obtainFlow2<Device>()

lateinit var localConfigPath: String
var localConfigFilename = "config.json"
