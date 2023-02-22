package utils.sign

import localConfigPath
import utils.CmdProcess
import utils.CommandResult
import utils.obtainFlow2

data class SignInfo(
    val zipalignPath: String = "",
    val apksignerPath: String = "",
    val keys: List<KeyInfo> = listOf(),
)

data class KeyInfo(
    val tag: String = "",
    val jksPath: String = "",
    val jksKeyStorePwd: String = "",
    val jksKeyAlias: String = "",
    val jksKeyPwd: String = "",
)

val signFlow = obtainFlow2<SignInfo>()

object SignProcess: CmdProcess() {

    /**
     * zipalign是一种zip归档对齐工具。它确保存档中所有未压缩的文件都与文件的开头对齐。这
     * 允许通过mmap直接访问这些文件，无需将这些数据复制到RAM中，并减少应用程序的内存使用。
     * 在将APK文件分发给用户之前，应使用zipalign优化APK文件。如果您使用Android Studio
     * 进行构建，这将自动完成。
     */
    var zipalignPath: String = ""
    var apksignerPath: String = ""
    var keytoolPath: String = ""
    /**
     * 签名文件路径
     */
    var jksPath: String = ""
    var jksKeyStorePwd: String = ""
    var jksKeyAlias: String = ""
    var jksKeyPwd: String = ""
    /**
     * 已加固需重签名的文件路径
     */
    var steadyFilePath: String = ""
    /**
     * 执行完优化命令后的文件路径
     */
    var zipalignCompletedFilePath = ""
    /**
     * 最终输出的文件
     */
    var outputFile: String = ""

    /**
     * 默认开启对齐优化
     */
    suspend fun exec(zipalign: Boolean = true): CommandResult {
        val zipalignResult: CommandResult?
        if (zipalign) {
            zipalignResult = runZipalign()
            if (zipalignResult.exitCode != 0) {
                return zipalignResult
            }
        }
        val originFilePath = if (zipalign) {
            zipalignCompletedFilePath
        } else {
            steadyFilePath
        }
        return runSign(originFilePath)
    }

    suspend fun runZipalign(): CommandResult {
        if (zipalignPath.isEmpty() || steadyFilePath.isEmpty()) {
            return CommandResult.checkFailed()
        }
        if (zipalignCompletedFilePath.isEmpty()) {
            zipalignCompletedFilePath = localConfigPath + "zipalign.apk"
        }
        val zipalignArgs = mutableListOf<String>().apply {
            add(zipalignPath)
            add("-p")
            add("-f")
            add("-v")
            add("4")
            add(steadyFilePath)
            add(zipalignCompletedFilePath)
        }
        return realExec(zipalignArgs)
    }

    suspend fun runSign(originFilePath: String = steadyFilePath): CommandResult {
        if (apksignerPath.isEmpty() || originFilePath.isEmpty() ||
            jksPath.isEmpty() || jksKeyStorePwd.isEmpty() ||
            jksKeyAlias.isEmpty() || jksKeyPwd.isEmpty()){
            return CommandResult.checkFailed()
        }
        if (outputFile.isEmpty()) {
            outputFile = localConfigPath + "signed.apk"
        }
        val signArgs = mutableListOf<String>().apply {
            add("java")
            add("-jar")
            add(apksignerPath)
            add("sign")
            add("--verbose")
            add("--ks")
            add(jksPath)
            add("--ks-pass")
            add("pass:$jksKeyStorePwd")
            add("--ks-key-alias")
            add(jksKeyAlias)
            add("--key-pass")
            add("pass:$jksKeyPwd")
            add("--out")
            add(outputFile)
            add(originFilePath)
        }
        return realExec(signArgs)
    }

    /**
     * 使用配置
     */
    suspend fun runSign(
        jksInfo: KeyInfo,
        originFilePath: String,
        outputFile: String = localConfigPath + "signed.apk"
    ) = runSign(jksInfo.jksPath, jksInfo.jksKeyStorePwd, jksInfo.jksKeyAlias, jksInfo.jksKeyPwd, originFilePath, outputFile)

    suspend fun runSign(
        jksPath: String,
        jksKeyStorePwd: String,
        jksKeyAlias: String,
        jksKeyPwd: String,
        originFilePath: String,
        outputFile: String = localConfigPath + "signed.apk"
    ): CommandResult {
        if (apksignerPath.isEmpty() || originFilePath.isEmpty() ||
            jksPath.isEmpty() || jksKeyStorePwd.isEmpty() ||
            jksKeyAlias.isEmpty() || jksKeyPwd.isEmpty()){
            return CommandResult.checkFailed()
        }
        val signArgs = mutableListOf<String>().apply {
            add("java")
            add("-jar")
            add(apksignerPath)
            add("sign")
            add("--verbose")
            add("--ks")
            add(jksPath)
            add("--ks-pass")
            add("pass:$jksKeyStorePwd")
            add("--ks-key-alias")
            add(jksKeyAlias)
            add("--key-pass")
            add("pass:$jksKeyPwd")
            add("--out")
            add(outputFile)
            add(originFilePath)
        }
        return realExec(signArgs)
    }

    /**
     * 创建一个密钥
     */
    suspend fun createKey(
        storePath: String,
        storePwd: String,
        alias: String,
        keyPwd: String,
        validity: Int = 10,
        keytoolPath: String = "keytool",
        name: String = "Unknown"
    ): CommandResult {
        if (!existKeytoolEnvironment()) {
            return CommandResult.checkFailed("环境变量不存在")
        }
        val args = mutableListOf<String>().apply {
            add(keytoolPath)
            add("-genkey")
            add("-alias")
            add(alias)
            add("-keypass")
            add(keyPwd)
            add("-keyalg")
            add("RSA")
            add("-keysize")
            add("2048")
            add("-validity")
            val realValidity = if (validity < 0 || validity > 100) {
                10
            } else {
                validity
            }
            add("${realValidity * 365}")
            add("-keystore")
            add("${storePath}.jks")
            add("-storepass")
            add(storePwd)
            add("-storetype")
            add("pkcs12")
        }
        return realExec(args)
    }

    /**
     * @param file jks文件路径
     */
    suspend fun showJksInfo(file: String, storepass: String, containsMD5: Boolean = true, keytoolPath: String?): CommandResult {
        if (!existKeytoolEnvironment()) {
            return CommandResult.checkFailed("环境变量不存在")
        }
        val args = mutableListOf<String>().apply {
            add(keytoolPath ?: "keytool")
            add("--list")
            add("-v")
            add("-keystore")
            add(file)
            add("-storepass")
            add(storepass)
        }
        return realExec(args)
    }

    suspend fun existKeytoolEnvironment() = realExec(listOf("keytool")).exitCode == 0
}