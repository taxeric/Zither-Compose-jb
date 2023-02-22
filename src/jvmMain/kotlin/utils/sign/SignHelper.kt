package utils.sign

import utils.CommandResult

class SignHelper {

    suspend fun zipalign(): CommandResult = SignProcess.runZipalign()

    suspend fun sign(): CommandResult = SignProcess.runSign()

    suspend fun sign(keyInfo: KeyInfo, originFilePath: String) = SignProcess.runSign(keyInfo, originFilePath)

    suspend fun exec(): CommandResult = SignProcess.exec()

    suspend fun showJksInfo(
        file: String,
        storepass: String,
        containsMD5: Boolean = true,
        keytoolPath: String? = null
    ) = SignProcess.showJksInfo(file, storepass, containsMD5, keytoolPath)
}