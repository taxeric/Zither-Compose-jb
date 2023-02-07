package utils

import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun openCommonFolderDialog(
        window: ComposeWindow? = null,
        title: String = "选择目录"
    ): String = FileDialog(window, title, FileDialog.LOAD).apply {
        isVisible = true
    }.directory

    fun openCommonFileDialog(
        window: ComposeWindow? = null,
        title: String = "选择文件"
    ): String {
        val dialog = FileDialog(window, title, FileDialog.LOAD).apply {
            isVisible = true
        }
        return dialog.directory + dialog.file
    }

    suspend fun read(path: String, filename: String): String {
        val file = File(path, filename)
        if (!file.exists()) {
            return ""
        }
        return withContext(Dispatchers.IO) {
            async {
                file.readText()
            }.await()
        }
    }

    suspend fun write(str: String, path: String, filename: String, append: Boolean = false) {
        if (str.isEmpty()) {
            return
        }
        val file = File(path, filename)
        withContext(Dispatchers.IO) {
            if (!file.exists()) {
                file.createNewFile()
                file.writeText(str)
            } else {
                FileOutputStream(file, append).use { it.write(str.toByteArray(Charsets.UTF_8)) }
            }
        }
    }
}