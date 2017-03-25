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

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.Arrays;

import static io.github.davejoyce.util.Arguments.requireNonNull;

/**
 * Subclass of {@link TemporalNamespaceId} that holds a high-precision
 * modification timestamp. Instances of this class are {@link Comparable} and
 * provide 'natural' sort order. Additionally, a {@code BiTemporalNamespaceId}
 * object produces a text representation of itself from which it can later be
 * reconstructed.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BiTemporalNamespaceId<T extends Comparable<T>> extends TemporalNamespaceId<T> {

    public static <T extends Comparable<T>> BiTemporalNamespaceId<T> fromString(String idString, Class<T> idType) {
        int separatorPos1 = idString.indexOf(SEPARATOR);
        int separatorPos2 = idString.indexOf(SEPARATOR, (separatorPos1 + 1));
        int separatorPos3 = idString.lastIndexOf(SEPARATOR);
        if (-1 == separatorPos1 || -1 == separatorPos2 || separatorPos3 == separatorPos2) {
            throw new IllegalArgumentException("ID string must contain at least 3 '" + SEPARATOR + "' separators");
        }
        String namespace = idString.substring(0, separatorPos1);
        String idVal = idString.substring((separatorPos1 + 1), separatorPos2);
        String asOfTimeVal = idString.substring((separatorPos2 + 1), separatorPos3);
        String asAtTimeVal = idString.substring((separatorPos3 + 1), idString.length());
        T id = null;
        try {
            Constructor<T> constructor = idType.getConstructor(String.class);
            id = constructor.newInstance(idVal);
        } catch (Exception e) {
            id = idType.cast(idVal);
        }
        Instant asOfTime = Instant.parse(asOfTimeVal);
        Instant asAtTime = Instant.parse(asAtTimeVal);
        return new BiTemporalNamespaceId<>(namespace, id, asOfTime, asAtTime);
    }

    public static BiTemporalNamespaceId<String> fromString(String idString) {
        return fromString(idString, String.class);
    }

    private final long[] asAtTime;

    public BiTemporalNamespaceId(String namespace,
                                 T id,
                                 long asOfTimeSeconds,
                                 int asOfTimeNanoseconds,
                                 long asAtTimeSeconds,
                                 int asAtTimeNanoseconds) {
        super(namespace, id, asOfTimeSeconds, asOfTimeNanoseconds);
        this.asAtTime = new long[] { asAtTimeSeconds, asAtTimeNanoseconds };
    }

    public BiTemporalNamespaceId(String namespace, T id, long asOfTimeSeconds, long asAtTimeSeconds) {
        this(namespace, id, asOfTimeSeconds, 0, asAtTimeSeconds, 0);
    }

    public BiTemporalNamespaceId(String namespace, T id, Instant asOfTime, Instant asAtTime) {
        this(namespace, id,
                requireNonNull(asOfTime).getEpochSecond(), requireNonNull(asOfTime).getNano(),
                requireNonNull(asAtTime).getEpochSecond(), requireNonNull(asAtTime).getNano());
    }

    public BiTemporalNamespaceId(String namespace, T id, Instant asOfTime) {
        this(namespace, id, asOfTime, Instant.now());
    }

    public BiTemporalNamespaceId(String namespace, T id) {
        this(namespace, id, Instant.now(), Instant.now());
    }

    public Instant getAsAtTime() {
        return Instant.ofEpochSecond(asAtTime[0], asAtTime[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BiTemporalNamespaceId<?> that = (BiTemporalNamespaceId<?>) o;
        return Arrays.equals(asAtTime, that.asAtTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(asAtTime);
        return result;
    }

    @Override
    public int compareTo(NamespaceId<T> o) {
        int comp = super.compareTo(o);
        if (0 != comp) return comp;
        BiTemporalNamespaceId<T> that = (BiTemporalNamespaceId<T>)o;
        comp = Long.valueOf(this.asAtTime[0]).compareTo(that.asAtTime[0]);
        if (0 != comp) return comp;
        return Long.valueOf(this.asAtTime[1]).compareTo(that.asAtTime[1]);
    }

    @Override
    public String toString() {
        return super.toString() + SEPARATOR + getAsAtTime().toString();
    }

}
