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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Supporting data provider and utility methods, constants for unit tests.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class TestSupport {

    private static final List<String> GOOD_NSID_STRINGS = new ArrayList<>();
    private static final List<NamespaceId<String>> GOOD_NSID_STRING_OBJS = new ArrayList<>();
    private static final List<NamespaceId<?>> GOOD_NSID_TYPED_OBJS = new ArrayList<>();
    private static final List<String> GOOD_TNSID_STRINGS = new ArrayList<>();
    private static final List<TemporalNamespaceId<String>> GOOD_TNSID_STRING_OBJS = new ArrayList<>();
    private static final List<TemporalNamespaceId<?>> GOOD_TNSID_TYPED_OBJS = new ArrayList<>();
    private static final List<String> GOOD_BTNSID_STRINGS = new ArrayList<>();
    private static final List<BiTemporalNamespaceId<String>> GOOD_BTNSID_STRING_OBJS = new ArrayList<>();
    private static final List<BiTemporalNamespaceId<?>> GOOD_BTNSID_TYPED_OBJS = new ArrayList<>();
    private static final List<Class<? extends Comparable<?>>> GOOD_ID_TYPES = new ArrayList<>();

    private static final List<String> BAD_NSID_STRINGS = new ArrayList<>();
    private static final List<String> BAD_TNSID_STRINGS = new ArrayList<>();
    private static final List<String> BAD_BTNSID_STRINGS = new ArrayList<>();
    private static final List<Class<? extends Comparable<?>>> BAD_ID_TYPES = new ArrayList<>();

    static {
        // Instants of specific times
        Instant asOfTime = Instant.parse("1977-11-13T14:18:00Z");
        Instant asAtTime = Instant.parse("2008-01-05T22:00:00Z");

        GOOD_NSID_STRINGS.add("namespace/id");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "id"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<String>("namespace", "id"));
        GOOD_TNSID_STRINGS.add("temporal/id/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "id", asOfTime));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<String>("temporal", "id", asOfTime));
        GOOD_BTNSID_STRINGS.add("bitemporal/id/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "id", asOfTime, asAtTime));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<String>("bitemporal", "id", asOfTime, asAtTime));
        GOOD_ID_TYPES.add(String.class);
        BAD_ID_TYPES.add(Integer.class);

        GOOD_NSID_STRINGS.add("namespace/2");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "2"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<Integer>("namespace", 2));
        GOOD_TNSID_STRINGS.add("temporal/2/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "2", asOfTime));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<Integer>("temporal", 2, asOfTime));
        GOOD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "2", asOfTime, asAtTime));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<Integer>("bitemporal", 2, asOfTime, asAtTime));
        GOOD_ID_TYPES.add(Integer.class);
        BAD_ID_TYPES.add(Date.class);

        GOOD_NSID_STRINGS.add("namespace/3.141592");
        GOOD_NSID_STRING_OBJS.add(new NamespaceId<>("namespace", "3.141592"));
        GOOD_NSID_TYPED_OBJS.add(new NamespaceId<Float>("namespace", 3.141592F));
        GOOD_TNSID_STRINGS.add("temporal/3.141592/1977-11-13T14:18:00Z");
        GOOD_TNSID_STRING_OBJS.add(new TemporalNamespaceId<>("temporal", "3.141592", asOfTime));
        GOOD_TNSID_TYPED_OBJS.add(new TemporalNamespaceId<Float>("temporal", 3.141592F, asOfTime));
        GOOD_BTNSID_STRINGS.add("bitemporal/3.141592/1977-11-13T14:18:00Z/2008-01-05T22:00:00Z");
        GOOD_BTNSID_STRING_OBJS.add(new BiTemporalNamespaceId<>("bitemporal", "3.141592", asOfTime, asAtTime));
        GOOD_BTNSID_TYPED_OBJS.add(new BiTemporalNamespaceId<Float>("bitemporal", 3.141592F, asOfTime, asAtTime));
        GOOD_ID_TYPES.add(Float.class);
        BAD_ID_TYPES.add(Integer.class);

        BAD_NSID_STRINGS.add(null);
        BAD_NSID_STRINGS.add("");
        BAD_NSID_STRINGS.add("/noNamespace");
        BAD_NSID_STRINGS.add("noId/");
        BAD_TNSID_STRINGS.add(null);
        BAD_TNSID_STRINGS.add("");
        BAD_TNSID_STRINGS.add("temporal/2");
        BAD_TNSID_STRINGS.add("temporal/2/1977-11-13");
        BAD_BTNSID_STRINGS.add(null);
        BAD_BTNSID_STRINGS.add("");
        BAD_BTNSID_STRINGS.add("bitemporal/2");
        BAD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z");
        BAD_BTNSID_STRINGS.add("bitemporal/2/1977-11-13T14:18:00Z/2008-01-05");
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
    public static Iterator<Object[]> goodNamespaceIdStringIterator() {
        return goodDataStream(NamespaceId.class, false).iterator();
    }

    @DataProvider
    public static Iterator<Object[]> goodNamespaceIdStringIteratorWithType() {
        return goodDataStream(NamespaceId.class, true).iterator();
    }

    @DataProvider
    public static Iterator<Object[]> goodTemporalNamespaceIdStringIterator() {
        return goodDataStream(TemporalNamespaceId.class, false).iterator();
    }

    @DataProvider
    public static Iterator<Object[]> goodTemporalNamespaceIdStringIteratorWithType() {
        return goodDataStream(TemporalNamespaceId.class, true).iterator();
    }

    @DataProvider
    public static Iterator<Object[]> goodBiTemporalNamespaceIdStringIterator() {
        return goodDataStream(BiTemporalNamespaceId.class, false).iterator();
    }

    @DataProvider
    public static Iterator<Object[]> goodBiTemporalNamespaceIdStringIteratorWithType() {
        return goodDataStream(BiTemporalNamespaceId.class, true).iterator();
    }

    private static Stream<Object[]> goodDataStream(Class<? extends NamespaceId> testTargetClass, boolean withType) {
        if (NamespaceId.class.equals(testTargetClass)) {
            int count = GOOD_NSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_NSID_TYPED_OBJS.get(i) })
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), GOOD_NSID_STRING_OBJS.get(i) });
        } else if (TemporalNamespaceId.class.equals(testTargetClass)) {
            int count = GOOD_TNSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_TNSID_TYPED_OBJS.get(i) })
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), GOOD_TNSID_STRING_OBJS.get(i) });
        } else if (BiTemporalNamespaceId.class.equals(testTargetClass)) {
            int count = GOOD_BTNSID_STRINGS.size();
            return (withType)
                    ? IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), GOOD_ID_TYPES.get(i), GOOD_BTNSID_TYPED_OBJS.get(i) })
                    : IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), GOOD_BTNSID_STRING_OBJS.get(i) });
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + testTargetClass);
        }
    }

    @DataProvider
    public static Iterator<Object[]> badDataIterator(Method m) {
        return badDataStream(m).iterator();
    }

    private static Stream<Object[]> badDataStream(Method m) {
        List<Class<?>> methodParamTypes = Arrays.asList(m.getParameterTypes());
        // Determine from the test class
        Class<?> testClass = m.getDeclaringClass();
        if (1 == methodParamTypes.size() && methodParamTypes.contains(String.class)) {
            if (NamespaceIdTest.class.equals(testClass)) {
                return BAD_NSID_STRINGS.stream().map(bad_nsid_string -> new Object[]{bad_nsid_string});
            } else if (TemporalNamespaceIdTest.class.equals(testClass)) {
                return BAD_TNSID_STRINGS.stream().map(bad_tnsid_string -> new Object[]{bad_tnsid_string});
            } else if (BiTemporalNamespaceIdTest.class.equals(testClass)) {
                return BAD_BTNSID_STRINGS.stream().map(bad_btnsid_string -> new Object[]{bad_btnsid_string});
            } else {
                throw new IllegalArgumentException("Unsupported test class: " + testClass.getCanonicalName());
            }
        } else if (2 == methodParamTypes.size()) {
            if (NamespaceIdTest.class.equals(testClass)) {
                return IntStream.range(0, GOOD_NSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_NSID_STRINGS.get(i), BAD_ID_TYPES.get(i) });
            } else if (TemporalNamespaceIdTest.class.equals(testClass)) {
                return IntStream.range(0, GOOD_TNSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_TNSID_STRINGS.get(i), BAD_ID_TYPES.get(i) });
            } else if (BiTemporalNamespaceIdTest.class.equals(testClass)) {
                return IntStream.range(0, GOOD_BTNSID_STRINGS.size()).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRINGS.get(i), BAD_ID_TYPES.get(i) });
            } else {
                throw new IllegalArgumentException("Unsupported test class: " + testClass.getCanonicalName());
            }
        } else {
            throw new IllegalArgumentException("Unsupported test method: " + m.toString());
        }
    }

    @DataProvider
    public static Iterator<Object[]> goodToStringDataIterator(Method m) {
        return goodToStringDataStream(m).iterator();
    }

    private static Stream<Object[]> goodToStringDataStream(Method m) {
        List<Class<?>> methodParamTypes = Arrays.asList(m.getParameterTypes());
        if (methodParamTypes.contains(NamespaceId.class)) {
            int count = GOOD_NSID_STRING_OBJS.size();
            return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_NSID_STRING_OBJS.get(i), GOOD_NSID_STRINGS.get(i) });
        } else if (methodParamTypes.contains(TemporalNamespaceId.class)) {
            int count = GOOD_TNSID_STRING_OBJS.size();
            return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_TNSID_STRING_OBJS.get(i), GOOD_TNSID_STRINGS.get(i) });
        } else if (methodParamTypes.contains(BiTemporalNamespaceId.class)) {
            int count = GOOD_BTNSID_STRING_OBJS.size();
            return IntStream.range(0, count).mapToObj(i -> new Object[]{ GOOD_BTNSID_STRING_OBJS.get(i), GOOD_BTNSID_STRINGS.get(i) });
        } else {
            throw new IllegalArgumentException("Unsupported test method: " + m.toString());
        }
    }

}
