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

/**
 * Unit tests of {@code NamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceIdTest {

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodDataIterator")
    public void testFromStringGood(String nsIdString,
                                   NamespaceId<String> expected) throws Exception {
        NamespaceId<String> actual = NamespaceId.fromString(nsIdString);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          expectedExceptions = IllegalArgumentException.class)
    public void testFromStringBad(String nsIdString) throws Exception {
        NamespaceId.fromString(nsIdString);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodDataIterator")
    public <T extends Comparable<T>> void testFromStringWithTypeGood(String nsIdString,
                                                                     Class<T> idType,
                                                                     NamespaceId<T> expected) throws Exception {
        NamespaceId<T> actual = NamespaceId.fromString(nsIdString, idType);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          expectedExceptions = IllegalArgumentException.class)
    public <T extends Comparable<T>> void testFromStringWithTypeBad(String nsIdString,
                                                                    Class<T> idType) throws Exception {
        NamespaceId.fromString(nsIdString, idType);
        fail("Expected exception on parse of: " + nsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodGetterDataIterator")
    public void testGetNamespace(NamespaceId<?> nsId) throws Exception {
        assertNotNull(nsId.getNamespace());
    }

    @Test(dataProviderClass = TestSupport.class,
            dataProvider = "goodGetterDataIterator")
    public void testGetId(NamespaceId<?> nsId) throws Exception {
        assertNotNull(nsId.getId());
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray")
    public <T extends Comparable<T>> void testEquals(NamespaceId<T> nsId1,
                                                     NamespaceId<T> nsId2,
                                                     boolean expectedEqual) throws Exception {
        boolean actual1 = nsId1.equals(nsId2);
        boolean actual2 = (null != nsId2) && nsId2.equals(nsId1);
        assertEquals(actual1, actual2);
        assertEquals(actual1, expectedEqual);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray")
    public void testHashCode(NamespaceId<?> nsId1, NamespaceId<?> nsId2, boolean expectedEqual) throws Exception {
        int actual1 = nsId1.hashCode();
        int actual2 = (null != nsId2) ? nsId2.hashCode() : -1;
        if (expectedEqual) {
            assertEquals(actual1, actual2);
        } else {
            assertNotEquals(actual1, actual2);
        }
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "compareToDataArray")
    public <T extends Comparable<T>> void testCompareTo(NamespaceId<T> nsId1,
                                                        NamespaceId<T> nsId2,
                                                        int expectedResultFlag) throws Exception {
        int actual = nsId1.compareTo(nsId2);
        switch (expectedResultFlag) {
            case TestSupport.BEFORE:
                assertTrue(0 > actual);
                break;
            case TestSupport.EQUAL:
                assertTrue(0 == actual);
                break;
            case TestSupport.AFTER:
                assertTrue(0 < actual);
                break;
            default:
                break;
        }
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodToStringDataIterator")
    public void testToString(NamespaceId<?> nsId, String expected) throws Exception {
        String actual = nsId.toString();
        assertEquals(actual, expected);
    }

}