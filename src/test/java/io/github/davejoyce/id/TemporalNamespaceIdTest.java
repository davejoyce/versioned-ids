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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.testng.Assert.*;

/**
 * Unit tests of {@code TemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceIdTest extends AbstractIdTest {

    private static final List<String> GOOD_TNSID_STRINGS = new ArrayList<>();
    private static final List<String> BAD_TNSID_STRINGS = new ArrayList<>();
    private static final List<TemporalNamespaceId<String>> GOOD_TNSID_STRING_OBJS = new ArrayList<>();
    private static final List<TemporalNamespaceId<?>> GOOD_TNSID_TYPED_OBJS = new ArrayList<>();

    static {
        GOOD_TNSID_STRINGS.add("temporal/id/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "id", AS_OF_TIME));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<String>("temporal", "id", AS_OF_TIME));
        GOOD_TNSID_STRINGS.add("temporal/2/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "2", AS_OF_TIME));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<Integer>("temporal", 2, AS_OF_TIME));
        GOOD_TNSID_STRINGS.add("temporal/3.141592/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "3.141592", AS_OF_TIME));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<Float>("temporal", 3.141592F, AS_OF_TIME));
        BAD_TNSID_STRINGS.add(null);
        BAD_TNSID_STRINGS.add("");
        BAD_TNSID_STRINGS.add("temporal/2");
        BAD_TNSID_STRINGS.add("temporal/2/1977-11-13");
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

    @DataProvider
    public Iterator<Object[]> toStringData() {
        int count = GOOD_TNSID_STRING_OBJS.size();
        return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRING_OBJS.get(i), GOOD_TNSID_STRINGS.get(i) }).iterator();
    }

    @DataProvider
    public Iterator<Object[]> badFromStringData() {
        return fromStringStreamIterator(false, false);
    }

    @DataProvider
    public Iterator<Object[]> badFromStringWithTypeData() {
        return fromStringStreamIterator(false, true);
    }

    @DataProvider
    public Iterator<Object[]> goodFromStringData() {
        return fromStringStreamIterator(true, false);
    }

    @DataProvider
    public Iterator<Object[]> goodFromStringWithTypeData() {
        return fromStringStreamIterator(true, true);
    }

    @DataProvider
    public Iterator<Object[]> goodGetterData() {
        return GOOD_TNSID_TYPED_OBJS.stream().map(tnsId -> new Object[]{ tnsId }).iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends Comparable<T>, R extends NamespaceId<T>> R fromStringWithType(String nsIdString, Class<T> idType) {
        return (R)TemporalNamespaceId.fromString(nsIdString, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <R extends NamespaceId<String>> R fromString(String nsIdString) {
        return (R)TemporalNamespaceId.fromString(nsIdString);
    }

    private Iterator<Object[]> fromStringStreamIterator(boolean goodData, boolean withType) {
        if (goodData) {
            int count = GOOD_TNSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_TNSID_TYPED_OBJS.get(i) }).iterator()
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), GOOD_TNSID_STRING_OBJS.get(i) }).iterator();
        } else {
            return (withType)
                    ? IntStream.range(0, GOOD_TNSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), BAD_ID_TYPES.get(i) }).iterator()
                    : BAD_TNSID_STRINGS.stream().map(bad_tnsid_string -> new Object[]{bad_tnsid_string}).iterator();
        }
    }

}