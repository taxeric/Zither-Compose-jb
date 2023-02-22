package page.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import page.common.selectionTextView
import utils.FileUtil
import utils.ProcessManager

@Composable
fun MainScreen() {
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        rowItems1(coroutine)
        rowItems2(coroutine)
    }
}

@Composable
fun rowItems1(coroutineScope: CoroutineScope) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .padding(12.dp, 0.dp)
        ) {
            Text("安装APK")
        }
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .padding(12.dp, 0.dp)
        ) {
            Text("卸载APK")
        }
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .padding(12.dp, 0.dp)
        ) {
            Text("清除数据")
        }
    }
}

@Composable
fun rowItems2(coroutineScope: CoroutineScope) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                showDialog = !showDialog

            },
            modifier = Modifier
                .weight(1f)
                .padding(12.dp, 0.dp)
        ) {
            Text("获取key信息")
        }
    }
    if (showDialog) {
        getKeyInfoDialog(coroutineScope) { showDialog = !showDialog }
    }
}

@Composable
private fun getKeyInfoDialog(
    coroutineScope: CoroutineScope,
    onClose: () -> Unit
) {
    var path by remember { mutableStateOf("") }
    var storePwd by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    Dialog(
        state = rememberDialogState(position = WindowPosition(Alignment.Center)),
        onCloseRequest = { onClose() },
        title = "获取key信息"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(path)
            Button(
                onClick = {
                    val mPath = FileUtil.openCommonFileDialog()
                    if (!mPath.contains("null")) {
                        path = mPath
                    }
                }
            ) {
                Text("选择key")
            }
            AnimatedVisibility(path.isNotEmpty()) {
                OutlinedTextField(
                    value = storePwd,
                    onValueChange = {
                        storePwd = it
                    },
                    label = {
                        Text("store pwd")
                    }
                )
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (path.isEmpty() || storePwd.isEmpty()) {
                            return@launch
                        }
                        val result = ProcessManager.signHelper.showJksInfo(
                            path,
                            storePwd,
                        )
                        data = if (result.exitCode != 0) {
                            result.stderr
                        } else {
                            result.stdout
                        }
                    }
                }
            ) {
                Text("get")
            }
            Button(
                onClick = { data = "" }
            ) {
                Text("清空")
            }
            selectionTextView(data)
        }
    }
}
