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
 * Unit tests of {@code TemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceIdTest extends AbstractIdTest {

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodTemporalNamespaceIdStringIterator",
          groups = "id")
    public void testFromStringGood(String tnsIdString,
                                   TemporalNamespaceId<String> expected) throws Exception {
        TemporalNamespaceId<String> actual = TemporalNamespaceId.fromString(tnsIdString);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public void testFromStringBad(String tnsIdString) throws Exception {
        TemporalNamespaceId.fromString(tnsIdString);
        fail("Expected exception on parse of: " + tnsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodTemporalNamespaceIdStringIteratorWithType",
          groups = "id")
    public <T extends Comparable<T>> void testFromStringWithTypeGood(String tnsIdString,
                                                                     Class<T> idType,
                                                                     TemporalNamespaceId<T> expected) throws Exception {
        TemporalNamespaceId<T> actual = TemporalNamespaceId.fromString(tnsIdString, idType);
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "badDataIterator",
          groups = "id",
          expectedExceptions = IllegalArgumentException.class)
    public <T extends Comparable<T>> void testFromStringWithTypeBad(String tnsIdString,
                                                                    Class<T> idType) throws Exception {
        TemporalNamespaceId.fromString(tnsIdString, idType);
        fail("Expected exception on parse of: " + tnsIdString);
    }

    @Test(dataProviderClass = TestSupport.class,
          dataProvider = "goodToStringDataIterator",
          groups = "id")
    public void testToString(TemporalNamespaceId<?> tnsId, String expected) throws Exception {
        String actual = tnsId.toString();
        assertEquals(actual, expected);
    }

    @Test
    @Override
    public void testToNamespaceId() throws Exception {
        final Instant asOfTime = Instant.now();
        TemporalNamespaceId<String> tnsId = new TemporalNamespaceId<>("namespace", "id", asOfTime);
        NamespaceId<String> actual = tnsId.toNamespaceId();
        NamespaceId<String> expected = new NamespaceId<>("namespace", "id");
        assertNotSame(actual, tnsId);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] compareToData() {
        final Instant asOfTime = Instant.now();
        final Instant nextTime = asOfTime.plusSeconds(3600L); // add 1 hour
        final TemporalNamespaceId<Integer> tnsId = new TemporalNamespaceId<Integer>("identity", 10, asOfTime);
        return new Object[][] {
                new Object[]{new TemporalNamespaceId<Integer>("apples", 1, asOfTime), new TemporalNamespaceId<Integer>("apples", 1, nextTime), BEFORE},
                new Object[]{new TemporalNamespaceId<Integer>("apples", 1, nextTime), new TemporalNamespaceId<Integer>("apples", 1, asOfTime), AFTER},
                new Object[]{tnsId, tnsId, EQUAL},
        };
    }

    @DataProvider
    public Object[][] equalityData() {
        final Instant asOfTime = Instant.now();
        final TemporalNamespaceId<Integer> tnsId = new TemporalNamespaceId<Integer>("identity", 10, asOfTime);
        return new Object[][] {
                new Object[]{new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), true},
                new Object[]{new TemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime), new TemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime), true},
                new Object[]{new TemporalNamespaceId<String>("namespace", "id", asOfTime), new TemporalNamespaceId<String>("namespace", "ID", asOfTime), false},
                new Object[]{new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), null, false},
                new Object[]{tnsId, tnsId, true},
        };
    }

}