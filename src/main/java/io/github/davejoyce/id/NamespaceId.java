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

import io.github.davejoyce.versioned.Versioned;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.github.davejoyce.util.Arguments.requireNonEmpty;
import static io.github.davejoyce.util.Arguments.requireNonNull;

/**
 * Basic unique identifier within a particular namespace. An instance of this
 * class is {@link Versioned}; it provides 'natural' sort order and can
 * determine whether it occurs before or after another version of itself.
 * Additionally, a {@code NamespaceId} object produces a string-encoded
 * representation of itself from which it can later be reconstructed, supported
 * by all serialization protocols.
 *
 * @param <T> comparable type of ID attribute
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceId<T extends Comparable<T>>
    implements Versioned<NamespaceId<T>> {

    /**
     * Separator of fields in string-encoded {@code NamespaceId} instance.
     */
    public static final char SEPARATOR = '/';

    /**
     * Magic number prime for hashcode calculation.
     */
    protected static final int HASHCODE_MULTIPLIER = 31;

    /**
     * Create a new {@code NamespaceId} instance with ID attribute of type
     * <tt>T</tt> from the specified string representation.
     *
     * @param idString '/' separated ID string to be parsed
     * @param idType class of ID attribute type
     * @param <T> comparable type of ID attribute
     * @return new NamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to NamespaceId with ID attribute of
     *                                  type <tt>T</tt>
     */
    public static <T extends Comparable<T>> NamespaceId<T> fromString(
            final String idString,
            final Class<T> idType) {
        String s = requireNonEmpty(idString, "ID string cannot be empty");
        int separatorPos1 = s.indexOf(SEPARATOR);
        if (-1 == separatorPos1) {
            throw new IllegalArgumentException(
                    "NamespaceId string must contain at least 1 '"
                    + SEPARATOR
                    + "' separator");
        }
        // ID string may be temporal or bi-temporal ID string being downcast;
        // disregard temporal components past 2nd slash
        int separatorPosLast = s.lastIndexOf(SEPARATOR);
        int endPos = s.length();
        if (separatorPos1 < separatorPosLast) {
            endPos = separatorPosLast;
        }
        final String ns = requireNonEmpty(s.substring(0, separatorPos1),
                "Namespace segment cannot be empty");
        String idVal = requireNonEmpty(s.substring((separatorPos1 + 1), endPos),
                "Identifier segment cannot be empty");
        final T id = castId(idVal, idType);
        return new NamespaceId<>(ns, id);
    }

    /**
     * Create a new {@code NamespaceId} instance with ID attribute of type
     * <tt>String</tt> from the specified string representation.
     *
     * @param idString '/' separated ID string to be parsed
     * @return new NamespaceId object
     * @throws IllegalArgumentException if <tt>idString</tt> cannot be converted
     *                                  back to NamespaceId with ID attribute of
     *                                  type <tt>String</tt>
     */
    public static NamespaceId<String> fromString(final String idString) {
        return fromString(idString, String.class);
    }

    /**
     * Convert the specified string representation of the ID value to its actual
     * type. This utility method is protected for use within {@code NamespaceId}
     * and subclasses only.
     *
     * @param idValue string to be converted
     * @param idType class of ID attribute type
     * @param <T> comparable type of ID attribute
     * @return converted ID attribute
     */
    protected static <T extends Comparable<T>> T castId(final String idValue,
                                                        final Class<T> idType) {
        T id;
        try {
            // 1. Look for standard 'valueOf' factory method on type
            Method valueOfMethod = idType.getMethod("valueOf", String.class);
            id = idType.cast(valueOfMethod.invoke(null, idValue));
        } catch (IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e1) {
            try {
                // 2. Look for constructor that takes a string representation of
                //    value
                Constructor<T> c = idType.getConstructor(String.class);
                id = c.newInstance(idValue);
            } catch (IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException
                    | NoSuchMethodException e2) {
                try {
                    // 3. See if we can just cast it to the target type
                    //    (last resort)
                    id = idType.cast(idValue);
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException(
                            "Identifier segment cannot be converted to type: "
                            + idType.getCanonicalName());
                }
            }
        }
        return id;
    }

    /**
     * Namespace of this object.
     */
    private final String namespace;

    /**
     * Unique, comparable ID value of this object.
     */
    private final T id;

    /**
     * Construct a {@code NamespaceId} object in the given namespace and with
     * the given unique identifier value.
     *
     * @param ns namespace to be occupied
     * @param idValue unique identifier value
     * @throws IllegalArgumentException if <tt>namespace</tt> is empty or
     *                                  <tt>id</tt> is null
     */
    public NamespaceId(final String ns, final T idValue) {
        this.namespace = requireNonEmpty(ns, "Namespace cannot be empty");
        this.id = requireNonNull(idValue, "ID cannot be null");
    }

    /**
     * Get the namespace occupied by this object.
     *
     * @return namespace (never null)
     */
    public final String getNamespace() {
        return namespace;
    }

    /**
     * Get the unique identifier value of this object.
     *
     * @return ID (never null)
     */
    public final T getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NamespaceId<?> that = (NamespaceId<?>) o;
        return (namespace.equals(that.namespace) && id.equals(that.id));
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = HASHCODE_MULTIPLIER * result + id.hashCode();
        return result;
    }

    @Override
    public int compareTo(final NamespaceId<T> o) {
        if (this == o) {
            return 0;
        }
        int comp = this.namespace.compareTo(o.namespace);
        if (0 != comp) {
            return comp;
        }
        return this.id.compareTo(o.id);
    }

    @Override
    public boolean after(final NamespaceId<T> o) {
        return (0 < this.compareTo(o));
    }

    @Override
    public boolean before(final NamespaceId<T> o) {
        return (0 > this.compareTo(o));
    }

    /**
     * Get {@code NamespaceId} representation of this object.
     *
     * @return NamespaceId representation
     */
    public NamespaceId<T> toNamespaceId() {
        return this;
    }

    @Override
    public String toString() {
        return namespace + SEPARATOR + id.toString();
    }

}
