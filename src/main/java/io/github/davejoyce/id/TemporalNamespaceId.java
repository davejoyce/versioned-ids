/*
 *  Copyright 2016 David Joyce
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
 * Subclass of {@link NamespaceId} that holds a high-precision creation
 * timestamp. Instances of this class are {@link Comparable} and provide
 * 'natural' sort order. Additionally, a {@code TemporalNamespaceId} object
 * produces a text representation of itself from which it can later be
 * reconstructed.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceId<T extends Comparable<T>> extends NamespaceId<T> {

    public static <T extends Comparable<T>> TemporalNamespaceId<T> fromString(String idString, Class<T> idType) {
        int separatorPos1 = idString.indexOf(SEPARATOR);
        int separatorPos2 = idString.lastIndexOf(SEPARATOR);
        if (-1 == separatorPos1 || separatorPos2 == separatorPos1) {
            throw new IllegalArgumentException("ID string must contain at least 2 '" + SEPARATOR + "' separators");
        }
        String namespace = idString.substring(0, separatorPos1);
        String idVal = idString.substring((separatorPos1 + 1), separatorPos2);
        String asOfTimeVal = idString.substring((separatorPos2 + 1), idString.length());
        T id = null;
        try {
            Constructor<T> constructor = idType.getConstructor(String.class);
            id = constructor.newInstance(idVal);
        } catch (Exception e) {
            id = idType.cast(idVal);
        }
        Instant asOfTime = Instant.parse(asOfTimeVal);
        return new TemporalNamespaceId<T>(namespace, id, asOfTime);
    }

    public static TemporalNamespaceId<String> fromString(String idString) {
        return fromString(idString, String.class);
    }

    private final long[] asOfTime;

    public TemporalNamespaceId(String namespace, T id, long asOfTimeSeconds, int asOfTimeNanoseconds) {
        super(namespace, id);
        this.asOfTime = new long[] { asOfTimeSeconds, asOfTimeNanoseconds };
    }

    public TemporalNamespaceId(String namespace, T id, long asOfTimeSeconds) {
        this(namespace, id, asOfTimeSeconds, 0);
    }

    public TemporalNamespaceId(String namespace, T id, Instant asOfTime) {
        this(namespace, id, requireNonNull(asOfTime).getEpochSecond(), requireNonNull(asOfTime).getNano());
    }

    public TemporalNamespaceId(String namespace, T id) {
        this(namespace, id, Instant.now());
    }

    public Instant getAsOfTime() {
        return Instant.ofEpochSecond(asOfTime[0], asOfTime[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TemporalNamespaceId<?> that = (TemporalNamespaceId<?>) o;
        return Arrays.equals(this.asOfTime, that.asOfTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(asOfTime);
        return result;
    }

    @Override
    public int compareTo(NamespaceId<T> o) {
        int comp = super.compareTo(o);
        if (0 != comp) return comp;
        TemporalNamespaceId<T> that = (TemporalNamespaceId<T>)o;
        comp = Long.valueOf(this.asOfTime[0]).compareTo(that.asOfTime[0]);
        if (0 != comp) return comp;
        return Long.valueOf(this.asOfTime[1]).compareTo(that.asOfTime[1]);
    }

    @Override
    public String toString() {
        return super.toString() + SEPARATOR + getAsOfTime().toString();
    }

}
