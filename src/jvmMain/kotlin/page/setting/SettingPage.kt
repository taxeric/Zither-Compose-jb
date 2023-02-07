package page.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ext.covertStr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import localCache
import localConfigFilename
import localConfigPath
import utils.adb.AdbInfo
import utils.adb.AdbProcess
import utils.FileUtil
import utils.adb.adbInfoFlow
import utils.sign.SignInfo
import utils.sign.SignProcess
import utils.sign.signFlow

@Composable
fun SettingScreen() {
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        adbView(coroutine)
        signView(coroutine)
    }
}

@Composable
fun adbView(coroutine: CoroutineScope) {
    val adbInfo = adbInfoFlow.collectAsState(initial = AdbInfo()).value
    Column {
        Text("adb")
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
                        AdbProcess.adbPath = path
                        localCache = localCache.copy(adb = newInfo)
                        coroutine.launch { FileUtil.write(localCache.covertStr(), localConfigPath, localConfigFilename) }
                    }
                }
            ) {
                Text("重新选择")
            }
            Text(adbInfo.path)
        }
    }
}

@Composable
fun signView(coroutine: CoroutineScope) {
    val signInfo = signFlow.collectAsState(initial = SignInfo()).value
    Column {
        Text("sign")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val path = FileUtil.openCommonFileDialog()
                    if (path.isNotEmpty()) {
                        val newInfo = signInfo.copy(zipalignPath = path)
                        signFlow.tryEmit(newInfo)
                        SignProcess.zipalignPath = path
                        val lastInfo = localCache.sign.copy(zipalignPath = path)
                        localCache = localCache.copy(sign = lastInfo)
                        coroutine.launch { FileUtil.write(localCache.covertStr(), localConfigPath, localConfigFilename) }
                    }
                }
            ) {
                Text("zipalign path")
            }
            Text(signInfo.zipalignPath)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val path = FileUtil.openCommonFileDialog()
                    if (path.isNotEmpty()) {
                        val newInfo = signInfo.copy(apksignerPath = path)
                        signFlow.tryEmit(newInfo)
                        SignProcess.zipalignPath = path
                        val lastInfo = localCache.sign.copy(apksignerPath = path)
                        localCache = localCache.copy(sign = lastInfo)
                        coroutine.launch { FileUtil.write(localCache.covertStr(), localConfigPath, localConfigFilename) }
                    }
                }
            ) {
                Text("apksigner path")
            }
            Text(signInfo.apksignerPath)
        }
        Text("共存在 ${signInfo.keys.size} 个签名文件")
        signInfo.keys.forEach { key ->
            Text(
                buildAnnotatedString {
                    append("签名: ")
                    withStyle(SpanStyle(color = Color.Blue)) {
                        append(key.tag)
                    }
                }
            )
        }
    }
}
