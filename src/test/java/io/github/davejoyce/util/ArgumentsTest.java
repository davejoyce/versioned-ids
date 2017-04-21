/*
 *  Copyright 2017 David Joyce
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.davejoyce.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Unit tests of {@code Arguments}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ArgumentsTest {

    @Test(groups = "util")
    public void testRequireNonNull() throws Exception {
        Integer i = Arguments.requireNonNull(1);
        try {
            i = Arguments.requireNonNull(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), Arguments.ERROR_NULL_ARG);
        }
    }

    @Test(groups = "util")
    public void testRequireNonNullWithMessage() throws Exception {
        String expectedMsg = "Integer argument cannot be null";
        Integer i = Arguments.requireNonNull(1, expectedMsg);
        try {
            i = Arguments.requireNonNull(null, expectedMsg);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), expectedMsg);
        }
    }

    @Test(groups = "util")
    public void testRequireNonEmpty() throws Exception {
        String s = Arguments.requireNonEmpty("test");
        try {
            s = Arguments.requireNonEmpty("\t");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), Arguments.ERROR_EMPTY_STRING);
        }
    }

    @Test(groups = "util")
    public void testRequireNonEmptyWithMessage() throws Exception {
        String expectedMsg = "Missing text argument";
        String s = Arguments.requireNonEmpty("test", expectedMsg);
        try {
            s = Arguments.requireNonEmpty("\t", expectedMsg);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), expectedMsg);
        }
    }

}