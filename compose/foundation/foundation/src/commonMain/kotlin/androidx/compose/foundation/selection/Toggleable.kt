/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.compose.foundation.selection

import androidx.compose.foundation.Indication
import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.Strings
import androidx.compose.foundation.indication
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.state.ToggleableState.Indeterminate
import androidx.compose.ui.state.ToggleableState.Off
import androidx.compose.ui.state.ToggleableState.On

/**
 * Configure component to make it toggleable via input and accessibility events
 *
 * This version has no [InteractionState] or [Indication] parameters, default indication from
 * [LocalIndication] will be used. To specify [InteractionState] or [Indication], use another
 * overload.
 *
 * @sample androidx.compose.foundation.samples.ToggleableSample
 *
 * @see [Modifier.triStateToggleable] if you require support for an indeterminate state.
 *
 * @param value whether Toggleable is on or off
 * @param enabled whether or not this [toggleable] will handle input events and appear
 * enabled for semantics purposes
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param onValueChange callback to be invoked when toggleable is clicked,
 * therefore the change of the state in requested.
 */
fun Modifier.toggleable(
    value: Boolean,
    enabled: Boolean = true,
    role: Role? = null,
    onValueChange: (Boolean) -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "toggleable"
        properties["value"] = value
        properties["enabled"] = enabled
        properties["role"] = role
        properties["onValueChange"] = onValueChange
    }
) {
    toggleableImpl(
        state = ToggleableState(value),
        onClick = { onValueChange(!value) },
        enabled = enabled,
        role = role,
        interactionState = remember { InteractionState() },
        indication = LocalIndication.current
    )
}

/**
 * Configure component to make it toggleable via input and accessibility events.
 *
 * This version requires both [InteractionState] and [Indication] to work properly. Use another
 * overload if you don't need these parameters.
 *
 * @sample androidx.compose.foundation.samples.ToggleableSample
 *
 * @see [Modifier.triStateToggleable] if you require support for an indeterminate state.
 *
 * @param value whether Toggleable is on or off
 * @param interactionState [InteractionState] that will be updated when this toggleable is
 * pressed, using [Interaction.Pressed]
 * @param indication indication to be shown when modified element is pressed. Be default,
 * indication from [LocalIndication] will be used. Pass `null` to show no indication, or
 * current value from [LocalIndication] to show theme default
 * @param enabled whether or not this [toggleable] will handle input events and appear
 * enabled for semantics purposes
 * * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param onValueChange callback to be invoked when toggleable is clicked,
 * therefore the change of the state in requested.
 */
fun Modifier.toggleable(
    value: Boolean,
    interactionState: InteractionState,
    indication: Indication?,
    enabled: Boolean = true,
    role: Role? = null,
    onValueChange: (Boolean) -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "toggleable"
        properties["value"] = value
        properties["enabled"] = enabled
        properties["role"] = role
        properties["interactionState"] = interactionState
        properties["indication"] = indication
        properties["onValueChange"] = onValueChange
    },
    factory = {
        toggleableImpl(
            state = ToggleableState(value),
            onClick = { onValueChange(!value) },
            enabled = enabled,
            role = role,
            interactionState = interactionState,
            indication = indication
        )
    }
)

/**
 * Configure component to make it toggleable via input and accessibility events with three
 * states: On, Off and Indeterminate.
 *
 * TriStateToggleable should be used when there are dependent Toggleables associated to this
 * component and those can have different values.
 *
 * This version has no [InteractionState] or [Indication] parameters, default indication from
 * [LocalIndication] will be used. To specify [InteractionState] or [Indication], use another
 * overload.
 *
 * @sample androidx.compose.foundation.samples.TriStateToggleableSample
 *
 * @see [Modifier.toggleable] if you want to support only two states: on and off
 *
 * @param state current value for the component
 * @param enabled whether or not this [triStateToggleable] will handle input events and
 * appear enabled for semantics purposes
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param onClick will be called when user clicks the toggleable.
 */
fun Modifier.triStateToggleable(
    state: ToggleableState,
    enabled: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "triStateToggleable"
        properties["state"] = state
        properties["enabled"] = enabled
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    toggleableImpl(
        state,
        enabled,
        role,
        remember { InteractionState() },
        LocalIndication.current,
        onClick
    )
}

/**
 * Configure component to make it toggleable via input and accessibility events with three
 * states: On, Off and Indeterminate.
 *
 * TriStateToggleable should be used when there are dependent Toggleables associated to this
 * component and those can have different values.
 *
 * This version requires both [InteractionState] and [Indication] to work properly. Use another
 * overload if you don't need these parameters.
 *
 * @sample androidx.compose.foundation.samples.TriStateToggleableSample
 *
 * @see [Modifier.toggleable] if you want to support only two states: on and off
 *
 * @param state current value for the component
 * @param interactionState [InteractionState] that will be updated when this toggleable is
 * pressed, using [Interaction.Pressed]
 * @param indication indication to be shown when modified element is pressed. Be default,
 * indication from [LocalIndication] will be used. Pass `null` to show no indication, or
 * current value from [LocalIndication] to show theme default
 * @param enabled whether or not this [triStateToggleable] will handle input events and
 * appear enabled for semantics purposes
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param onClick will be called when user clicks the toggleable.
 */
fun Modifier.triStateToggleable(
    state: ToggleableState,
    interactionState: InteractionState,
    indication: Indication?,
    enabled: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "triStateToggleable"
        properties["state"] = state
        properties["enabled"] = enabled
        properties["role"] = role
        properties["interactionState"] = interactionState
        properties["indication"] = indication
        properties["onClick"] = onClick
    },
    factory = {
        toggleableImpl(state, enabled, role, interactionState, indication, onClick)
    }
)

@Suppress("ModifierInspectorInfo", "DEPRECATION")
private fun Modifier.toggleableImpl(
    state: ToggleableState,
    enabled: Boolean,
    role: Role? = null,
    interactionState: InteractionState,
    indication: Indication?,
    onClick: () -> Unit
): Modifier = composed {
    // TODO(pavlis): Handle multiple states for Semantics
    val semantics = Modifier.semantics(mergeDescendants = true) {
        if (role != null) {
            this.role = role
        }
        this.stateDescription = when (state) {
            On -> if (role == Role.Switch) Strings.On else Strings.Checked
            Off -> if (role == Role.Switch) Strings.Off else Strings.Unchecked
            Indeterminate -> Strings.Indeterminate
        }
        this.toggleableState = state

        onClick(action = { onClick(); return@onClick true }, label = Strings.Toggle)
        if (!enabled) {
            disabled()
        }
    }
    val interactionUpdate =
        if (enabled) {
            Modifier.pressIndicatorGestureFilter(
                onStart = { interactionState.addInteraction(Interaction.Pressed, it) },
                onStop = { interactionState.removeInteraction(Interaction.Pressed) },
                onCancel = { interactionState.removeInteraction(Interaction.Pressed) }
            )
        } else {
            Modifier
        }
    val click = if (enabled) Modifier.tapGestureFilter { onClick() } else Modifier

    DisposableEffect(interactionState) {
        onDispose {
            interactionState.removeInteraction(Interaction.Pressed)
        }
    }
    this
        .then(semantics)
        .indication(interactionState, indication)
        .then(interactionUpdate)
        .then(click)
}