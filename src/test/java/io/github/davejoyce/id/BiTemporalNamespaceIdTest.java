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
 * Unit tests of {@code BiTemporalNamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BiTemporalNamespaceIdTest extends AbstractIdTest {

    private static final List<String> GOOD_BTNSID_STRINGS = new ArrayList<>();
    private static final List<String> BAD_BTNSID_STRINGS = new ArrayList<>();
    private static final List<BiTemporalNamespaceId<String>> GOOD_BTNSID_STRING_OBJS = new ArrayList<>();
    private static final List<BiTemporalNamespaceId<?>> GOOD_BTNSID_TYPED_OBJS = new ArrayList<>();

    static {
        GOOD_BTNSID_STRINGS.add("bitemporal/id/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "id", AS_OF_TIME, AS_AT_TIME));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<String>("bitemporal", "id", AS_OF_TIME, AS_AT_TIME));
        GOOD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "2", AS_OF_TIME, AS_AT_TIME));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<Integer>("bitemporal", 2, AS_OF_TIME, AS_AT_TIME));
        GOOD_BTNSID_STRINGS.add("bitemporal/3.141592/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "3.141592", AS_OF_TIME, AS_AT_TIME));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<Float>("bitemporal", 3.141592F, AS_OF_TIME, AS_AT_TIME));
        BAD_BTNSID_STRINGS.add(null);
        BAD_BTNSID_STRINGS.add("");
        BAD_BTNSID_STRINGS.add("bitemporal/2");
        BAD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z");
        BAD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z/2008-01-05");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromStringNoSeparator() throws Exception {
        String badTnsIdString = "bitemporal.id.1977-11-13T14:18:00Z.2008-01-05T22:00:00Z";
        BiTemporalNamespaceId<String> tnsId = BiTemporalNamespaceId.fromString(badTnsIdString);
        fail("Expected IllegalArgumentException on 0 separators");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromStringTwoSeparators() throws Exception {
        String badTnsIdString = "bitemporal/id/1977-11-13T14:18:00Z";
        BiTemporalNamespaceId<String> tnsId = BiTemporalNamespaceId.fromString(badTnsIdString);
        fail("Expected IllegalArgumentException on less than 3 separators");
    }

    @Test
    public void testConstructorEpochSeconds() {
        Instant timestamp1 = Instant.parse("1977-11-13T14:18:00Z");
        Instant timestamp2 = Instant.parse("2008-01-05T22:00:00Z");
        BiTemporalNamespaceId<Integer> tnsId = new BiTemporalNamespaceId<Integer>("bitemporal",
                1,
                timestamp1.getEpochSecond(),
                timestamp2.getEpochSecond());
        Instant asOfTime = tnsId.getAsOfTime();
        Instant asAtTime = tnsId.getAsAtTime();
        assertEquals(asOfTime, timestamp1);
        assertEquals(asAtTime, timestamp2);
    }

    @Test
    public void testEquals() throws Exception {
        BiTemporalNamespaceId<Integer> tnsId = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z", Integer.class);
        boolean actual = tnsId.equals(null);
        assertFalse(actual);

        Instant notAnNsId = Instant.now();
        actual = tnsId.equals(notAnNsId);
        assertFalse(actual);

        BiTemporalNamespaceId<Integer> tnsId2 = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z", Integer.class);
        assertTrue(tnsId2.equals(tnsId));
        assertTrue(tnsId.equals(tnsId2));
    }

    @Test
    public void testCompareTo() throws Exception {
        BiTemporalNamespaceId<Integer> tnsId1 = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z", Integer.class);
        BiTemporalNamespaceId<Integer> tnsId2 = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:01Z/2008-01-05T22:00:00Z", Integer.class);
        int actual = tnsId1.compareTo(tnsId2);
        assertTrue(0 > actual);

        actual = tnsId2.compareTo(tnsId1);
        assertTrue(0 < actual);
    }

    @Test
    @Override
    public void testToNamespaceId() throws Exception {
        final Instant asOfTime = Instant.now();
        final Instant asAtTime = asOfTime.plusSeconds(3600L); // add 1 hour
        BiTemporalNamespaceId<String> btnsId = new BiTemporalNamespaceId<>("namespace", "id", asOfTime, asAtTime);
        NamespaceId<String> actual = btnsId.toNamespaceId();
        NamespaceId<String> expected = new NamespaceId<>("namespace", "id");
        assertNotSame(actual, btnsId);
        assertEquals(actual, expected);
    }

    @Test
    public void testToTemporalNamespaceId() throws Exception {
        BiTemporalNamespaceId<Integer> tnsId1 = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z", Integer.class);
        TemporalNamespaceId<Integer> tnsId2 = tnsId1.toTemporalNamespaceId();
        assertEquals(tnsId2.getAsOfTime(), tnsId1.getAsOfTime());
        assertEquals(tnsId2.getId(), tnsId1.getId());
        assertEquals(tnsId2.getNamespace(), tnsId1.getNamespace());
        assertNotSame(tnsId2, tnsId1);
    }

    @Test
    public void testToBiTemporalNamespaceId() throws Exception {
        BiTemporalNamespaceId<Integer> tnsId1 = BiTemporalNamespaceId.fromString("bitemporal/1/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z", Integer.class);
        BiTemporalNamespaceId<Integer> tnsId2 = tnsId1.toBiTemporalNamespaceId();
        assertEquals(tnsId2, tnsId1);
        assertSame(tnsId2, tnsId1);
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

    @DataProvider
    public Iterator<Object[]> toStringData() {
        int count = GOOD_BTNSID_STRING_OBJS.size();
        return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRING_OBJS.get(i), GOOD_BTNSID_STRINGS.get(i) }).iterator();
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
        return GOOD_BTNSID_TYPED_OBJS.stream().map(btnsId -> new Object[]{ btnsId }).iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends Comparable<T>, R extends NamespaceId<T>> R fromStringWithType(String nsIdString, Class<T> idType) {
        return (R)BiTemporalNamespaceId.fromString(nsIdString, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <R extends NamespaceId<String>> R fromString(String nsIdString) {
        return (R)BiTemporalNamespaceId.fromString(nsIdString);
    }

    private Iterator<Object[]> fromStringStreamIterator(boolean goodData, boolean withType) {
        if (goodData) {
            int count = GOOD_BTNSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_BTNSID_TYPED_OBJS.get(i) }).iterator()
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), GOOD_BTNSID_STRING_OBJS.get(i) }).iterator();
        } else {
            return (withType)
                    ? IntStream.range(0, GOOD_BTNSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), BAD_ID_TYPES.get(i) }).iterator()
                    : BAD_BTNSID_STRINGS.stream().map(bad_btnsid_string -> new Object[]{bad_btnsid_string}).iterator();
        }
    }

}