package utils.sign

import utils.CommandResult

class SignHelper {

    suspend fun zipalign(): CommandResult = SignProcess.runZipalign()

    suspend fun sign(): CommandResult = SignProcess.runSign()

    suspend fun exec(): CommandResult = SignProcess.exec()

    suspend fun showJksInfo(file: String, storepass: String, keytoolPath: String?) = SignProcess.showJksInfo(file, storepass, keytoolPath)
}