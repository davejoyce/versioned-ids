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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.github.davejoyce.util.Arguments.requireNonEmpty;
import static io.github.davejoyce.util.Arguments.requireNonNull;

/**
 * Unique identifier within a particular namespace. Instances of this class are
 * {@link Comparable} and provide 'natural' sort order. Additionally, a
 * {@code NamespaceId} object produces a text representation of itself from
 * which it can later be reconstructed.
 *
 * @param <T> comparable type of ID attribute
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceId<T extends Comparable<T>> implements Comparable<NamespaceId<T>> {

    public static final char SEPARATOR = '/';

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
    public static <T extends Comparable<T>> NamespaceId<T> fromString(String idString, Class<T> idType) {
        String rawIdString = requireNonEmpty(idString, "NamespaceId string cannot be empty");
        int firstSeparatorPos = rawIdString.indexOf(SEPARATOR);
        if (-1 == firstSeparatorPos) {
            throw new IllegalArgumentException("NamespaceId string must contain at least 1 '" + SEPARATOR + "' separator");
        }
        // ID string may be temporal or bi-temporal ID string being downcast;
        // disregard temporal components past 2nd slash
        int lastSeparatorPos = rawIdString.lastIndexOf(SEPARATOR);
        int endPos = (lastSeparatorPos == firstSeparatorPos) ? rawIdString.length() : lastSeparatorPos;

        final String namespace = requireNonEmpty(rawIdString.substring(0, firstSeparatorPos), "Namespace segment cannot be empty");
        String idVal = requireNonEmpty(rawIdString.substring((firstSeparatorPos + 1), endPos), "Identifier segment cannot be empty");
        final T id = convertId(idVal, idType);
        return new NamespaceId<>(namespace, id);
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
    public static NamespaceId<String> fromString(String idString) {
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
    protected static <T extends Comparable<T>> T convertId(String idValue, Class<T> idType) {
        final T id;
        try {
            // 1. Look for standard 'valueOf' factory method on type
            Method valueOfMethod = idType.getMethod("valueOf", String.class);
            id = idType.cast(valueOfMethod.invoke(null, idValue));
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e1) {
            try {
                // 2. Look for constructor that takes a string representation of value
                Constructor<T> constructor = idType.getConstructor(String.class);
                id = constructor.newInstance(idValue);
            } catch (IllegalAccessException |
                    InstantiationException |
                    InvocationTargetException |
                    NoSuchMethodException e2) {
                try {
                    // 3. See if we can just cast it to the target type (last resort)
                    id = idType.cast(idValue);
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("Identifier segment cannot be converted to type: " + idType.getCanonicalName());
                }
            }
        }
        return id;
    }

    private final String namespace;
    private final T id;

    /**
     * Construct a {@code NamespaceId} object in the given namespace and with
     * the given unique identifier value.
     *
     * @param namespace namespace to be occupied
     * @param id unique identifier value
     * @throws IllegalArgumentException if <tt>namespace</tt> is empty or
     *                                  <tt>id</tt> is null
     */
    public NamespaceId(String namespace, T id) {
        this.namespace = requireNonEmpty(namespace, "Namespace argument cannot be empty");
        this.id = requireNonNull(id, "ID argument cannot be null");
    }

    /**
     * Get the namespace occupied by this object.
     *
     * @return namespace (never null)
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Get the unique identifier value of this object.
     *
     * @return ID (never null)
     */
    public T getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceId<?> that = (NamespaceId<?>) o;
        return (namespace.equals(that.namespace) && id.equals(that.id));
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public int compareTo(NamespaceId<T> o) {
        if (this == o) return 0;
        int comp = this.namespace.compareTo(o.namespace);
        if (0 != comp) {
            return comp;
        }
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return namespace + SEPARATOR + id.toString();
    }

}
