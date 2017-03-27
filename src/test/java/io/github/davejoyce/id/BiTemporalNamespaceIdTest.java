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
 * Unit tests of {@code BiTemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BiTemporalNamespaceIdTest {

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodDataIterator",
          groups = "id")
    public void testFromStringGood(String btnsIdString,
                                   BiTemporalNamespaceId<String> expected) throws Exception {
        BiTemporalNamespaceId<String> actual = BiTemporalNamespaceId.fromString(btnsIdString);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public void testFromStringBad(String btnsIdString) throws Exception {
        BiTemporalNamespaceId<String> actual = BiTemporalNamespaceId.fromString(btnsIdString);
        fail("Expected exception on parse of: " + btnsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodDataIterator",
          groups = "id")
    public <T extends Comparable<T>> void testFromStringWithTypeGood(String btnsIdString,
                                                                     Class<T> idType,
                                                                     BiTemporalNamespaceId<T> expected) throws Exception {
        BiTemporalNamespaceId<T> actual = BiTemporalNamespaceId.fromString(btnsIdString, idType);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public <T extends Comparable<T>> void testFromStringWithTypeBad(String btnsIdString,
                                                                    Class<T> idType) throws Exception {
        BiTemporalNamespaceId.fromString(btnsIdString, idType);
        fail("Expected exception on parse of: " + btnsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray",
          groups = "id")
    public <T extends Comparable<T>> void testEquals(BiTemporalNamespaceId<T> btnsId1,
                                                     BiTemporalNamespaceId<T> btnsId2,
                                                     boolean expectedEqual) throws Exception {
        boolean actual1 = btnsId1.equals(btnsId2);
        boolean actual2 = (null != btnsId2) && btnsId2.equals(btnsId1);
        assertEquals(actual1, actual2);
        assertEquals(actual1, expectedEqual);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray",
          groups = "id")
    public void testHashCode(BiTemporalNamespaceId<?> btnsId1,
                             BiTemporalNamespaceId<?> btnsId2,
                             boolean expectedEqual) throws Exception {
        int actual1 = btnsId1.hashCode();
        int actual2 = (null != btnsId2) ? btnsId2.hashCode() : -1;
        if (expectedEqual) {
            assertEquals(actual1, actual2);
        } else {
            assertNotEquals(actual1, actual2);
        }
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "compareToDataArray",
          groups = "id")
    public <T extends Comparable<T>> void testCompareTo(BiTemporalNamespaceId<T> btnsId1,
                                                        BiTemporalNamespaceId<T> btnsId2,
                                                        int expectedResultFlag) throws Exception {
        int actual = btnsId1.compareTo(btnsId2);
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
          dataProvider = "goodToStringDataIterator",
          groups = "id")
    public void testToString(BiTemporalNamespaceId<?> btnsId, String expected) throws Exception {
        String actual = btnsId.toString();
        assertEquals(actual, expected);
    }

}