package page.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.FileUtil

@Composable
fun chooseFileItem(
    tips: String,
    path: String,
    onPathChanged: (String) -> Unit,
    filename: String,
    onValueChanged: (String) -> Unit,
    openFolder: Boolean = true,
    lineColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                tips,
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(36.dp)
                    .border(1.dp, Color.Blue, RoundedCornerShape(2.dp))
            ) {
                Text(
                    path,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
            }
            OutlinedButton(
                onClick = {
                    val mPath = if (openFolder) FileUtil.openCommonFolderDialog() else FileUtil.openCommonFileDialog()
                    if (mPath.isNotEmpty()) {
                        onPathChanged(mPath)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("选择")
            }
        }
        if (openFolder) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    "设置文件名",
                    modifier = Modifier
                        .weight(1f)
                )
                OutlinedTextField(
                    label = {
                        Text("文件名")
                    },
                    value = filename,
                    onValueChange = onValueChanged,
                    modifier = Modifier
                        .weight(2f)
                )
                Text(
                    ".apk",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = lineColor)
    }
}
