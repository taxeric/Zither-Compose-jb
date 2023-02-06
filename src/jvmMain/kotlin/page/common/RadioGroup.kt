package page.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/**
 * 自定义radiobutton
 *
 * @param modifier 最外层
 * @param contentModifier 子项
 * @param orientation 方向
 * @param tabs 显示内容
 * @param onSelected 选中事件
 * @param content 填充
 */
@Composable
fun SimpleRadioGroup(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    defaultSelected: Int = 0,
    tabs: List<CustomTab>,
    onSelected: (Int, CustomTab) -> Unit,
    content: @Composable (
        tab: CustomTab,
        selected: String,
        childModifier: Modifier
    ) -> Unit = { _,_,_ -> },
) {
    if (tabs.isEmpty()) {
        return
    }
    var selectedTab by remember {
        mutableStateOf(tabs[defaultSelected].tag)
    }
    if (orientation == Orientation.Horizontal) {
        Row(
            modifier = modifier
                .selectableGroup()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemView(
                modifier = contentModifier,
                selectedTab = selectedTab,
                tabs = tabs,
                onSelected = { index, tab ->
                    selectedTab = tabs[index].tag
                    onSelected(index, tab)
                },
                content = content
            )
        }
    } else {
        Column(
            modifier = modifier
                .selectableGroup()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemView(
                modifier = contentModifier,
                selectedTab = selectedTab,
                tabs = tabs,
                onSelected = { index, tab ->
                    selectedTab = tabs[index].tag
                    onSelected(index, tab)
                },
                content = content
            )
        }
    }
}

@Composable
private fun ItemView(
    modifier: Modifier,
    selectedTab: String,
    tabs: List<CustomTab>,
    onSelected: (Int, CustomTab) -> Unit,
    content: @Composable (
        tab: CustomTab,
        selected: String,
        childModifier: Modifier
    ) -> Unit = { _,_,_ -> },
) {
    tabs.forEachIndexed { index, tab ->
        Box(
            modifier = modifier
                .selectable(
                    selected = selectedTab == tab.tag,
                    onClick = {
                        onSelected(index, tabs[index])
                    },
                    role = Role.RadioButton
                )
        ) {
            content(tab, selectedTab, Modifier.align(Alignment.Center))
        }
    }
}

class CustomTab(
    val text: String,
    val tag: String = text
)
