/*
 * Copyright (C) 2015 The Android Open Source Project
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

package androidx.appcompat.app;

import androidx.test.annotation.UiThreadTest;
import androidx.test.filters.LargeTest;

import org.junit.Test;

@LargeTest
public class BasicsTestCaseWithToolbar extends BaseBasicsTestCase<ToolbarAppCompatActivity> {
    public BasicsTestCaseWithToolbar() {
        super(ToolbarAppCompatActivity.class);
    }

    @Test
    @UiThreadTest
    public void testSupportActionModeAppCompatCallbacks() {
        // Since we're using Toolbar, any action modes will be created from the window
        testSupportActionModeAppCompatCallbacks(true);
    }
}
