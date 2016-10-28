/*
 *  Copyright 2016 $user
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

/**
 * Subclass of {@link NamespaceId} that holds a high-precision
 * creation timestamp. Instances of this class are
 * {@link Comparable} and provide 'natural' sort order. Additionally, a
 * {@code NamespaceId} object produces a text representation of itself from
 * which it can later be reconstructed.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceId<T extends Comparable<T>> extends NamespaceId<T> {

    public static <T extends Comparable<T>> TemporalNamespaceId<T> fromString(String idString, Class<T> idType) {
        int separatorPos1 = idString.indexOf(SEPARATOR);
        int separatorPos2 = idString.lastIndexOf(SEPARATOR);
        String namespace = idString.substring(0, separatorPos1);
        String idVal = idString.substring((separatorPos1 + 1), separatorPos2);
        String asOfTimeVal = idString.substring((separatorPos2 + 1));
        T id = null;
        try {
            Constructor<T> constructor = idType.getConstructor(String.class);
            id = constructor.newInstance(idVal);
        } catch (Exception e) {
            id = idType.cast(idVal);
        }
        return new TemporalNamespaceId<T>(namespace, id, Double.parseDouble(asOfTimeVal));
    }

    public static TemporalNamespaceId<String> fromString(String idString) {
        return fromString(idString, String.class);
    }

    private final double asOfTime;

    protected TemporalNamespaceId(String namespace, T id, double asOfTime) {
        super(namespace, id);
        this.asOfTime = asOfTime;
    }

    public double getAsOfTime() {
        return asOfTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TemporalNamespaceId<?> that = (TemporalNamespaceId<?>) o;
        return Double.compare(that.asOfTime, asOfTime) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp = Double.doubleToLongBits(asOfTime);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(NamespaceId<T> o) {
        int comp = super.compareTo(o);
        if (0 != comp) return comp;
        TemporalNamespaceId<T> that = (TemporalNamespaceId<T>)o;
        return Double.compare(this.asOfTime, that.asOfTime);
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString()).append(SEPARATOR).append(asOfTime).toString();
    }

}
