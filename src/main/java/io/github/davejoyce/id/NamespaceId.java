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
import java.util.Objects;

/**
 * Unique identifier within a particular namespace. Instances of this class are
 * {@link Comparable} and provide 'natural' sort order. Additionally, a
 * {@code NamespaceId} object produces a text representation of itself from
 * which it can later be reconstructed.
 *
 * @param <T> comparable type of ID value
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class NamespaceId<T extends Comparable<T>> implements Comparable<NamespaceId<T>> {

    public static final char SEPARATOR = '/';

    public static <T extends Comparable<T>> NamespaceId<T> fromString(String idString, Class<T> idType) {
        int separatorPos = idString.indexOf(SEPARATOR);
        String namespace = idString.substring(0, separatorPos);
        String idVal = idString.substring((separatorPos + 1), idString.length());
        T id = null;
        try {
            Constructor<T> constructor = idType.getConstructor(String.class);
            id = constructor.newInstance(idVal);
        } catch (Exception e) {
            id = idType.cast(idVal);
        }
        return new NamespaceId<T>(namespace, id);
    }

    public static NamespaceId<String> fromString(String idString) {
        return fromString(idString, String.class);
    }

    private final String namespace;
    private final T id;

    protected NamespaceId(String namespace, T id) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace argument cannot be null");
        this.id = Objects.requireNonNull(id, "ID argument cannot be null");
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
