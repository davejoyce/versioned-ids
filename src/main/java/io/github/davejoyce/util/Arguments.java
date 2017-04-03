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

package io.github.davejoyce.util;

/**
 * Utility methods for parameter argument checking.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class Arguments {

    /**
     * Default message of IllegalArgumentException thrown due to null argument.
     */
    public static final String ERROR_NULL_ARG = "Argument cannot be null";

    /**
     * Default message of IllegalArgumentException thrown due to empty String
     * argument.
     */
    public static final String ERROR_EMPTY_STRING = "Argument cannot be empty";

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private Arguments() {
        throw new AssertionError("Argument instances not allowed");
    }

    /**
     * Ensure non-null parameter requirement of the caller. If the specified
     * argument is null, an {@code IllegalArgumentException} with the given
     * message is thrown.
     *
     * @param argument method argument to be checked
     * @param errMsg error message of exception to be thrown if
     *               <tt>argument</tt> is null
     * @param <T> type or argument to be checked
     * @return specified <tt>argument</tt>
     * @throws IllegalArgumentException if <tt>argument</tt> is null
     */
    public static <T> T requireNonNull(final T argument, final String errMsg) {
        if (null == argument) {
            throw new IllegalArgumentException(errMsg);
        }
        return argument;
    }

    /**
     * Ensure non-null parameter requirement of the caller. If the specified
     * argument is null, an {@code IllegalArgumentException} with the
     * {@link #ERROR_NULL_ARG default} message is thrown.
     *
     * @param argument method argument to be checked
     * @param <T> type or argument to be checked
     * @return specified <tt>argument</tt>
     * @throws IllegalArgumentException if <tt>argument</tt> is null
     */
    public static <T> T requireNonNull(final T argument) {
        return requireNonNull(argument, ERROR_NULL_ARG);
    }

    /**
     * Ensure non-empty parameter requirement of the caller. If the specified
     * argument is empty, an {@code IllegalArgumentException} with the given
     * message is thrown.
     *
     * @param argument method argument to be checked
     * @param errMsg error message of exception to be thrown if
     *               <tt>argument</tt> is empty
     * @return specified <tt>argument</tt>
     * @throws IllegalArgumentException if <tt>argument</tt> is empty
     */
    public static String requireNonEmpty(final String argument,
                                         final String errMsg) {
        if (null == argument || "".equals(argument.trim())) {
            throw new IllegalArgumentException(errMsg);
        }
        return argument;
    }

    /**
     * Ensure non-empty parameter requirement of the caller. If the specified
     * argument is empty, an {@code IllegalArgumentException} with the
     * {@link #ERROR_EMPTY_STRING default} message is thrown.
     *
     * @param argument method argument to be checked
     * @return specified <tt>argument</tt>
     * @throws IllegalArgumentException if <tt>argument</tt> is empty
     */
    public static String requireNonEmpty(final String argument) {
        return requireNonEmpty(argument, ERROR_EMPTY_STRING);
    }

}
