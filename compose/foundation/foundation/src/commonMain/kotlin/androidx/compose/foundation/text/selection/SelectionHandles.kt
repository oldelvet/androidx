/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.foundation.text.selection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.style.ResolvedTextDirection
import androidx.compose.ui.unit.dp

internal val HANDLE_WIDTH = 25.dp
internal val HANDLE_HEIGHT = 25.dp

/**
 * @suppress
 */
@InternalTextApi // Used by TextField Selection from foundation
@Composable
expect fun SelectionHandle(
    startHandlePosition: Offset?,
    endHandlePosition: Offset?,
    isStartHandle: Boolean,
    directions: Pair<ResolvedTextDirection, ResolvedTextDirection>,
    handlesCrossed: Boolean,
    modifier: Modifier,
    handle: (@Composable () -> Unit)?
)

/**
 * Adjust coordinates for given text offset.
 *
 * Currently [android.text.Layout.getLineBottom] returns y coordinates of the next
 * line's top offset, which is not included in current line's hit area. To be able to
 * hit current line, move up this y coordinates by 1 pixel.
 *
 * @suppress
 */
@InternalTextApi // Used by TextField Selection from foundation
fun getAdjustedCoordinates(position: Offset): Offset {
    return Offset(position.x, position.y - 1f)
}
