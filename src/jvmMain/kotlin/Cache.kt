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
    val name: String,
    val info: String,
) {
    companion object{
        fun default() = Device(
            name = "unknown",
            info = ""
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
