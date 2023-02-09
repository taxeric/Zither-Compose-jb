package page.sign

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import localCache
import page.common.CustomTab
import page.common.SimpleRadioGroup
import utils.FileUtil
import utils.ProcessManager
import utils.sign.SignProcess

@Composable
fun SignScreen() {
    val coroutine = rememberCoroutineScope()
    var steadyFilePath by remember { mutableStateOf("") }
    var steadyFilename by remember { mutableStateOf("") }
    var zipalignCompletedFilePath by remember { mutableStateOf("") }
    var zipalignCompletedFilename by remember { mutableStateOf("") }
    var outputFilePath by remember { mutableStateOf("") }
    var outputFilename by remember { mutableStateOf("") }

    val tabs = mutableListOf<CustomTab>().apply {
        localCache.sign.keys.forEach {
            add(CustomTab(it.tag))
        }
    }
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 12.dp, 0.dp)
    ) {
        Text("key")
        keyItems(tabs, selectedIndex) {
            selectedIndex = it
        }
        Text("output")
        signFileItem(
            openFolder = false,
            path = steadyFilePath,
            filename = "",
            onPathChanged = { steadyFilePath = it },
            onValueChanged = {}
        )
        signFileItem(
            path = zipalignCompletedFilePath,
            filename = zipalignCompletedFilename,
            onPathChanged = { zipalignCompletedFilePath = it},
            onValueChanged = { zipalignCompletedFilename = it }
        )
        signFileItem(
            path = outputFilePath,
            filename = outputFilename,
            onPathChanged = { outputFilePath = it },
            onValueChanged = { outputFilename = it }
        )
        Button(
            onClick = {
                SignProcess.steadyFilePath = steadyFilePath
                SignProcess.zipalignCompletedFilePath = zipalignCompletedFilePath + zipalignCompletedFilename
                SignProcess.outputFile = outputFilePath + outputFilename
                val selectKey = localCache.sign.keys[selectedIndex]
                SignProcess.jksPath = selectKey.jksPath
                SignProcess.jksKeyStorePwd = selectKey.jksKeyStorePwd
                SignProcess.jksKeyAlias = selectKey.jksKeyAlias
                SignProcess.jksKeyPwd = selectKey.jksKeyPwd
                println(SignProcess.jksPath)
                println(SignProcess.jksKeyStorePwd)
                println(SignProcess.jksKeyAlias)
                println(SignProcess.jksKeyPwd)
                println(SignProcess.steadyFilePath)
                println(SignProcess.zipalignCompletedFilePath)
                println(SignProcess.outputFile)
            }
        ) {
            Text("save")
        }
        Button(
            onClick = {
                coroutine.launch {
                    ProcessManager.signHelper.exec()
                }
            }
        ) {
            Text("run")
        }
    }
}

@Composable
private fun keyItems(tabs: List<CustomTab>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    SimpleRadioGroup(
        tabs = tabs,
        onSelected = { index, _ ->
            onSelected(index)
        },
        defaultSelected = selectedIndex,
        modifier = Modifier,
        contentModifier = Modifier
            .padding(8.dp, 8.dp)
    ) { tab, selected, childModifier ->
        Text(
            text = tab.text,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontSize = 12.sp,
            modifier = childModifier
                .border(
                    1.dp,
                    if (selected == tab.tag) Color.DarkGray else Color.Transparent,
                    RoundedCornerShape(4.dp)
                )
                .padding(12.dp, 12.dp)
        )
    }
}

@Composable
private fun signFileItem(
    path: String,
    onPathChanged: (String) -> Unit,
    filename: String,
    onValueChanged: (String) -> Unit,
    openFolder: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            path,
            modifier = Modifier
                .weight(3f)
        )
        Button(
            onClick = {
                val mPath = if (openFolder) FileUtil.openCommonFolderDialog() else FileUtil.openCommonFileDialog()
                if (mPath.isNotEmpty()) {
                    onPathChanged(mPath)
                }
            },
            modifier = Modifier
                .weight(1f)
        ) {
            Text("set")
        }
        if (openFolder) {
            OutlinedTextField(
                label = {
                    Text("文件名")
                },
                value = filename,
                onValueChange = onValueChanged,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}
