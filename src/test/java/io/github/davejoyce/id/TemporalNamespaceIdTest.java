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
 * Unit tests of {@code TemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceIdTest {

    @Test
    public void testFromString() throws Exception {
    }

    @Test
    public void testFromString1() throws Exception {
    }

    @Test
    public void testGetAsOfTime() throws Exception {
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray",
          groups = "id")
    public <T extends Comparable<T>> void testEquals(TemporalNamespaceId<T> tnsId1,
                                                     TemporalNamespaceId<T> tnsId2,
                                                     boolean expectedEqual) throws Exception {
        boolean actual = tnsId1.equals(tnsId2);
        assertEquals(actual, expectedEqual);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "equalityDataArray",
          groups = "id")
    public <T extends Comparable<T>> void testHashCode(TemporalNamespaceId<T> tnsId1,
                                                       TemporalNamespaceId<T> tnsId2,
                                                       boolean expectedEqual) throws Exception {
        int actual1 = tnsId1.hashCode();
        int actual2 = (null != tnsId2) ? tnsId2.hashCode() : -1;
        if (expectedEqual) {
            assertEquals(actual1, actual2);
        } else {
            assertNotEquals(actual1, actual2);
        }
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "compareToDataArray",
          groups = "id")
    public <T extends Comparable<T>> void testCompareTo(TemporalNamespaceId<T> tnsId1,
                                                        TemporalNamespaceId<T> tnsId2,
                                                        int expectedResultFlag) throws Exception {
        int actual = tnsId1.compareTo(tnsId2);
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

    @Test
    public void testToString() throws Exception {
    }

}