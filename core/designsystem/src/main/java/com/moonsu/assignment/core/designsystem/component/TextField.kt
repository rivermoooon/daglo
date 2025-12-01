package com.moonsu.assignment.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moonsu.assignment.core.designsystem.component.preview.BasePreview
import com.moonsu.assignment.core.designsystem.component.preview.DagloPreview
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme

@Composable
fun DagloTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    limit: Int? = null,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    throttleTime: Long = 2000L,
    onDone: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    rightComponent: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var lastDoneTime by remember { mutableLongStateOf(0L) }
    var isFocused by remember { mutableStateOf(false) }

    val backgroundColor = when {
        readOnly -> DagloTheme.colors.surfaceVariant
        else -> DagloTheme.colors.surface
    }

    val textColor = when {
        readOnly -> DagloTheme.colors.onSurfaceVariant
        else -> DagloTheme.colors.onSurface
    }

    BasicTextField(
        value = value,
        onValueChange = { input ->
            when {
                limit != null -> if (input.length <= limit) onValueChange(input)
                else -> onValueChange(input)
            }
        },
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged(focusState.isFocused)
            },
        singleLine = true,
        readOnly = readOnly,
        textStyle = DagloTheme.typography.bodyMediumR.copy(color = textColor),
        cursorBrush = SolidColor(DagloTheme.colors.primary),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                handleDoneAction(
                    keyboardController = keyboardController,
                    focusManager = focusManager,
                    throttleTime = throttleTime,
                    lastDoneTime = lastDoneTime,
                    onDone = onDone,
                    onUpdateTime = { lastDoneTime = it },
                )
            },
        ),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            style = DagloTheme.typography.bodyMediumR,
                            color = DagloTheme.colors.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                    }
                    innerTextField()
                }
                rightComponent()
            }
        },
    )
}

private fun handleDoneAction(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    throttleTime: Long,
    lastDoneTime: Long,
    onDone: () -> Unit,
    onUpdateTime: (Long) -> Unit,
) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastDoneTime >= throttleTime) {
        keyboardController?.hide()
        onDone()
        focusManager.moveFocus(FocusDirection.Down)
        onUpdateTime(currentTime)
    }
}

@DagloPreview
@Composable
private fun PreviewDagloTextField() {
    var text by remember { mutableStateOf("") }
    BasePreview(
        content = {
            DagloTextField(
                value = text,
                onValueChange = { text = it },
                hint = "텍스트를 입력하세요",
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}

@DagloPreview
@Composable
private fun PreviewDagloTextFieldReadOnly() {
    var text by remember { mutableStateOf("읽기 전용 텍스트 필드") }
    BasePreview(
        content = {
            DagloTextField(
                value = text,
                onValueChange = { text = it },
                hint = "텍스트를 입력하세요",
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}
