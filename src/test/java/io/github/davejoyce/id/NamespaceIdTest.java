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
 * Unit tests of {@code NamespaceId}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceIdTest extends AbstractIdTest {

    private static final List<String> GOOD_NSID_STRINGS = new ArrayList<>();
    private static final List<String> BAD_NSID_STRINGS = new ArrayList<>();
    private static final List<NamespaceId<String>> GOOD_NSID_STRING_OBJS = new ArrayList<>();
    private static final List<NamespaceId<?>> GOOD_NSID_TYPED_OBJS = new ArrayList<>();

    static {
        GOOD_NSID_STRINGS.add("namespace/id");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "id"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<String>("namespace", "id"));
        GOOD_NSID_STRINGS.add("namespace/2");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "2"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<Integer>("namespace", 2));
        GOOD_NSID_STRINGS.add("namespace/3.141592");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "3.141592"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<Float>("namespace", 3.141592F));
        BAD_NSID_STRINGS.add(null);
        BAD_NSID_STRINGS.add("");
        BAD_NSID_STRINGS.add("/noNamespace");
        BAD_NSID_STRINGS.add("noId/");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromStringNoSeparator() throws Exception {
        String badNsIdString = "namespace.1";
        NamespaceId<Integer> nsId = NamespaceId.fromString(badNsIdString, Integer.class);
        fail("Expected IllegalArgumentException on missing separator '/'");
    }

    @Test
    public void testFromStringIgnoresAdditionalSegments() throws Exception {
        NamespaceId<Integer> expected = new NamespaceId<>("namespace", 1);
        String nsIdWithExtraSegmentString = "namespace/1/1977-11-13T14:18:00Z";
        NamespaceId<Integer> actual = NamespaceId.fromString(nsIdWithExtraSegmentString, Integer.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testEquals() throws Exception {
        NamespaceId<Integer> nsId = NamespaceId.fromString("namespace/1", Integer.class);
        boolean actual = nsId.equals(null);
        assertFalse(actual);

        Instant notAnNsId = Instant.now();
        actual = nsId.equals(notAnNsId);
        assertFalse(actual);

        NamespaceId<Integer> nsId2 = NamespaceId.fromString("otherNamespace/1", Integer.class);
        actual = nsId.equals(nsId2);
        assertFalse(actual);

        NamespaceId<Integer> nsId3 = NamespaceId.fromString("namespace/2", Integer.class);
        actual = nsId.equals(nsId3);
        assertFalse(actual);

        NamespaceId<Integer> nsId4 = NamespaceId.fromString("namespace/1", Integer.class);
        assertTrue(nsId4.equals(nsId));
        assertTrue(nsId.equals(nsId4));
    }

    @Test
    @Override
    public void testToNamespaceId() throws Exception {
        NamespaceId<String> expected = new NamespaceId<>("namespace", "id");
        NamespaceId<String> actual = expected.toNamespaceId();
        assertSame(actual, expected);
    }

    @DataProvider
    public Object[][] compareToData() {
        final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
        return new Object[][] {
                new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 2), BEFORE},
                new Object[]{new NamespaceId<Integer>("apples", 1), new NamespaceId<Integer>("bananas", 1), BEFORE},
                new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 1), AFTER},
                new Object[]{new NamespaceId<Integer>("bananas", 1), new NamespaceId<Integer>("apples", 1), AFTER},
                new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 2), EQUAL},
                new Object[]{nsId, nsId, EQUAL},
        };
    }

    @DataProvider
    public Object[][] equalityData() {
        final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
        return new Object[][] {
                new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 1), true},
                new Object[]{new NamespaceId<Float>("namespace", 3.141592F), new NamespaceId<Float>("namespace", 3.141592F), true},
                new Object[]{new NamespaceId<String>("namespace", "id"), new NamespaceId<String>("namespace", "ID"), false},
                new Object[]{new NamespaceId<Integer>("namespace", 1), null, false},
                new Object[]{nsId, nsId, true},
        };
    }

    @DataProvider
    public Iterator<Object[]> toStringData() {
        int count = GOOD_NSID_STRING_OBJS.size();
        return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRING_OBJS.get(i), GOOD_NSID_STRINGS.get(i) }).iterator();
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
        return GOOD_NSID_TYPED_OBJS.stream().map(nsId -> new Object[]{ nsId }).iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends Comparable<T>, R extends NamespaceId<T>> R fromStringWithType(String nsIdString, Class<T> idType) {
        return (R)NamespaceId.fromString(nsIdString, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <R extends NamespaceId<String>> R fromString(String nsIdString) {
        return (R)NamespaceId.fromString(nsIdString);
    }

    private Iterator<Object[]> fromStringStreamIterator(boolean goodData, boolean withType) {
        if (goodData) {
            int count = GOOD_NSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_NSID_TYPED_OBJS.get(i) }).iterator()
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_NSID_STRING_OBJS.get(i) }).iterator();
        } else {
            return (withType)
                    ? IntStream.range(0, GOOD_NSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), BAD_ID_TYPES.get(i) }).iterator()
                    : BAD_NSID_STRINGS.stream().map(bad_nsid_string -> new Object[]{bad_nsid_string}).iterator();
        }
    }

}