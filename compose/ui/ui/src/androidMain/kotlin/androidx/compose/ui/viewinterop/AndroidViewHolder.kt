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

package androidx.compose.ui.viewinterop

import android.content.Context
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.platform.AndroidComposeView
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import kotlin.math.roundToInt

/**
 * A base class used to host a [View] inside Compose.
 * This API is not designed to be used directly, but rather using the [AndroidView] and
 * `AndroidViewBinding` APIs, which are built on top of [AndroidViewHolder].
 */
// Opt in snapshot observing APIs.
@OptIn(ExperimentalComposeApi::class)
internal abstract class AndroidViewHolder(
    context: Context,
    parentContext: CompositionContext?
) : ViewGroup(context) {
    init {
        clipChildren = false

        // Any [Abstract]ComposeViews that are descendants of this view will host
        // subcompositions of the host composition.
        // UiApplier doesn't supply this, only AndroidView.
        parentContext?.let {
            compositionContext = it
        }
    }

    /**
     * The view hosted by this holder.
     */
    var view: View? = null
        internal set(value) {
            if (value !== field) {
                field = value
                removeAllViews()
                if (value != null) {
                    addView(value)
                    runUpdate()
                }
            }
        }

    /**
     * The update logic of the [View].
     */
    var update: () -> Unit = {}
        protected set(value) {
            field = value
            hasUpdateBlock = true
            runUpdate()
        }
    private var hasUpdateBlock = false

    /**
     * The modifier of the `LayoutNode` corresponding to this [View].
     */
    var modifier: Modifier = Modifier
        set(value) {
            if (value !== field) {
                field = value
                onModifierChanged?.invoke(value)
            }
        }

    internal var onModifierChanged: ((Modifier) -> Unit)? = null

    /**
     * The screen density of the layout.
     */
    var density: Density = Density(1f)
        set(value) {
            if (value !== field) {
                field = value
                onDensityChanged?.invoke(value)
            }
        }

    internal var onDensityChanged: ((Density) -> Unit)? = null

    @OptIn(ExperimentalComposeApi::class)
    private val snapshotObserver = SnapshotStateObserver { command ->
        if (handler.looper === Looper.myLooper()) {
            command()
        } else {
            handler.post(command)
        }
    }

    private val onCommitAffectingUpdate: (AndroidViewHolder) -> Unit = {
        handler.post(runUpdate)
    }

    @OptIn(ExperimentalComposeApi::class)
    private val runUpdate: () -> Unit = {
        if (hasUpdateBlock) {
            snapshotObserver.observeReads(this, onCommitAffectingUpdate, update)
        }
    }

    internal var onRequestDisallowInterceptTouchEvent: ((Boolean) -> Unit)? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        view?.measure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(view?.measuredWidth ?: 0, view?.measuredHeight ?: 0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        view?.layout(0, 0, r - l, b - t)
    }

    override fun getLayoutParams(): LayoutParams? {
        return view?.layoutParams
            ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        onRequestDisallowInterceptTouchEvent?.invoke(disallowIntercept)
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        snapshotObserver.enableStateUpdatesObserving(true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        snapshotObserver.enableStateUpdatesObserving(false)
        // remove all observations:
        snapshotObserver.clear()
    }

    /**
     * Builds a [LayoutNode] tree representation for this Android [View] holder.
     * The [LayoutNode] will proxy the Compose core calls to the [View].
     */
    fun toLayoutNode(): LayoutNode {
        // TODO(soboleva): add layout direction here?
        // TODO(popam): forward pointer input, accessibility, focus
        // Prepare layout node that proxies measure and layout passes to the View.
        val layoutNode = LayoutNode()

        val coreModifier = Modifier
            .pointerInteropFilter(this)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    (layoutNode.owner as? AndroidComposeView)
                        ?.drawAndroidView(this@AndroidViewHolder, canvas.nativeCanvas)
                }
            }.onGloballyPositioned {
                // The global position of this LayoutNode can change with it being replaced. For
                // these cases, we need to inform the View.
                layoutAccordingTo(layoutNode)
            }
        layoutNode.modifier = modifier.then(coreModifier)
        onModifierChanged = { layoutNode.modifier = it.then(coreModifier) }

        layoutNode.density = density
        onDensityChanged = { layoutNode.density = it }

        var viewRemovedOnDetach: View? = null
        layoutNode.onAttach = { owner ->
            (owner as? AndroidComposeView)?.addAndroidView(this, layoutNode)
            if (viewRemovedOnDetach != null) view = viewRemovedOnDetach
        }
        layoutNode.onDetach = { owner ->
            (owner as? AndroidComposeView)?.removeAndroidView(this)
            viewRemovedOnDetach = view
            view = null
        }

        layoutNode.measureBlocks = object : LayoutNode.NoIntrinsicsMeasureBlocks(
            "Intrinsics not supported for Android views"
        ) {
            override fun measure(
                measureScope: MeasureScope,
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                if (constraints.minWidth != 0) {
                    getChildAt(0).minimumWidth = constraints.minWidth
                }
                if (constraints.minHeight != 0) {
                    getChildAt(0).minimumHeight = constraints.minHeight
                }
                // TODO (soboleva): native view should get LD value from Compose?

                // TODO(shepshapard): !! necessary?
                measure(
                    obtainMeasureSpec(
                        constraints.minWidth,
                        constraints.maxWidth,
                        layoutParams!!.width
                    ),
                    obtainMeasureSpec(
                        constraints.minHeight,
                        constraints.maxHeight,
                        layoutParams!!.height
                    )
                )
                return measureScope.layout(measuredWidth, measuredHeight) {
                    layoutAccordingTo(layoutNode)
                }
            }
        }
        return layoutNode
    }

    /**
     * Intersects [Constraints] and [View] LayoutParams to obtain the suitable [View.MeasureSpec]
     * for measuring the [View].
     */
    private fun obtainMeasureSpec(
        min: Int,
        max: Int,
        preferred: Int
    ): Int = when {
        preferred >= 0 || min == max -> {
            // Fixed size due to fixed size layout param or fixed constraints.
            MeasureSpec.makeMeasureSpec(preferred.coerceIn(min, max), MeasureSpec.EXACTLY)
        }
        preferred == LayoutParams.WRAP_CONTENT && max != Constraints.Infinity -> {
            // Wrap content layout param with finite max constraint. If max constraint is infinite,
            // we will measure the child with UNSPECIFIED.
            MeasureSpec.makeMeasureSpec(max, MeasureSpec.AT_MOST)
        }
        preferred == LayoutParams.MATCH_PARENT && max != Constraints.Infinity -> {
            // Match parent layout param, so we force the child to fill the available space.
            MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
        }
        else -> {
            // max constraint is infinite and layout param is WRAP_CONTENT or MATCH_PARENT.
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        }
    }
}

private fun View.layoutAccordingTo(layoutNode: LayoutNode) {
    val position = layoutNode.coordinates.positionInRoot()
    val x = position.x.roundToInt()
    val y = position.y.roundToInt()
    layout(x, y, x + measuredWidth, y + measuredHeight)
}