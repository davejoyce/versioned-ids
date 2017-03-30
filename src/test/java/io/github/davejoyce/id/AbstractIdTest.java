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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * Abstract superclass of ID type unit test classes.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractIdTest {

    protected static final List<Class<? extends Comparable<?>>> GOOD_ID_TYPES = new ArrayList<>();
    protected static final List<Class<? extends Comparable<?>>> BAD_ID_TYPES = new ArrayList<>();

    // Instants of specific times
    static final Instant AS_OF_TIME = Instant.parse("1977-11-13T14:18:00Z");
    static final Instant AS_AT_TIME = Instant.parse("2008-01-05T22:00:00Z");

    static final int BEFORE = -1;
    static final int EQUAL  = 0;
    static final int AFTER  = 1;

    static {
        GOOD_ID_TYPES.add(String.class);
        BAD_ID_TYPES.add(Integer.class);

        GOOD_ID_TYPES.add(Integer.class);
        BAD_ID_TYPES.add(Date.class);

        GOOD_ID_TYPES.add(Float.class);
        BAD_ID_TYPES.add(Integer.class);
    }

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
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testCompareTo(R id1,
                                                                                  R id2,
                                                                                  int expectedResultFlag) throws Exception {
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
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testAfter(R id1,
                                                                              R id2,
                                                                              int expectedResultFlag) throws Exception {
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
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testBefore(R id1,
                                                                               R id2,
                                                                               int expectedResultFlag) throws Exception {
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

    @Test(dataProvider = "badFromStringData",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public void testFromStringBad(String nsIdString) throws Exception {
        fromString(nsIdString);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProvider = "badFromStringWithTypeData",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public <T extends Comparable<T>> void testFromStringWithTypeBad(String nsIdString,
                                                                    Class<T> idType) throws Exception {
        fromStringWithType(nsIdString, idType);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProvider = "goodFromStringData", groups = "id")
    public <R extends NamespaceId<String>> void testFromStringGood(String nsIdString,
                                                                   R expected) throws Exception {
        R actual = fromString(nsIdString);
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "goodFromStringWithTypeData", groups = "id")
    public <T extends Comparable<T>, R extends NamespaceId<T>> void testFromStringWithTypeGood(String nsIdString,
                                                                                               Class<T> idType,
                                                                                               R expected) throws Exception {
        R actual = fromStringWithType(nsIdString, idType);
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "toStringData", groups = "id")
    public <R extends NamespaceId<?>> void testToString(R nsId, String expected) throws Exception {
        String actual = nsId.toString();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "goodGetterData", groups = "id")
    public <R extends NamespaceId<?>> void testGetNamespace(R nsId) throws Exception {
        assertNotNull(nsId.getNamespace());
    }

    @Test(dataProvider = "goodGetterData", groups = "id")
    public <R extends NamespaceId<?>> void testGetId(R nsId) throws Exception {
        assertNotNull(nsId.getId());
    }

    public abstract void testToNamespaceId() throws Exception;

    protected abstract <R extends NamespaceId<String>> R fromString(String nsIdString);

    protected abstract <T extends Comparable<T>, R extends NamespaceId<T>> R fromStringWithType(String nsIdString, Class<T> idType);

}
