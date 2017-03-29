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

package io.github.davejoyce.id;

import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * Abstract superclass of ID type unit test classes.
 */
public abstract class AbstractIdTest {

    static final int BEFORE = -1;
    static final int EQUAL  = 0;
    static final int AFTER  = 1;

    @Test(dataProvider = "equalityData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testEquals(R nsId1,
                                                                               R nsId2,
                                                                               boolean expectedEqual) throws Exception {
        boolean actual1 = nsId1.equals(nsId2);
        boolean actual2 = (null != nsId2) && nsId2.equals(nsId1);
        assertEquals(actual1, actual2);
        assertEquals(actual1, expectedEqual);
    }

    @Test(dataProvider = "equalityData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testHashCode(R nsId1,
                                                                                 R nsId2,
                                                                                 boolean expectedEqual) throws Exception {
        int actual1 = nsId1.hashCode();
        int actual2 = (null != nsId2) ? nsId2.hashCode() : -1;
        if (expectedEqual) {
            assertEquals(actual1, actual2);
        } else {
            assertNotEquals(actual1, actual2);
        }
    }

    @Test(dataProvider = "compareToData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testCompareTo(R id1, R id2, int expectedResultFlag) throws Exception {
        int actual = id1.compareTo(id2);
        switch (expectedResultFlag) {
            case BEFORE:
                assertTrue(0 > actual);
                break;
            case EQUAL:
                assertTrue(0 == actual);
                break;
            case AFTER:
                assertTrue(0 < actual);
                break;
            default:
                break;
        }
    }

    @Test(dataProvider = "compareToData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testAfter(R id1, R id2, int expectedResultFlag) throws Exception {
        boolean after = id1.after(id2);
        switch (expectedResultFlag) {
            case BEFORE:
            case EQUAL:
                assertFalse(after);
                break;
            case AFTER:
                assertTrue(after);
                break;
            default:
                fail("Unsupported result flag: " + expectedResultFlag);
                break;
        }
    }

    @Test(dataProvider = "compareToData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testBefore(R id1, R id2, int expectedResultFlag) throws Exception {
        boolean before = id1.before(id2);
        switch (expectedResultFlag) {
            case BEFORE:
                assertTrue(before);
                break;
            case AFTER:
            case EQUAL:
                assertFalse(before);
                break;
            default:
                fail("Unsupported result flag: " + expectedResultFlag);
                break;
        }
    }

}
