/*
 * Copyright 2020 The Android Open Source Project
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

package androidx.compose.ui.platform

import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillTree
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.node.Owner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * The default animation clock used for animations when an explicit clock isn't provided.
 */
@Deprecated(
    "Renamed to LocalAnimationClock",
    replaceWith = ReplaceWith(
        "LocalAnimationClock",
        "androidx.compose.ui.platform.LocalAnimationClock"
    )
)
val AmbientAnimationClock get() = LocalAnimationClock

/**
 * The default animation clock used for animations when an explicit clock isn't provided.
 */
val LocalAnimationClock = staticCompositionLocalOf<AnimationClockObservable>()

/**
 * The CompositionLocal that can be used to trigger autofill actions.
 * Eg. [Autofill.requestAutofillForNode].
 */
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
@Deprecated(
    "Renamed to LocalAutofill",
    replaceWith = ReplaceWith(
        "LocalAutofill",
        "androidx.compose.ui.platform.LocalAutofill"
    )
)
val AmbientAutofill get() = LocalAutofill

/**
 * The CompositionLocal that can be used to trigger autofill actions.
 * Eg. [Autofill.requestAutofillForNode].
 */
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
val LocalAutofill = staticCompositionLocalOf<Autofill?>()

/**
 * The CompositionLocal that can be used to add
 * [AutofillNode][import androidx.compose.ui.autofill.AutofillNode]s to the autofill tree. The
 * [AutofillTree] is a temporary data structure that will be replaced by Autofill Semantics
 * (b/138604305).
 */
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
@Deprecated(
    "Renamed to LocalAutofillTree",
    replaceWith = ReplaceWith(
        "LocalAutofillTree",
        "androidx.compose.ui.platform.LocalAutofillTree"
    )
)
val AmbientAutofillTree get() = LocalAutofillTree

/**
 * The CompositionLocal that can be used to add
 * [AutofillNode][import androidx.compose.ui.autofill.AutofillNode]s to the autofill tree. The
 * [AutofillTree] is a temporary data structure that will be replaced by Autofill Semantics
 * (b/138604305).
 */
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
val LocalAutofillTree = staticCompositionLocalOf<AutofillTree>()

/**
 * The CompositionLocal to provide communication with platform clipboard service.
 */
@Deprecated(
    "Renamed to LocalClipboardManager",
    replaceWith = ReplaceWith(
        "LocalClipboardManager",
        "androidx.compose.ui.platform.LocalClipboardManager"
    )
)
val AmbientClipboardManager get() = LocalClipboardManager

/**
 * The CompositionLocal to provide communication with platform clipboard service.
 */
val LocalClipboardManager = staticCompositionLocalOf<ClipboardManager>()

/**
 * Provides the [Density] to be used to transform between [density-independent pixel
 * units (DP)][androidx.compose.ui.unit.Dp] and [pixel units][androidx.compose.ui.unit.Px] or
 * [scale-independent pixel units (SP)][androidx.compose.ui.unit.TextUnit] and
 * [pixel units][androidx.compose.ui.unit.Px]. This is typically used when a
 * [DP][androidx.compose.ui.unit.Dp] is provided and it must be converted in the body of [Layout]
 * or [DrawModifier].
 */
@Deprecated(
    "Renamed to LocalDensity",
    replaceWith = ReplaceWith(
        "LocalDensity",
        "androidx.compose.ui.platform.LocalDensity"
    )
)
val AmbientDensity get() = LocalDensity

/**
 * Provides the [Density] to be used to transform between [density-independent pixel
 * units (DP)][androidx.compose.ui.unit.Dp] and [pixel units][androidx.compose.ui.unit.Px] or
 * [scale-independent pixel units (SP)][androidx.compose.ui.unit.TextUnit] and
 * [pixel units][androidx.compose.ui.unit.Px]. This is typically used when a
 * [DP][androidx.compose.ui.unit.Dp] is provided and it must be converted in the body of [Layout]
 * or [DrawModifier].
 */
val LocalDensity = staticCompositionLocalOf<Density>()

/**
 * The CompositionLocal that can be used to control focus within Compose.
 */
@Deprecated(
    "Renamed to LocalFocusManager",
    replaceWith = ReplaceWith(
        "LocalFocusManager",
        "androidx.compose.ui.platform.LocalFocusManager"
    )
)
val AmbientFocusManager get() = LocalFocusManager

/**
 * The CompositionLocal that can be used to control focus within Compose.
 */
val LocalFocusManager = staticCompositionLocalOf<FocusManager>()

/**
 * The CompositionLocal to provide platform font loading methods.
 *
 * Use [androidx.compose.ui.res.fontResource] instead.
 * @suppress
 */
@Deprecated(
    "Renamed to LocalFontLoader",
    replaceWith = ReplaceWith(
        "LocalFontLoader",
        "androidx.compose.ui.platform.LocalFontLoader"
    )
)
val AmbientFontLoader get() = LocalFontLoader

/**
 * The CompositionLocal to provide platform font loading methods.
 *
 * Use [androidx.compose.ui.res.fontResource] instead.
 * @suppress
 */
val LocalFontLoader = staticCompositionLocalOf<Font.ResourceLoader>()

/**
 * The CompositionLocal to provide haptic feedback to the user.
 */
@Deprecated(
    "Renamed to LocalHapticFeedback",
    replaceWith = ReplaceWith(
        "LocalHapticFeedback",
        "androidx.compose.ui.platform.LocalHapticFeedback"
    )
)
val AmbientHapticFeedback get() = LocalHapticFeedback

/**
 * The CompositionLocal to provide haptic feedback to the user.
 */
val LocalHapticFeedback = staticCompositionLocalOf<HapticFeedback>()

/**
 * The CompositionLocal to provide the layout direction.
 */
@Deprecated(
    "Renamed to LocalLayoutDirection",
    replaceWith = ReplaceWith(
        "LocalLayoutDirection",
        "androidx.compose.ui.platform.LocalLayoutDirection"
    )
)
val AmbientLayoutDirection get() = LocalLayoutDirection

/**
 * The CompositionLocal to provide the layout direction.
 */
val LocalLayoutDirection = staticCompositionLocalOf<LayoutDirection>()

/**
 * The CompositionLocal to provide communication with platform text input service.
 */
@Deprecated(
    "Renamed to LocalTextInputService",
    replaceWith = ReplaceWith(
        "LocalTextInputService",
        "androidx.compose.ui.platform.LocalTextInputService"
    )
)
val AmbientTextInputService get() = LocalTextInputService

/**
 * The CompositionLocal to provide communication with platform text input service.
 */
val LocalTextInputService = staticCompositionLocalOf<TextInputService?>()

/**
 * The CompositionLocal to provide text-related toolbar.
 */
@Deprecated(
    "Renamed to LocalTextToolbar",
    replaceWith = ReplaceWith(
        "LocalTextToolbar",
        "androidx.compose.ui.platform.LocalTextToolbar"
    )
)
val AmbientTextToolbar get() = LocalTextToolbar

/**
 * The CompositionLocal to provide text-related toolbar.
 */
val LocalTextToolbar = staticCompositionLocalOf<TextToolbar>()

/**
 * The CompositionLocal to provide functionality related to URL, e.g. open URI.
 */
@Deprecated(
    "Renamed to LocalUriHandler",
    replaceWith = ReplaceWith(
        "LocalUriHandler",
        "androidx.compose.ui.platform.LocalUriHandler"
    )
)
val AmbientUriHandler get() = LocalUriHandler

/**
 * The CompositionLocal to provide functionality related to URL, e.g. open URI.
 */
val LocalUriHandler = staticCompositionLocalOf<UriHandler>()

/**
 * The CompositionLocal that provides the ViewConfiguration.
 */
@Deprecated(
    "Renamed to LocalViewConfiguration",
    replaceWith = ReplaceWith(
        "LocalViewConfiguration",
        "androidx.compose.ui.platform.LocalViewConfiguration"
    )
)
val AmbientViewConfiguration get() = LocalViewConfiguration

/**
 * The CompositionLocal that provides the ViewConfiguration.
 */
val LocalViewConfiguration = staticCompositionLocalOf<ViewConfiguration>()

/**
 * The CompositionLocal that provides information about the window that hosts the current [Owner].
 */
@Deprecated(
    "Renamed to LocalWindowInfo",
    replaceWith = ReplaceWith(
        "LocalWindowInfo",
        "androidx.compose.ui.platform.LocalWindowInfo"
    )
)
val AmbientWindowInfo get() = LocalWindowInfo

/**
 * The CompositionLocal that provides information about the window that hosts the current [Owner].
 */
val LocalWindowInfo = staticCompositionLocalOf<WindowInfo>()

@ExperimentalComposeUiApi
@Composable
internal fun ProvideCommonCompositionLocals(
    owner: Owner,
    animationClock: AnimationClockObservable,
    uriHandler: UriHandler,
    content: @Composable () -> Unit
) {
    Providers(
        LocalAnimationClock provides animationClock,
        LocalAutofill provides owner.autofill,
        LocalAutofillTree provides owner.autofillTree,
        LocalClipboardManager provides owner.clipboardManager,
        LocalDensity provides owner.density,
        LocalFocusManager provides owner.focusManager,
        LocalFontLoader provides owner.fontLoader,
        LocalHapticFeedback provides owner.hapticFeedBack,
        LocalLayoutDirection provides owner.layoutDirection,
        LocalTextInputService provides owner.textInputService,
        LocalTextToolbar provides owner.textToolbar,
        LocalUriHandler provides uriHandler,
        LocalViewConfiguration provides owner.viewConfiguration,
        LocalWindowInfo provides owner.windowInfo,
        content = content
    )
}