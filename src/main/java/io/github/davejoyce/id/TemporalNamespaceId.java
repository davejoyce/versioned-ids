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

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static io.github.davejoyce.util.Arguments.requireNonEmpty;
import static io.github.davejoyce.util.Arguments.requireNonNull;

/**
 * Subclass of {@link NamespaceId} that provides a high precision timestamp of
 * the time from which point this version is effective. An instance of this
 * class is {@link io.github.davejoyce.versioned.Versioned}; it provides
 * 'natural' sort order and can determine whether it occurs before or after
 * another version of itself. Additionally, a {@code TemporalNamespaceId} object
 * produces a string-encoded representation of itself from which it can later be
 * reconstructed, supported by all serialization protocols.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TemporalNamespaceId<T extends Comparable<T>>
    extends NamespaceId<T> {

    /**
     * Create a new {@code TemporalNamespaceId} instance with ID attribute of
     * type <tt>T</tt> from the specified string representation.
     *
     * @param idString '/' separated ID string to be parsed
     * @param idType class of ID attribute type
     * @param <T> comparable type of ID attribute
     * @return new TemporalNamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to TemporalNamespaceId with ID
     *                                  attribute of type <tt>T</tt>
     */
    public static <T extends Comparable<T>> TemporalNamespaceId<T> fromString(
            final String idString,
            final Class<T> idType) {
        String s = requireNonEmpty(idString, "ID string cannot be empty");
        int separatorPos1 = s.indexOf(SEPARATOR);
        int separatorPos2 = s.lastIndexOf(SEPARATOR);
        if (-1 == separatorPos1 || separatorPos2 == separatorPos1) {
            throw new IllegalArgumentException(
                    "ID string must contain at least 2 '"
                    + SEPARATOR
                    + "' separators");
        }
        final String namespace = s.substring(0, separatorPos1);
        String idVal = s.substring((separatorPos1 + 1), separatorPos2);
        String asOfTimeVal = s.substring((separatorPos2 + 1), s.length());
        final T id = castId(idVal, idType);
        final Instant asOfTime;
        try {
            asOfTime = Instant.parse(asOfTimeVal);
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("Bad 'asOf' timestamp: "
                                               + asOfTimeVal, dtpe);
        }
        return new TemporalNamespaceId<>(namespace, id, asOfTime);
    }

    /**
     * Create a new {@code TemporalNamespaceId} instance with ID attribute of
     * type <tt>String</tt> from the specified string representation.
     *
     * @param idStr '/' separated ID string to be parsed
     * @return new TemporalNamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to TemporalNamespaceId with ID
     *                                  attribute of type <tt>String</tt>
     */
    public static TemporalNamespaceId<String> fromString(final String idStr) {
        return fromString(idStr, String.class);
    }

    /**
     * Effective (as of) time of this object, stored as a long array.
     * Elements:
     * <ol>
     *     <li value="0">'as of' time in seconds since UNIX epoch</li>
     *     <li>nanosecond adjustment beyond epoch second value</li>
     * </ol>
     */
    private final long[] asOfTime;

    /**
     * Construct a {@code TemporalNamespaceId} object in the given namespace and
     * with the given unique identifier value, effective as of the specified
     * seconds (and nanoseconds) since the UNIX epoch.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfTimeSeconds effective (as of) time in seconds since UNIX epoch
     * @param asOfTimeNanoseconds nanosecond adjustment beyond epoch second
     *                            value
     * @throws IllegalArgumentException if <tt>ns</tt> is empty or
     *                                  <tt>idValue</tt> is null
     */
    public TemporalNamespaceId(final String ns,
                               final T idValue,
                               final long asOfTimeSeconds,
                               final int asOfTimeNanoseconds) {
        super(ns, idValue);
        this.asOfTime = new long[] {
                asOfTimeSeconds, asOfTimeNanoseconds
        };
    }

    /**
     * Construct a {@code TemporalNamespaceId} object in the given namespace and
     * with the given unique identifier value, effective as of the specified
     * seconds (and nanoseconds) since the UNIX epoch.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfTimeSeconds effective (as of) time in seconds since UNIX epoch
     * @throws IllegalArgumentException if <tt>ns</tt> is empty or
     *                                  <tt>idValue</tt> is null
     */
    public TemporalNamespaceId(final String ns,
                               final T idValue,
                               final long asOfTimeSeconds) {
        this(ns, idValue, asOfTimeSeconds, 0);
    }

    /**
     * Construct a {@code TemporalNamespaceId} object in the given namespace and
     * with the given unique identifier value, effective as of the specified
     * instant.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfInstant instant of effective (as of) time
     * @throws IllegalArgumentException if <tt>namespace</tt> is empty or
     *                                  <tt>id</tt> is null
     */
    public TemporalNamespaceId(final String ns,
                               final T idValue,
                               final Instant asOfInstant) {
        this(ns, idValue,
            requireNonNull(asOfInstant).getEpochSecond(),
            requireNonNull(asOfInstant).getNano());
    }

    /**
     * Get the instant from which the object identified by this ID is effective.
     *
     * @return instant of effective (as of) time
     */
    public final Instant getAsOfTime() {
        return Instant.ofEpochSecond(asOfTime[0], asOfTime[1]);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TemporalNamespaceId<?> that = (TemporalNamespaceId<?>) o;
        return Arrays.equals(this.asOfTime, that.asOfTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = HASHCODE_MULTIPLIER * result + Arrays.hashCode(asOfTime);
        return result;
    }

    @Override
    public int compareTo(final NamespaceId<T> o) {
        int comp = super.compareTo(o);
        if (0 != comp) {
            return comp;
        }
        TemporalNamespaceId<T> that = (TemporalNamespaceId<T>) o;
        comp = Long.valueOf(this.asOfTime[0]).compareTo(that.asOfTime[0]);
        if (0 != comp) {
            return comp;
        }
        return Long.valueOf(this.asOfTime[1]).compareTo(that.asOfTime[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceId<T> toNamespaceId() {
        return new NamespaceId<>(getNamespace(), getId());
    }

    /**
     * Get {@code TemporalNamespaceId} representation of this object.
     *
     * @return TemporalNamespaceId representation
     */
    public TemporalNamespaceId<T> toTemporalNamespaceId() {
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + SEPARATOR + getAsOfTime().toString();
    }

}
