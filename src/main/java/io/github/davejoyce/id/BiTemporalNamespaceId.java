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

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static io.github.davejoyce.util.Arguments.requireNonEmpty;
import static io.github.davejoyce.util.Arguments.requireNonNull;

/**
 * Subclass of {@link TemporalNamespaceId} that provides a high precision
 * timestamp of the time at which point this version was adjusted or observed.
 * An instance of this class is {@link io.github.davejoyce.versioned.Versioned};
 * it provides 'natural' sort order and can determine whether it occurs before
 * or after another version of itself. Additionally, a
 * {@code BiTemporalNamespaceId} object produces a string-encoded representation
 * of itself from which it can later be reconstructed, supported by all
 * serialization protocols.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BiTemporalNamespaceId<T extends Comparable<T>>
    extends TemporalNamespaceId<T> {

    /**
     * Create a new {@code BiTemporalNamespaceId} instance with ID attribute of
     * type <tt>T</tt> from the specified string representation.
     *
     * @param idString '/' separated ID string to be parsed
     * @param idType class of ID attribute type
     * @param <T> comparable type of ID attribute
     * @return new BiTemporalNamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to BiTemporalNamespaceId with ID
     *                                  attribute of type <tt>T</tt>
     */
    public static <T extends Comparable<T>> BiTemporalNamespaceId<T> fromString(
            final String idString,
            final Class<T> idType) {
        String s = requireNonEmpty(idString, "ID string cannot be empty");
        int separatorPos1 = s.indexOf(SEPARATOR);
        int separatorPos2 = s.indexOf(SEPARATOR, (separatorPos1 + 1));
        int separatorPos3 = s.lastIndexOf(SEPARATOR);
        if (-1 == separatorPos1
            || -1 == separatorPos2
            || separatorPos3 == separatorPos2) {
            throw new IllegalArgumentException(
                    "ID string must contain at least 3 '"
                    + SEPARATOR
                    + "' separators");
        }
        final String namespace = s.substring(0, separatorPos1);
        String idVal = s.substring((separatorPos1 + 1), separatorPos2);
        String asOfTimeVal = s.substring((separatorPos2 + 1), separatorPos3);
        String asAtTimeVal = s.substring((separatorPos3 + 1), s.length());
        final T id = castId(idVal, idType);
        final Instant asOfTime;
        final Instant asAtTime;
        try {
            asOfTime = Instant.parse(asOfTimeVal);
            asAtTime = Instant.parse(asAtTimeVal);
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("Bad timestamp segment", dtpe);
        }
        return new BiTemporalNamespaceId<>(namespace, id, asOfTime, asAtTime);
    }

    /**
     * Create a new {@code BiTemporalNamespaceId} instance with ID attribute of
     * type <tt>String</tt> from the specified string representation.
     *
     * @param idStr '/' separated ID string to be parsed
     * @return new BiTemporalNamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to BiTemporalNamespaceId with ID
     *                                  attribute of type <tt>String</tt>
     */
    public static BiTemporalNamespaceId<String> fromString(final String idStr) {
        return fromString(idStr, String.class);
    }

    /**
     * Adjustment/observation (as at) time of this object, stored as a long
     * array.
     * Elements:
     * <ol>
     *     <li value="0">'as at' time in seconds since UNIX epoch</li>
     *     <li>nanosecond adjustment beyond epoch second value</li>
     * </ol>
     */
    private final long[] asAtTime;

    /**
     * Construct a {@code BiTemporalNamespaceId} object in the given namespace
     * and with the given unique identifier value, effective as of the specified
     * seconds (and nanoseconds) since the UNIX epoch, adjusted or observed as
     * at the 2nd specified number of seconds (and nanoseconds) since the UNIX
     * epoch.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfTimeSeconds effective (as of) time in seconds since UNIX epoch
     * @param asOfTimeNanoseconds nanosecond adjustment beyond epoch second
     *                            value
     * @param asAtTimeSeconds adjusted/observed (as at) time in seconds since
     *                        UNIX epoch
     * @param asAtTimeNanoseconds nanosecond adjustment beyond epoch second
     *                            value
     * @throws IllegalArgumentException if <tt>ns</tt> is empty or
     *                                  <tt>idValue</tt> is null
     */
    public BiTemporalNamespaceId(final String ns,
                                 final T idValue,
                                 final long asOfTimeSeconds,
                                 final int asOfTimeNanoseconds,
                                 final long asAtTimeSeconds,
                                 final int asAtTimeNanoseconds) {
        super(ns, idValue, asOfTimeSeconds, asOfTimeNanoseconds);
        this.asAtTime = new long[] {
                asAtTimeSeconds, asAtTimeNanoseconds
        };
    }

    /**
     * Construct a {@code BiTemporalNamespaceId} object in the given namespace
     * and with the given unique identifier value, effective as of the specified
     * seconds since the UNIX epoch, adjusted or observed as at the 2nd
     * specified number of seconds since the UNIX epoch.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfTimeSeconds effective (as of) time in seconds since UNIX epoch
     * @param asAtTimeSeconds adjusted/observed (as at) time in seconds since
     *                        UNIX epoch
     * @throws IllegalArgumentException if <tt>ns</tt> is empty or
     *                                  <tt>idValue</tt> is null
     */
    public BiTemporalNamespaceId(final String ns,
                                 final T idValue,
                                 final long asOfTimeSeconds,
                                 final long asAtTimeSeconds) {
        this(ns, idValue, asOfTimeSeconds, 0, asAtTimeSeconds, 0);
    }

    /**
     * Construct a {@code BiTemporalNamespaceId} object in the given namespace
     * and with the given unique identifier value, effective as of the specified
     * instant, adjusted or observed as at the 2nd specified instant.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @param asOfInstant instant of effective (as of) time
     * @param asAtInstant instant of adjusted/observed (as at) time
     * @throws IllegalArgumentException if <tt>ns</tt> is empty or
     *                                  <tt>idValue</tt> is null
     */
    public BiTemporalNamespaceId(final String ns,
                                 final T idValue,
                                 final Instant asOfInstant,
                                 final Instant asAtInstant) {
        this(ns, idValue,
             requireNonNull(asOfInstant).getEpochSecond(),
             requireNonNull(asOfInstant).getNano(),
             requireNonNull(asAtInstant).getEpochSecond(),
             requireNonNull(asAtInstant).getNano());
    }

    /**
     * Get the instant at which the object identified by this ID is adjusted or
     * observed.
     *
     * @return instant of adjusted/observed (as at) time
     */
    public Instant getAsAtTime() {
        return Instant.ofEpochSecond(asAtTime[0], asAtTime[1]);
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
        BiTemporalNamespaceId<?> that = (BiTemporalNamespaceId<?>) o;
        return Arrays.equals(asAtTime, that.asAtTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = HASHCODE_MULTIPLIER * result + Arrays.hashCode(asAtTime);
        return result;
    }

    @Override
    public int compareTo(final NamespaceId<T> o) {
        int comp = super.compareTo(o);
        if (0 != comp) {
            return comp;
        }
        BiTemporalNamespaceId<T> that = (BiTemporalNamespaceId<T>) o;
        comp = Long.valueOf(this.asAtTime[0]).compareTo(that.asAtTime[0]);
        if (0 != comp) {
            return comp;
        }
        return Long.valueOf(this.asAtTime[1]).compareTo(that.asAtTime[1]);
    }

    /**
     * {@inheritDoc}
     */
    public TemporalNamespaceId<T> toTemporalNamespaceId() {
        return new TemporalNamespaceId<>(
                getNamespace(),
                getId(),
                getAsOfTime()
        );
    }

    /**
     * Get {@code BiTemporalNamespaceId} representation of this object.
     *
     * @return BiTemporalNamespaceId representation
     */
    public BiTemporalNamespaceId<T> toBiTemporalNamespaceId() {
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + SEPARATOR + getAsAtTime().toString();
    }

}
