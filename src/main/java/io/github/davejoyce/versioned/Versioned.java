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

package io.github.davejoyce.versioned;

/**
 * Core behavior of objects which are versioned; that is, objects which possess
 * a distinct version value for each incremental change in their total state.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface Versioned<T> extends Comparable<T> {

    /**
     * Determine if this object's version is after or later than the version of
     * the given object.
     *
     * @param o object to be compared
     * @return true if this object is after the argument by version, false
     *         otherwise
     */
    boolean after(Object o);

    /**
     * Determine if this object's version is before or earlier than the version
     * of the given object.
     *
     * @param o object to be compared
     * @return true if this object is before the argument by version, false
     *         otherwise
     */
    boolean before(Object o);

}
