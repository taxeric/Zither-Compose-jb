package page.setting

import Cache
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ext.covertStr
import kotlinx.coroutines.launch
import localConfigFilename
import localConfigPath
import utils.AdbInfo
import utils.AdbUtil
import utils.FileUtil
import utils.adbInfoFlow

@Composable
fun SettingScreen() {
    val adbInfo = adbInfoFlow.collectAsState(initial = AdbInfo()).value
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("adb 路径")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val path = FileUtil.openCommonFileDialog()
                    if (path.isNotEmpty()) {
                        val newInfo = AdbInfo(path = path)
                        adbInfoFlow.tryEmit(newInfo)
                        AdbUtil.adbPath = path
                        val cache = Cache(adb = newInfo)
                        coroutine.launch { FileUtil.write(cache.covertStr(), localConfigPath, localConfigFilename) }
                    }
                }
            ) {
                Text("重新选择")
            }
            Text(adbInfo.path)
        }
    }
}
