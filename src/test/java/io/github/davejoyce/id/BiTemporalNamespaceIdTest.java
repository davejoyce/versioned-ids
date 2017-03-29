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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;

import static org.testng.Assert.*;

/**
 * Unit tests of {@code BiTemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BiTemporalNamespaceIdTest extends AbstractIdTest {

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodBiTemporalNamespaceIdStringIterator",
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
          dataProvider = "goodBiTemporalNamespaceIdStringIteratorWithType",
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
          dataProvider = "goodToStringDataIterator",
          groups = "id")
    public void testToString(BiTemporalNamespaceId<?> btnsId, String expected) throws Exception {
        String actual = btnsId.toString();
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] compareToData() {
        final Instant asOfTime = Instant.now();
        final Instant asAtTime = asOfTime.plusSeconds(3600L); // add 1 hour
        final Instant nextTime = asAtTime.plusSeconds(3600L); // add 1 hour
        final BiTemporalNamespaceId<Integer> btnsId = new BiTemporalNamespaceId<Integer>("identity", 10, asOfTime, asAtTime);
        return new Object[][] {
                new Object[]{new BiTemporalNamespaceId<Integer>("apples", 1, asOfTime, asAtTime), new BiTemporalNamespaceId<Integer>("apples", 1, asOfTime, nextTime), BEFORE},
                new Object[]{new BiTemporalNamespaceId<Integer>("apples", 1, asOfTime, nextTime), new BiTemporalNamespaceId<Integer>("apples", 1, asOfTime, asAtTime), AFTER},
                new Object[]{btnsId, btnsId, EQUAL},
        };
    }

    @DataProvider
    public Object[][] equalityData() {
        final Instant asOfTime = Instant.now();
        final Instant asAtTime = asOfTime.plusSeconds(3600L); // add 1 hour
        final BiTemporalNamespaceId<Integer> btnsId = new BiTemporalNamespaceId<Integer>("identity", 10, asOfTime, asAtTime);
        return new Object[][] {
                new Object[]{new BiTemporalNamespaceId<Integer>("namespace", 1, asOfTime, asAtTime), new BiTemporalNamespaceId<Integer>("namespace", 1, asOfTime, asAtTime), true},
                new Object[]{new BiTemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime, asAtTime), new BiTemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime, asAtTime), true},
                new Object[]{new BiTemporalNamespaceId<String>("namespace", "id", asOfTime, asAtTime), new BiTemporalNamespaceId<String>("namespace", "ID", asOfTime, asAtTime), false},
                new Object[]{new BiTemporalNamespaceId<Integer>("namespace", 1, asOfTime, asAtTime), null, false},
                new Object[]{btnsId, btnsId, true},
        };
    }

}