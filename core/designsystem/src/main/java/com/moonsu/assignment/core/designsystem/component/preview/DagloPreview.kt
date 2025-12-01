package com.moonsu.assignment.core.designsystem.component.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme

// 아래 폴드는 예시이고 fontScale 같은 값도 조정이 가능합니다.
// @Preview(name = "Galaxy Z Fold3 접힌화면 (840x2289)", widthDp = 320, heightDp = 870, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@PreviewScreenSizes
annotation class DagloPreview

@Composable
fun BasePreview(content: @Composable () -> Unit = {}) {
    DagloTheme {
        Surface(color = DagloTheme.colors.onPrimary) {
            content()
        }
    }
}
