/*
 * Copyright 2019 The Android Open Source Project
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

package com.example.androidx.webkit;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.webkit.WebViewFeature;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration test for {@link ProxyOverrideActivity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProxyOverrideTestAppTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        WebkitTestHelpers.assumeFeature(WebViewFeature.PROXY_OVERRIDE);
    }

    @Test
    public void testProxyOverride() {
        WebkitTestHelpers.clickMenuListItemWithString(R.string.proxy_override_activity_title);
        WebkitTestHelpers.assertViewHasText(R.id.proxy_override_textview,
                R.string.proxy_override_requests_served, 1);
    }
}
