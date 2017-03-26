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

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Supporting data provider and utility methods, constants for unit tests.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class TestSupport {

    public static final int BEFORE = -1;
    public static final int EQUAL  = 0;
    public static final int AFTER  = 1;

    private static final List<String> GOOD_NSID_STRINGS = new ArrayList<>();
    private static final List<NamespaceId<String>> GOOD_NSID_STRING_OBJS = new ArrayList<>();
    private static final List<NamespaceId<?>> GOOD_NSID_TYPED_OBJS = new ArrayList<>();
    private static final List<Class<? extends Comparable<?>>> GOOD_ID_TYPES = new ArrayList<>();

    private static final List<String> BAD_NSID_STRINGS = new ArrayList<>();
    private static final List<Class<? extends Comparable<?>>> BAD_ID_TYPES = new ArrayList<>();

    static {
        GOOD_NSID_STRINGS.add("namespace/id");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "id"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<>("namespace", "id"));
        GOOD_ID_TYPES.add(String.class);
        BAD_ID_TYPES.add(Integer.class);

        GOOD_NSID_STRINGS.add("namespace/2");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "2"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<>("namespace", 2));
        GOOD_ID_TYPES.add(Integer.class);
        BAD_ID_TYPES.add(Date.class);

        GOOD_NSID_STRINGS.add("namespace/3.141592");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "3.141592"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<>("namespace", 3.141592F));
        GOOD_ID_TYPES.add(Float.class);
        BAD_ID_TYPES.add(Integer.class);

        BAD_NSID_STRINGS.add(null);
        BAD_NSID_STRINGS.add("");
        BAD_NSID_STRINGS.add("/noNamespace");
        BAD_NSID_STRINGS.add("noId/");
    }

    private TestSupport() {}

    @DataProvider
    public static Iterator<Object[]> goodGetterDataIterator(Method m) {
        return goodGetterDataStream(m).iterator();
    }

    private static Stream<Object[]> goodGetterDataStream(Method m) {
        return GOOD_NSID_TYPED_OBJS.stream().map(nsId -> new Object[]{ nsId });
    }

    @DataProvider
    public static Iterator<Object[]> goodDataIterator(Method m) {
        return goodDataStream(m.getName().contains("WithType")).iterator();
    }

    private static Stream<Object[]> goodDataStream(boolean withType) {
        int count = GOOD_NSID_STRINGS.size();
        return (withType)
                ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_NSID_TYPED_OBJS.get(i) })
                : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_NSID_STRING_OBJS.get(i) });
    }

    @DataProvider
    public static Iterator<Object[]> badDataIterator(Method m) {
        return badDataStream(m.getName().contains("WithType")).iterator();
    }

    private static Stream<Object[]> badDataStream(boolean withType) {
        return (withType)
                ? IntStream.range(0, GOOD_NSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), BAD_ID_TYPES.get(i) })
                : BAD_NSID_STRINGS.stream().map(bad_nsid_string -> new Object[]{bad_nsid_string});
    }

    @DataProvider
    public static Iterator<Object[]> goodToStringDataIterator(Method m) {
        return goodToStringDataStream().iterator();
    }

    private static Stream<Object[]> goodToStringDataStream() {
        int count = GOOD_NSID_STRING_OBJS.size();
        return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRING_OBJS.get(i), GOOD_NSID_STRINGS.get(i) });
    }

    @DataProvider
    public static Object[][] equalityDataArray(Method m) {
        List<Class<?>> methodParamTypes = Arrays.asList(m.getParameterTypes());
        if (methodParamTypes.contains(NamespaceId.class)) {
            final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
            return new Object[][] {
                    new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 1), true},
                    new Object[]{new NamespaceId<Float>("namespace", 3.141592F), new NamespaceId<Float>("namespace", 3.141592F), true},
                    new Object[]{new NamespaceId<String>("namespace", "id"), new NamespaceId<String>("namespace", "ID"), false},
                    new Object[]{new NamespaceId<Integer>("namespace", 1), null, false},
                    new Object[]{nsId, nsId, true},
            };
        } else if (methodParamTypes.contains(TemporalNamespaceId.class)) {
            final Instant asOfTime = Instant.now();
            final Instant nextTime = asOfTime.plusSeconds(3600L); // add 1 hour
            final TemporalNamespaceId<Integer> tnsId = new TemporalNamespaceId<Integer>("identity", 10, asOfTime);
            return new Object[][] {
                new Object[]{new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), true},
                new Object[]{new TemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime), new TemporalNamespaceId<Float>("namespace", 3.141592F, asOfTime), true},
                new Object[]{new TemporalNamespaceId<String>("namespace", "id", asOfTime), new TemporalNamespaceId<String>("namespace", "ID", asOfTime), false},
                new Object[]{new TemporalNamespaceId<Integer>("namespace", 1, asOfTime), null, false},
                new Object[]{tnsId, tnsId, true},
            };
        } else {
            throw new IllegalArgumentException("Unsupported test method: " + m.toString());
        }
    }

    @DataProvider
    public static Object[][] compareToDataArray(Method m) {
        List<Class<?>> methodParamTypes = Arrays.asList(m.getParameterTypes());
        if (methodParamTypes.contains(NamespaceId.class)) {
            final NamespaceId<Integer> nsId = new NamespaceId<>("identity", 10);
            return new Object[][] {
                    new Object[]{new NamespaceId<Integer>("namespace", 1), new NamespaceId<Integer>("namespace", 2), BEFORE},
                    new Object[]{new NamespaceId<Integer>("apples", 1), new NamespaceId<Integer>("bananas", 1), BEFORE},
                    new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 1), AFTER},
                    new Object[]{new NamespaceId<Integer>("bananas", 1), new NamespaceId<Integer>("apples", 1), AFTER},
                    new Object[]{new NamespaceId<Integer>("namespace", 2), new NamespaceId<Integer>("namespace", 2), EQUAL},
                    new Object[]{nsId, nsId, EQUAL},
            };
        } else if (methodParamTypes.contains(TemporalNamespaceId.class)) {
            final Instant asOfTime = Instant.now();
            final Instant nextTime = asOfTime.plusSeconds(3600L); // add 1 hour
            final TemporalNamespaceId<Integer> tnsId = new TemporalNamespaceId<Integer>("identity", 10, asOfTime);
            return new Object[][] {
                    new Object[]{new TemporalNamespaceId<Integer>("apples", 1, asOfTime), new TemporalNamespaceId<Integer>("apples", 1, nextTime), BEFORE},
                    new Object[]{new TemporalNamespaceId<Integer>("apples", 1, nextTime), new TemporalNamespaceId<Integer>("apples", 1, asOfTime), AFTER},
                    new Object[]{tnsId, tnsId, EQUAL},
            };
        } else {
            throw new IllegalArgumentException("Unsupported test method: " + m.toString());
        }
    }

}
