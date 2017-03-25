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
public class Arguments {

    public static final String ERROR_NULL_ARG = "Argument cannot be null";
    public static final String ERROR_EMPTY_STRING = "String argument cannot be empty";

    public static <T> T requireNonNull(T argument, String errMsg) {
        if (null == argument) {
            throw new IllegalArgumentException(errMsg);
        }
        return argument;
    }

    public static <T> T requireNonNull(T argument) {
        return requireNonNull(argument, ERROR_NULL_ARG);
    }

    public static String requireNonEmpty(String argument, String errMsg) {
        if (null == argument || "".equals(argument.trim())) {
            throw new IllegalArgumentException(errMsg);
        }
        return argument;
    }

    public static String requireNonEmpty(String argument) {
        return requireNonEmpty(argument, ERROR_EMPTY_STRING);
    }

}
