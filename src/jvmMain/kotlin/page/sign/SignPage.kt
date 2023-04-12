package page.sign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import localCache
import page.common.CustomTab
import page.common.SimpleRadioGroup
import page.common.chooseFileItem
import page.common.titleText
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

    var zipalignEnable by remember { mutableStateOf(false) }
    var tips by remember { mutableStateOf("Completed !") }

    val tabs = mutableListOf<CustomTab>().apply {
        localCache.sign.keys.forEach {
            add(CustomTab(it.tag))
        }
    }
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(end = 12.dp)
    ) {
        titleText(
            "选择Key",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        )
        keyItems(tabs, selectedIndex) {
            selectedIndex = it
        }
        titleText(
            "设置输入输出文件",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = zipalignEnable,
                onCheckedChange = {
                    zipalignEnable = it
                }
            )
            Text(
                "启用对齐",
                modifier = Modifier
                    .clickable {
                        zipalignEnable = !zipalignEnable
                    }
            )
        }
        chooseFileItem(
            openFolder = false,
            tips = "未签名文件",
            path = steadyFilePath,
            filename = "",
            onPathChanged = { steadyFilePath = it },
            onValueChanged = {},
            modifier = Modifier
                .fillMaxWidth()
        )
        AnimatedVisibility(visible = zipalignEnable) {
            chooseFileItem(
                path = zipalignCompletedFilePath,
                tips = "对齐后路径",
                filename = zipalignCompletedFilename,
                onPathChanged = { zipalignCompletedFilePath = it },
                onValueChanged = { zipalignCompletedFilename = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        chooseFileItem(
            path = outputFilePath,
            tips = "签名后路径",
            filename = outputFilename,
            onPathChanged = { outputFilePath = it },
            onValueChanged = { outputFilename = it },
            modifier = Modifier
                .fillMaxWidth()
        )
        titleText(
            "执行命令",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    coroutine.launch {
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
                        ProcessManager.signHelper.exec()
                    }
                },
                modifier = Modifier
            ) {
                Text("RUN")
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                tips,
                color = Color.Green,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            )
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
