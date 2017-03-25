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
     */
    public static <T extends Comparable<T>> NamespaceId<T> fromString(String idString, Class<T> idType) {
        int firstSeparatorPos = idString.indexOf(SEPARATOR);
        if (-1 == firstSeparatorPos) {
            throw new IllegalArgumentException("ID string must contain at least 1 '" + SEPARATOR + "' separator");
        }
        // ID string may be temporal or bi-temporal ID string being downcast;
        // disregard temporal components past 2nd slash
        int lastSeparatorPos = idString.lastIndexOf(SEPARATOR);
        int endPos = (lastSeparatorPos == firstSeparatorPos) ? idString.length() : lastSeparatorPos;

        String namespace = idString.substring(0, firstSeparatorPos);
        String idVal = idString.substring((firstSeparatorPos + 1), endPos);
        T id = null;
        try {
            Constructor<T> constructor = idType.getConstructor(String.class);
            id = constructor.newInstance(idVal);
        } catch (Exception e) {
            id = idType.cast(idVal);
        }
        return new NamespaceId<T>(namespace, id);
    }

    /**
     * Create a new {@code NamespaceId} instance with ID attribute of type
     * <tt>String</tt> from the specified string representation.
     *
     * @param idString '/' separated ID string to be parsed
     * @return new NamespaceId object
     */
    public static NamespaceId<String> fromString(String idString) {
        return fromString(idString, String.class);
    }

    private final String namespace;
    private final T id;

    protected NamespaceId(String namespace, T id) {
        this.namespace = requireNonEmpty(namespace, "Namespace argument cannot be empty");
        this.id = requireNonNull(id, "ID argument cannot be null");
    }

    public String getNamespace() {
        return namespace;
    }

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
        return new StringBuilder(namespace).append(SEPARATOR).append(id.toString()).toString();
    }

}
