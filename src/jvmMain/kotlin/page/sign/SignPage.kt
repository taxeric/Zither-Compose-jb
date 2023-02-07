package page.sign

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import utils.FileUtil

@Composable
fun SignScreen() {
    var steadyFilePath by remember { mutableStateOf("") }
    var steadyFilename by remember { mutableStateOf("") }
    var zipalignCompletedFilePath by remember { mutableStateOf("") }
    var zipalignCompletedFilename by remember { mutableStateOf("") }
    var outputFilePath by remember { mutableStateOf("") }
    var outputFilename by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 12.dp, 0.dp)
    ) {
        signFileItem(
            path = steadyFilePath,
            filename = steadyFilename,
            onPathChanged = { steadyFilePath = it },
            onValueChanged = { steadyFilename = it }
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
    }
}

@Composable
private fun signFileItem(
    path: String,
    onPathChanged: (String) -> Unit,
    filename: String,
    onValueChanged: (String) -> Unit,
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
                val mPath = FileUtil.openCommonFolderDialog()
                if (mPath.isNotEmpty()) {
                    onPathChanged(mPath)
                }
            },
            modifier = Modifier
                .weight(1f)
        ) {
            Text("set")
        }
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
